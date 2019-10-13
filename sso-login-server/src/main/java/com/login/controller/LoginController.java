package com.login.controller;

import com.sso.pojo.User;
import com.sso.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangfu
 */
@Controller
public class LoginController {

    @Value("${login.ttlMillis}")
    private long ttlMillis;

    @RequestMapping("index")
    public ModelAndView index(String sourceUrl, ModelAndView modelAndView){
        modelAndView.addObject("sourceUrl",sourceUrl);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping("login")
    @ResponseBody
    public boolean login(User user){
        if("123".equals(user.getUserName()) && "123".equals(user.getPassword())){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("user",user);
            String salt = JwtUtil.createJWT(map, ttlMillis, "阴天而且开心");
            System.out.println(salt);
            return true;
        }
        return false;
    }
}
