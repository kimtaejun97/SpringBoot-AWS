package com.study.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //email로 이미 가입된 이메일인지 확인.
    Optional<User> findByEmail(String email);
}
