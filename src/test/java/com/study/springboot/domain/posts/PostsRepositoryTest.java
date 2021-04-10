package com.study.springboot.domain.posts;


import org.junit.After;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void saveLoadPost(){
        String title = "test post";
        String content = "test post content";

        //save | update
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("kimtaejun")
                .build()
        );

        //When 테이블에 있는 모든 데이터 조회.
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity(){
        LocalDateTime now = LocalDateTime.of(2021,4,11,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);

        assertThat(posts.getCreateDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

        System.out.println(">>>>>> create Date = "+posts.getCreateDate());
        System.out.println(">>>>>> modified Date = "+posts.getModifiedDate());
    }
}
