package com.study.springboot.web.dto;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class HelloResponseDtoTest {

    @Test
    public void lombokTest(){
        String name = "test";
        int amount = 10000;

        //생성자 확인
        HelloResponseDto dto =new HelloResponseDto(name,amount);

        //getter확인
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }

}
