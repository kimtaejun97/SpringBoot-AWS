package com.study.springboot.domain.posts;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Entity Class, PK Type>
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
