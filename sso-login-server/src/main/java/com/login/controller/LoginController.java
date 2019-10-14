package com.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.sso.pojo.User;
import com.sso.util.CookieUtil;
import com.sso.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author huangfu
 */
@Controller
public class LoginController {

    @Value("${login.ttlMillis}")
    private long ttlMillis;

    /**
     * 跳转方法
     * @param sourceUrl
     * @param modelAndView
     * @return
     */
    @RequestMapping("index")
    public ModelAndView index(String sourceUrl, ModelAndView modelAndView){
        modelAndView.addObject("sourceUrl",sourceUrl);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /**
     * 登录逻辑
     * @param user
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("login")
    @ResponseBody
    public String login(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * 模拟查询数据库
         */
        if("123".equals(user.getUserName()) && "123".equals(user.getPassword())){
            Map<String,Object> map = new HashMap<String,Object>(5);
            user.setAddress("北京市朝阳区");
            user.setAge(20);
            user.setId(UUID.randomUUID().toString());
            map.put("user",user);
            //获取token
            String token = JwtUtil.createJWT(map, ttlMillis,"皇甫");
            CookieUtil.setCookie(response,"token",token,60 * 60 * 2);
            return token;
        }
        return null;
    }
    @RequestMapping("verification")
    @ResponseBody
    public String verification(String token,HttpServletRequest request){
        String sub = JwtUtil.verification(token,"皇甫");
        if(sub != null){
            return sub;
        }
        return null;
    }

    @RequestMapping("outLogin")
    public void outLogin(HttpServletResponse response, HttpSession session){
        CookieUtil.deleteCookie(response,"token");
        session.removeAttribute("user");
    }
}
