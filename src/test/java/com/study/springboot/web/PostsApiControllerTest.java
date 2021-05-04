package com.study.springboot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @After
    public void cleanup()throws Exception{
        postsRepository.deleteAll();
    }
    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles="USER")
    public void createPosts() throws Exception {
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
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(saveRequestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);


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
    @WithMockUser(roles="USER")
    public void updatePosts() throws Exception {
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

//        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
//
//        //when
//        ResponseEntity<Long> responseEntity =restTemplate.exchange(url, HttpMethod.PUT, requestEntity,Long.class);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(expectedContent);

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

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
