package com.sso.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * 第一部分我们称它为头部（header),声明类型，这里是jwt!声明加密的算法 通常直接使用 HMAC SHA256
 * {
 *   'typ': 'JWT',
 *   'alg': 'HS256'
 * }
 * 然后将头部进行base64加密（该加密是可以对称解密的),构成了第一部分.(eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9)
 * 第二部分我们称其为载荷（payload, 类似于飞机上承载的物品)，
 *      iss: jwt签发者
 *      sub: jwt所面向的用户
 *      aud: 接收jwt的一方
 *      exp: jwt的过期时间，这个过期时间必须要大于签发时间
 *      nbf: 定义在什么时间之前，该jwt都是不可用的.
 *      iat: jwt的签发时间
 *      jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
 * 第三部分是签证（signature).
 * @author 皇甫
 */
public class JwtUtil {

    /**
     * 新建jwt
     * @param map
     * @param ttlMillis
     * @param iDentity 身份可以看作是盐
     * @return
     */
    public static String createJWT(Map<String,Object> map, long ttlMillis, String... iDentity){
        String iDentityString = "";
        if(iDentity != null && iDentity.length>0){
            for (int i = 0; i < iDentity.length; i++) {
                iDentityString+=iDentity[i];
            }
        }
        //head部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        /**
         * 创建 JwtBuilder
         * setHeaderParam  指定头信息
         * setSubject  主体信息
         * setIssuedAt 创建时间
         * signWith  绑定
         * setExpiration 过期时间
         * setIssuer 签发者
         */
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("identity",iDentityString)
                .setSubject(map.toString())
                .setIssuedAt(date)
                .signWith(signatureAlgorithm,generaKey())
                .setExpiration(new Date(l+ttlMillis))
                .setIssuer("huangfu");
        return builder.compact();
    }

    /**
     * 解码
     * @param jwt
     * @return
     */
    public static Map<String,Object> parseJwt(String jwt){
        SecretKey secretKey = generaKey();
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        return body;
    }

    private static SecretKey generaKey(){
        String stringKey = "55qH55Sr56eR5pif";
        //解码
        byte[] decode = Base64.getDecoder().decode(stringKey);
        //用于构建秘密密钥规范
        SecretKeySpec aes = new SecretKeySpec(decode, 0, decode.length, "AES");
        return aes;
    }

    public static void main(String[] args) throws InterruptedException {
        //Base64.Encoder encoder = Base64.getEncoder();
        //String keyStr = "皇甫科星";
        //byte[] encode = encoder.encode(keyStr.getBytes());
        //55qH55Sr56eR5pif


        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("id", UUID.randomUUID().toString());
        stringObjectHashMap.put("userName","张三");
        stringObjectHashMap.put("age",80);
        String jwt = createJWT(stringObjectHashMap, 400000,"施瓦辛格");
        jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6IumYtOWkqeiAjOS4lOW8gOW_gyIsInN1YiI6Int1c2VyPVVzZXIoaWQ9bnVsbCwgdXNlck5hbWU9MTIzLCBwYXNzd29yZD0xMjMsIGFkZHJlc3M9bnVsbCwgYWdlPTApfSIsImlhdCI6MTU3MDk3OTIwMCwiZXhwIjoxNTcwOTc5ODAwLCJpc3MiOiJodWFuZ2Z1In0.lS4mp0AFiQPZjOOf73ZOFj1cefoesIhEdFwfmAeYSuA";
        System.out.println(jwt);
        Thread.sleep(2000);
        long l = System.currentTimeMillis();
        System.out.println(parseJwt(jwt));
        System.out.println(System.currentTimeMillis()-l);
    }
}