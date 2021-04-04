package com.study.springboot.web;

import com.study.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//컨트롤러를 JSON을 반환하는 컨트롤러로 만들어줌.
@RestController
public class HelloControllerDto {

    //HTTP Get의 요청을 받을 수 있는 API를 만들어 줌.
    @GetMapping("/hello/dto")
    //@RequestParam : 외부에서 API로 넘김 파라미터를 가져옴.
    public HelloResponseDto hello(@RequestParam("name") String
                                          name,
                                  @RequestParam("amount") int
                                          amount) {
        return new HelloResponseDto(name,amount);
    }
}
