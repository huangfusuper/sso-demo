package com.user.controller;

import com.sso.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author huangfu
 */
@RestController
public class UserController {

    @RequestMapping("getUser")
    public Object getUser(HttpServletRequest request){
        System.out.println("------------------------------------------");
        HttpSession session = request.getSession();
        Object userName = session.getAttribute("user");
        return userName;
    }
}
