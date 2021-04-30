package com.study.springboot.config.auth.dto;

import com.study.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SesstionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SesstionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}