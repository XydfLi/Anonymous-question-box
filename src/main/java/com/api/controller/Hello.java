package com.api.controller;

import com.api.encryption.AESUtil;
import com.api.encryption.RSAUtil;
import com.api.model.Account;
import com.api.model.ResponseMes;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.UUID;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
@Path("/hello")
@Log
@PermitAll
@MultipartConfig
//@DenyAll//不允许任何人访问
public class Hello {

    @POST
    @Path("/say")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes sayHello(Account account){
        System.out.println(account.getAccountName());
        System.out.println("进入hello");
        return ResponseMes.success("Hi I am CL.");
    }

    @POST
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes test(Account account) {


        System.out.println("被公钥加密后的AES秘钥："+account.getAccountName());



        PrivateKey privateKey= RSAUtil.string2PrivateKey(RSAUtil.privateKeyStr);
        //加密后的内容Base64解码
        byte[] base642Byte=RSAUtil.base642Byte(account.getAccountName());
        //用私钥解密
        byte[] privateDecrypt=RSAUtil.privateDecrypt(base642Byte, privateKey);
        String AESKey=new String(privateDecrypt);//将秘钥RSA解密得到AES秘钥




        System.out.println("解密后得到AES私钥："+AESKey);


        String sss=AESUtil.aesDecrypt(account.getPassword(),AESKey);

        System.out.println("最终解密得到数据："+sss);

//        System.out.println("后端收到的数据："+account.getPassword());
//        String KEY="1234567887654321";
//        System.out.println("解密后的数据："+AESUtil.aesDecrypt(account.getPassword(),KEY));
        return ResponseMes.success(sss);
    }


}
