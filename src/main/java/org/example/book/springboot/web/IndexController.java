package org.example.book.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        //머스테치 스타터 덕분에 컨트롤러에 문자열을 반환할 때, 앞의 경로(prefix)와 뒤의 파일 확장자(suffix)는 자동으로 지정.
        //default prefix : src/main/resources/templates
        //default suffix : .mustache
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }
}
