package com.api.signUri;

import com.alibaba.fastjson.JSON;
import com.api.encryption.AESUtil;
import com.api.exception.AuthorityException;
import com.api.exception.InfoCode;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

/**
 * 生成signURI、验证signURI
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
@Log
public class GetSignUri {
    private static String KEY=null;

    static {
        if (KEY==null){
            try {
                Properties properties=new Properties();
                InputStream in=GetSignUri.class.getClassLoader().getResourceAsStream("config/encryption.properties");
                properties.load(in);
                KEY=properties.getProperty("SignURIKey");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String createUri(SignUser user) {
//        try {
            String content=JSON.toJSONString(user);
            content=AESUtil.aesEncrypt(content,KEY);
            return content;
//            return URLEncoder.encode(content,"UTF-8");
//        } catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//        return null;
    }

    public static SignUser isLegal(String sign) {
//        try {
//            sign=URLDecoder.decode(sign,"UTF-8");
            String decrypt=AESUtil.aesDecrypt(sign,KEY);
            SignUser user=JSON.parseObject(decrypt,SignUser.class);
            if (user==null)
                throw new AuthorityException(InfoCode.SIGN_ERROR,null);
            Date now=new Date();
            if ((now.after(user.getDeadline()))||
                    (user.getAccountName()==null)||
                    (user.getMailbox()==null))
                throw new AuthorityException(InfoCode.SIGN_ERROR,null);
            return user;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public static void main(String[] args) {
        String s="6N5scZCH3kpGaP2tByiVOhuZXN0SMUQ5rvuiSL4%2BXzQ%2B%2FIKRrb0A%2FrS44SuqgQdf";
        String s2="6N5scZCH3kpGaP2tByiVOhuZXN0SMUQ5rvuiSL4%2BXzQ%2B%2FIKRrb0A%2FrS44SuqgQdf";
        if (s.equals(s2))
            System.out.println("相等");
//        System.out.println(AESUtil.aesDecrypt(s,KEY));
//        s="6N5scZCH3kpGaP2tByiVOhuZXN0SMUQ5rvuiSL4+XzQ+%2FIKRrb0A%2FrS44SuqgQdf";
        System.out.println(isLegal(s));
    }

}
