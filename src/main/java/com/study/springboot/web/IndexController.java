package com.study.springboot.web;

import com.study.springboot.config.auth.dto.SesstionUser;
import com.study.springboot.domain.user.User;
import com.study.springboot.service.posts.PostsService;
import com.study.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model){
        //서버 템플릿 엔진에서 사용할 수 있는 객체 저장 (attributeName으로 index.mustache에 전달)
        model.addAttribute("posts", postsService.findAllDesc());

        //user Name 전달
        SesstionUser user = (SesstionUser) httpSession.getAttribute("user");
        if(user !=null)
            model.addAttribute("userName",user.getName());

        //mustache로 인해 앞의 경로와 확장자는 자동으로 지정됨.
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
