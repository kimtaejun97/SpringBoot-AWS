package com.study.springboot.web;

import com.study.springboot.domain.posts.Posts;
import com.study.springboot.domain.posts.PostsRepository;
import com.study.springboot.web.dto.PostsResponseDto;
import com.study.springboot.web.dto.PostsSaveRequestDto;
import com.study.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void cleanup()throws Exception{
        postsRepository.deleteAll();
    }
    @Before
    public void Given(){

    }

    @Test
    public void createPosts(){
        //given
        String title = "test title";
        String content = "test content";
        PostsSaveRequestDto saveRequestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("kimtaejun")
                .build();

        String url = "http://localhost:" +port +"api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url,saveRequestDto, Long.class);


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //DB에 저장된 데이터를 불러와서 비교.
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void ReadPosts(){
        Posts savePosts =postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("kimtaejun")
                .build());

        Long readId = savePosts.getId();
        String url = "http://localhost:"+port +"api/v1/posts/"+readId;


        PostsResponseDto responseObject = restTemplate.getForObject(url, PostsResponseDto.class);

        assertThat(responseObject.getTitle()).isEqualTo("title");
        assertThat(responseObject.getContent()).isEqualTo("content");
        assertThat(responseObject.getAuthor()).isEqualTo("kimtaejun");
    }

    @Test
    public void updatePosts(){
        String title = "first title";
        String content = "first content";
        String expectedTitle ="update title";
        String expectedContent = "update content";

        Posts savePosts =postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("kimtaejun")
                .build());

        Long updateId = savePosts.getId();

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:"+port +"api/v1/posts/"+updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity =restTemplate.exchange(url, HttpMethod.PUT, requestEntity,Long.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(expectedContent);

    }

    //Delete
    @Test
    public void PostsDelete(){
        //given
        String title = "test title";
        String content = "test content";
        PostsSaveRequestDto saveRequestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("kimtaejun")
                .build();

        String url = "http://localhost:" +port +"api/v1/posts";

        //when
        ResponseEntity<Long> saveResponseEntity = restTemplate.postForEntity(url,saveRequestDto, Long.class);


        //then
        assertThat(saveResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(saveResponseEntity.getBody()).isGreaterThan(0L);

        //저장 확인
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);

        Long deleteId = postsList.get(0).getId();

        url +="/"+deleteId;

        restTemplate.delete(url);

        List<Posts> postsList2 = postsRepository.findAll();
        assertThat(postsList2.size()).isEqualTo(0);
    }
}
