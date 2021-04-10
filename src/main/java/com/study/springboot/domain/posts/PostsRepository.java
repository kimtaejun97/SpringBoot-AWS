package com.study.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Entity Class, PK Type>
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
