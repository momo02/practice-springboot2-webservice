package org.example.book.springboot.web;

import lombok.RequiredArgsConstructor;
import org.example.book.springboot.config.auth.dto.SessionUser;
import org.example.book.springboot.service.posts.PostsService;
import org.example.book.springboot.web.dto.PostsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;


    @GetMapping("/")
    public String index(Model model){

        model.addAttribute("posts",postsService.findAllDesc());

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if(user != null){
            model.addAttribute("userName",user.getName());
        }
        //머스테치 스타터 덕분에 컨트롤러에 문자열을 반환할 때, 앞의 경로(prefix)와 뒤의 파일 확장자(suffix)는 자동으로 지정.
        //default prefix : src/main/resources/templates
        //default suffix : .mustache
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
