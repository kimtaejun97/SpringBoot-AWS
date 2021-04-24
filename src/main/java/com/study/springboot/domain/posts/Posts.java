package com.study.springboot.domain.posts;

import com.study.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter //lombok
@NoArgsConstructor //lombok
@Entity //jpa, Table과 매칭될 클래스.
public class Posts extends BaseTimeEntity {

    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private  Long id;

    @Column(length = 500, nullable =false)
    private  String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title =title;
        this.content = content;
    }

}
