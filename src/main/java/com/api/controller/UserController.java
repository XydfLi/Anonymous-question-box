package com.api.controller;

import com.alibaba.fastjson.JSON;
import com.api.IP.IPUtil;
import com.api.VerifyCode.VerifyCodeUtil;
import com.api.dao.AccountDaoImpl;
import com.api.dao.AnswerBoxDaoImpl;
import com.api.dao.BaseCRUD;
import com.api.dao.QuestionBoxDaoImpl;
import com.api.encryption.AESUtil;
import com.api.encryption.MD5;
import com.api.encryption.RSAUtil;
import com.api.exception.AccountException;
import com.api.exception.AuthorityException;
import com.api.exception.InfoCode;
import com.api.jwt.JwtUtil;
import com.api.model.*;
import com.api.service.UserService;
import com.api.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一些用户的公操作
 * 包括获取RSA公钥、登入、注册、修改用户信息、激活邮箱、修改密码、修改头像
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/12
 */
@Log
@PermitAll//允许任何人访问
@Path("/users")
public class UserController {

    //service层接口
    private UserService userService=new UserServiceImpl();

    @GET
    @Path("/getAccountName")
    @PermitAll//允许任何人访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getAccountName(@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");
        if (accountName==null||accountName=="")
            accountName="visitor";
        return ResponseMes.success(accountName);
    }

    @GET
    @Path("/getAvatar/{accountName}")
    @PermitAll
    public void getAvatar(@PathParam("accountName") String accountName,
                          @Context HttpServletResponse response,
                          @Context ServletContext ctx){
        try {
            String filePath;
            String ctype;
            String fName="";
            if ("visitor".equals(accountName)||accountName==null||accountName==""){
                filePath=ctx.getRealPath("/WEB-INF/Avatars/1.jpg");
                ctype="image/jpg";
                fName="jpg";
            } else {
                BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
                Account account=accountBaseCRUD.readByKey(new Object[]{accountName,"accountName"});
                if (account==null)
                    throw new AccountException(InfoCode.ACCOUNT_NOT_EXIST,null);
                String av=account.getAvatar();
                filePath=ctx.getRealPath("/WEB-INF/Avatars/"+av);
                for (int i=av.length()-3;i<av.length();i++){
                    fName=fName+av.charAt(i);
                }
                ctype="image/"+fName;
            }

            BufferedImage image=ImageIO.read(new FileInputStream(filePath));

            //禁止图像缓存
            response.setHeader("Pragma","no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            //将图片输出给浏览器
            response.setContentType(ctype);
            OutputStream os=response.getOutputStream();
            ImageIO.write(image,fName,os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/getKey")
    @PermitAll//允许任何人访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getKey(){
        return ResponseMes.success(RSAUtil.publicKeyStr);
    }

    @GET
    @Path("/getCode")
    @PermitAll
    public void getCode(@Context HttpServletRequest request,
                        @Context HttpServletResponse response){

        //利用图片工具生成图片,第一个参数是生成的验证码,第二个参数是生成的图片
        Object[] objs=VerifyCodeUtil.createImage();

        //将记录验证码(SameSite属性...一言难尽，Chrome 80不可用！！！！！！！！！！)
        HttpSession session=request.getSession(true);
        session.setAttribute("code",(String)objs[0]);
//        ip_code.put(IPUtil.getClientIpAddress(request),(String)objs[0]);

        //禁止图像缓存
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //将图片输出给浏览器
        BufferedImage image=(BufferedImage)objs[1];
        response.setContentType("image/png");
        try {
            OutputStream os=response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @POST
//    @Path("/login")
    @Path("/login/{code}/{iden}")
    @PermitAll//允许任何人访问
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes login(@NonNull RequestMes requestMes,
                             @PathParam("code") String code1,
                             @PathParam("iden") int iden,
                             @Context HttpServletRequest request,
                             @Context HttpServletResponse response,
                             @Context ServletContext ctx) {
        //验证图形验证码
        HttpSession session=request.getSession(true);
        String code0=(String)session.getAttribute("code");
//        String code0=ip_code.get(IPUtil.getClientIpAddress(request));
        if ((code0==null)||(code0==""))
            throw new AuthorityException(InfoCode.CODE_ERROR,null);
        if (!code0.toLowerCase().equals(code1.toLowerCase()))
            throw new AuthorityException(InfoCode.CODE_ERROR,null);

        Account account=getAccount(requestMes,false);

        if (iden==1){
            if (account.getAccountName()!=null&&account.getAccountName().equals("admin"))
                throw new AccountException(InfoCode.ACCOUNTNAME_ERROR,null);
            if (account.getMailbox()!=null&&account.getMailbox().equals("xydflxy@sina.com"))
                throw new AccountException(InfoCode.MAILBOX_ERROR,null);
        } else if (iden==2){
            if (account.getAccountName()!=null&&!account.getAccountName().equals("admin"))
                throw new AccountException(InfoCode.ACCOUNTNAME_ERROR,null);
            if (account.getMailbox()!=null&&!account.getMailbox().equals("xydflxy@sina.com"))
                throw new AccountException(InfoCode.MAILBOX_ERROR,null);
        } else {
            throw new AccountException(InfoCode.ACCOUNTNAME_ERROR,new Throwable("不符合身份的登录"));
        }

        System.out.println("通过考验\n" +
                "用户名为："+account.getAccountName()+
        "，邮箱为："+account.getMailbox());

        account.setPassword(MD5.md5Password(account.getPassword()));//将用户密码MD5加密（加盐）

        String ip=IPUtil.getClientIpAddress(request);
        account=userService.loginLegal(account,ip);
        response.addHeader("jwt",userService.login(account,ip));
        log.info(account.getAccountName()+"登入成功");

        //TODO : 利用前端开发
//        try {
//            FileInputStream inputStream=new FileInputStream(ctx.getRealPath("/WEB-INF/Avatars")+"\\"+account.getAvatar());
//            int i=inputStream.available();
//            byte[] buff=new byte[i];
//            inputStream.read(buff);
//            inputStream.close();
//            response.setContentType("image/*");
//            OutputStream out=response.getOutputStream();
//            out.write(buff);
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return ResponseMes.success(0);
    }

    @POST
    @Path("/register/{code}")
    @PermitAll//允许任何人访问
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes register(@PathParam("code") String code1,
                                @Context HttpServletRequest request,
                                @NonNull RequestMes requestMes){

        //验证图形验证码
        HttpSession session=request.getSession(true);
        String code0=(String)session.getAttribute("code");
        if ((code0==null)||(code0==""))
            throw new AuthorityException(InfoCode.CODE_ERROR,null);
        if (!code0.toLowerCase().equals(code1.toLowerCase()))
            throw new AuthorityException(InfoCode.CODE_ERROR,null);

        Account account=getAccount(requestMes,false);
        account.setIdentity(Identity.ORDINARY.getCode());//只能注册普通用户
        account.setAvatar("1.jpg");//默认头像
        account.setIdentity(Identity.UNACTIVATED.getCode());//未激活用户
        account.setQuestionBoxStatus(false);//提问箱默认关闭
        userService.registerLegal(account);
        account.setPassword(MD5.md5Password(account.getPassword()));//将用户密码MD5加密（加盐）
        userService.register(account);
        return ResponseMes.success(0);
    }

    @POST
    @Path("/passwordUri")
    @PermitAll//允许任何人访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes passwordUri(@NonNull Account account,
                                   @Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");
        account.setAccountName(accountName);
        userService.passwordUriLegal(account);
        userService.passwordUri(account);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/password")
    @PermitAll//允许任何人访问
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes updatePassword(@NonNull RequestMes requestMes) {
        Account account=getAccount(requestMes,true);
        String sign=account.getMailbox();
        //只需要转化以下符号
//        sign=sign.replace(" ","%2B");
//        sign=sign.replace("/","%2F");

        Account account1=userService.updatePasswordLegal(sign,account);
        userService.updatePassword(account1);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/account")
    @RolesAllowed({"admin","ordi"})//允许管理员、普通用户访问
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseMes updateAccount(@NonNull RequestMes requestMes,
                                     @Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        Account account_=JSON.parseObject(requestMes.getData().toString(),Account.class);
        Account account;
        if ((account_.getPassword()!=null)&&(account_.getPassword()!=""))
            account=getAccount(requestMes,false);
        else
            account=account_;
        account.setAvatar(null);//这里不能修改头像地址
        userService.updateAccountLegal(account,accountName);
        userService.updateAccount(account,accountName);
        return ResponseMes.success(0);
    }

    @PUT
    @Path("/questionBoxStatus/{status}")
    @RolesAllowed({"admin","ordi"})//允许管理员、普通用户访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes updateQuestionBoxStatus(@PathParam("status") int status,
                                               @Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");
        userService.updateQuestionBoxStatus(status,accountName);
        return ResponseMes.success(0);
    }

    @POST
    @Path("/avatar")
    @RolesAllowed({"admin","ordi"})//只允许管理员、普通用户访问
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ResponseMes updateAvatar(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition disposition,
                                    @Context ServletContext ctx,
                                    @Context HttpHeaders headers,
                                    @Context HttpServletRequest request) {
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        /*
        查资料的时候说，在这种网络流的情况下InputStream、FormDataContentDisposition获取不到文件大小
        但是可以通过request.getContentLength()方法获取到文件的大小
         */
        userService.updateAvatar(fileInputStream,
                disposition,
                ctx,
                accountName,
                request.getContentLength());
        return ResponseMes.success(0);
    }

    @POST
    @Path("/emailUri")
    @RolesAllowed({"admin","ordi","unact"})//只允许管理员、普通用户、未激活用户访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes actEmailUri(Account account,@Context HttpHeaders headers){
        Claims claims=JwtUtil.parseJWT(headers.getHeaderString("jwt"));
        String accountName=(String)claims.get("accountName");

        userService.emailUriLegal(account,accountName);
        return ResponseMes.success(0);
    }

    @POST
    @Path("/actEmail")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes actEmail(@NonNull Account account){
        //只需要转化以下符号
//        sign=sign.replace(" ","%2B");
//        sign=sign.replace("/","%2F");

        String sign=account.getMailbox();
        userService.actEmail(sign);
        return ResponseMes.success(0);
    }

    @GET
    @Path("/accountInfo")
    @RolesAllowed({"admin"})//只允许管理员访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getAccountInfo(){
        BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
        AnswerBoxDaoImpl answerBoxDao=new AnswerBoxDaoImpl();
        BaseCRUD<QuestionBox> questionBoxBaseCRUD=new QuestionBoxDaoImpl();

        List<Account> accounts=accountBaseCRUD.readAll(null);
        int len=accounts.size();
        List<AccountInfo> ans=new ArrayList<>();

        for (int i=0;i<len;i++){
            Account account=accounts.get(i);
            AccountInfo accountInfo=new AccountInfo();
            accountInfo.setAccountName(account.getAccountName());
            accountInfo.setIdentity(account.getIdentity());

            List<AnswerBox> answerBoxList=answerBoxDao.AccountNameReadAll(new Object[]{account.getAccountName()});
            int sum=0;
            for (int j=0;j<answerBoxList.size();j++){
                if (answerBoxList.get(j).getStatus()==2)
                    sum++;
            }
            accountInfo.setBeQuestionedNum(answerBoxList.size());
            accountInfo.setQuestionAnsNum(sum);

            List<QuestionBox> questionBoxList=questionBoxBaseCRUD.readAll(new Object[]{account.getAccountName()});
            accountInfo.setQuestionNum(questionBoxList.size());

            ans.add(accountInfo);
        }

        return ResponseMes.success(new AccountInfoList(ans.size(),ans));
    }

    @GET
    @Path("/accountQInfo/{id}/{mark}")
    @RolesAllowed({"admin"})//只允许管理员访问
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMes getAccountQInfo(@PathParam("id") String id,
                                       @PathParam("mark") int mark){
        BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
        AnswerBoxDaoImpl answerBoxDao=new AnswerBoxDaoImpl();
        BaseCRUD<QuestionBox> questionBoxBaseCRUD=new QuestionBoxDaoImpl();

        Account account=null;
        if (mark==1)
            account=accountBaseCRUD.readByKey(new Object[]{id,"accountName"});
        else if (mark==2)
            account=accountBaseCRUD.readByKey(new Object[]{id,"mailbox"});
        if (account==null)
            throw new AccountException(InfoCode.ACCOUNT_NOT_EXIST,null);

        AccountQInfo accountQInfo=new AccountQInfo();
        List<AnswerBox> answerBoxList=answerBoxDao.AccountNameReadAll(new Object[]{account.getAccountName()});
        int sum=0;
        List<AnswerBox> t=new ArrayList<>();
        List<QuestionBox> q=new ArrayList<>();
        for (int j=0;j<answerBoxList.size();j++){
            QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{answerBoxList.get(j).getQuestionId()});
            q.add(questionBox);
            if (answerBoxList.get(j).getStatus()==2) {
                sum++;
                t.add(answerBoxList.get(j));
            }
        }
        accountQInfo.setQuestionAnsNum(sum);
        accountQInfo.setAnswerBoxesY(t);

        accountQInfo.setBeQuestionedNum(q.size());
        accountQInfo.setQuestionBoxesG(q);

        List<QuestionBox> questionBoxList=questionBoxBaseCRUD.readAll(new Object[]{account.getAccountName()});
        accountQInfo.setQuestionNum(questionBoxList.size());
        accountQInfo.setQuestionBoxes(questionBoxList);

        return ResponseMes.success(accountQInfo);
    }

    /**
     * 解密请求中的密文
     *
     * @param requestMes 请求信息
     * @return Account对象
     */
    @DenyAll//不允许任何人访问
    private Account getAccount(RequestMes requestMes,boolean flag){
        Account account=JSON.parseObject(requestMes.getData().toString(),Account.class);
        String key=requestMes.getKey();

        if (key==null||key=="")
            throw new AuthorityException(InfoCode.KEY_EMPTY,null);

        PrivateKey privateKey=RSAUtil.string2PrivateKey(RSAUtil.privateKeyStr);
        //加密后的内容Base64解码
        byte[] base642Byte=RSAUtil.base642Byte(key);
        //用私钥解密
        byte[] privateDecrypt=RSAUtil.privateDecrypt(base642Byte, privateKey);
        String AESKey=new String(privateDecrypt);//将秘钥RSA解密得到AES秘钥

        String password=account.getPassword();
        if (password==null||password=="")
            throw new AccountException(InfoCode.PASSWORD_EMPTY,null);
        password=AESUtil.aesDecrypt(password,AESKey);//运用AES秘钥解密用户密码
        account.setPassword(password);

        if (flag){
            String password1=account.getAccountName();
            if (password1==null||password1=="")
                throw new AccountException(InfoCode.OLD_PASSWORD_EMPTY,null);
            password1=AESUtil.aesDecrypt(password1,AESKey);//运用AES秘钥解密用户密码
            account.setAccountName(password1);
        }
        return account;
    }
}
