package com.api.service;

import com.api.dao.AccountDaoImpl;
import com.api.dao.BaseCRUD;
import com.api.dao.IPsDaoImpl;
import com.api.model.Account;
import com.api.model.IPs;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对应controller层的UserController
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
public interface UserService {
    //记录在线账号，实现单一登入
    public static Map<String, Date> OnlineAccount=new HashMap<>();
    //记录用户连续密码错误的次数
    public static Map<String, Integer> passwordErrorCount=new HashMap<>();
    BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
    BaseCRUD<IPs> ipsBaseCRUD=new IPsDaoImpl();

    //判断登录操作的合法性
    public Account loginLegal(Account account,String ip);
    //执行登入操作
    public String login(Account account,String ip);
    //判断注册的合法性
    public void registerLegal(Account account);
    //执行注册操作
    public void register(Account account);
    //判断发送URI的合法性
    public void passwordUriLegal(Account account);
    //发送URI
    public void passwordUri(Account account);
    //判断修改密码的合法性
    public Account updatePasswordLegal(String sign,Account account);
    //修改密码
    public void updatePassword(Account account);
    //判断修改用户的合法性
    public void updateAccountLegal(Account account,String accountName);
    //修改用户
    public void updateAccount(Account account,String accountName);
    //修改提问箱状态
    public void updateQuestionBoxStatus(int status,String accountName);
    //修改头像
    public void updateAvatar(InputStream fileInputStream,
                             FormDataContentDisposition disposition,
                             ServletContext ctx,
                             String accountName,
                             int size);
    //发送激活邮箱的邮件
    public void emailUriLegal(Account account,String accountName);
    //激活邮箱
    public void actEmail(String sign);
}
