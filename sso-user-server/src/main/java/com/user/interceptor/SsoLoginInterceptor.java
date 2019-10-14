package com.user.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sso.util.CookieUtil;
import com.sso.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author huangfu
 */
@Component
public class SsoLoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取Cookie的token
        String token = CookieUtil.getCookie(request, "token");
        //获取这次的URL
        StringBuffer requestURL = request.getRequestURL();
        //如果token为null
        if(StringUtils.isBlank(token)){
            //重定向回去登录
            response.sendRedirect("http://localhost:8080/index?sourceUrl="+requestURL);
            return false;
        }
        //验证token是否正确
        String forObject = HttpClientUtil.doGet("http://localhost:8080/verification?token=" + token);
        //如果验证失败
        if(StringUtils.isBlank(forObject)){
            //重定向回去登录
            response.sendRedirect("http://localhost:8080/index?sourceUrl="+requestURL);
            return false;
        }
        //延长cookie的时间
        CookieUtil.setCookie(response, "token", token, 60 * 60 * 2);
        JSONObject jsonObject = JSON.parseObject(forObject);
        HttpSession session = request.getSession();
        //将携带的信息放入session
        session.setAttribute("user",jsonObject.get("user"));
        return true;
    }
}
