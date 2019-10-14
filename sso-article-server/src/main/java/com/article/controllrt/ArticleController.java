package com.article.controllrt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ArticleController {

    @RequestMapping("getData")
    public Object getData(HttpSession session){
        return session.getAttribute("user");
    }
}
