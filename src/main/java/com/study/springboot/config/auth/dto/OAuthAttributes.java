package com.study.springboot.config.auth.dto;

import com.study.springboot.domain.user.Role;
import com.study.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String,Object> attribute;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String,Object> attribute, String nameAttributeKey,
                           String name, String email, String picture){

        this.attribute = attribute;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email =email;
        this.picture = picture;

    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attribute){
        if ("naver".equals(registrationId))
            return ofNaver(userNameAttributeName,attribute);
        return ofGoogle(userNameAttributeName,attribute);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attribute){
        return OAuthAttributes.builder()
                .name((String)attribute.get("name"))
                .email((String)attribute.get("email"))
                .picture((String)attribute.get("picture"))
                .attribute(attribute)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attribute){
        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        return OAuthAttributes.builder()
                .name((String)response.get("id"))
                .email((String)response.get("email"))
                .picture((String)response.get("picture"))
                .attribute(attribute)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                //User 정보가 없을 경우이기 때문에 기본 권한을 Guest로 설정.
                .role(Role.GUEST)
                .build();
    }

}
