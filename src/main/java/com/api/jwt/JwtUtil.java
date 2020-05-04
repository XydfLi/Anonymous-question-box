package com.api.jwt;

import com.api.controller.UserController;
import com.api.dao.AccountDaoImpl;
import com.api.dao.BaseCRUD;
import com.api.exception.AuthorityException;
import com.api.exception.InfoCode;
import com.api.model.Account;
import com.api.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Jwt工具类
 * 运用jwt防CSRF
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
public class JwtUtil {

    private static String KEY=null;

    static {
        if (KEY==null){
            try {
                Properties properties=new Properties();
                InputStream in=JwtUtil.class.getClassLoader().getResourceAsStream("config/encryption.properties");
                properties.load(in);
                KEY=properties.getProperty("JwtKey");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成jwt
     *
     * @param nowMillis 签发时间
     * @param ttlMillis 过期时间
     * @param user 私有声明
     * @return jwt字符串
     */
    public static String createJWT(long nowMillis,long ttlMillis,JwtUser user) {
        //创建payload的私有声明
        Map<String,Object> claims=new HashMap<String,Object>();
        claims.put("accountName",user.getAccountName());
        claims.put("identity",user.getIdentity());
        claims.put("mailbox",user.getMailbox());
        claims.put("loginTime",user.getLoginTime().getTime());

        JwtBuilder builder = Jwts.builder()
                //必须先设置这个自己创建的私有声明
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(new Date(nowMillis))
                //这是一个json格式的字符串，作为用户的唯一标志。
                .setSubject(user.getAccountName())
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS256,KEY);
        if (ttlMillis>=0) {
            Date exp=new Date(nowMillis+ttlMillis);
            builder.setExpiration(exp);//设置过期时间
        }
        return builder.compact();
    }

    /**
     * jwt解密
     *
     * @param token jwt字符串
     * @return 解密后的Claims对象
     */
    public static Claims parseJWT(String token) {

        return Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(KEY)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
    }

    public static void isLegal(Claims claims){
        Date now=new Date();
        BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
        if (now.after(claims.getExpiration()))
            throw new AuthorityException(InfoCode.LOGIN_EXPIRED,new Throwable("jwt过期"));
        Account account=accountBaseCRUD.readByKey(new Object[]{(String)claims.get("accountName"),"accountName"});
        if (account==null)//主要为了防止用户登入后修改用户名，从而非法实现非单一登入
            throw new AuthorityException(InfoCode.NO_LOGIN,new Throwable("账号不存在"));
        Date loginTime=new Date((Long)claims.get("loginTime"));
        Date pre=UserService.OnlineAccount.get(claims.getSubject());
        if (pre==null)
            throw new AuthorityException(InfoCode.NO_LOGIN,null);
        if ((pre!=null)&&(!pre.equals(loginTime)))
            throw new AuthorityException(InfoCode.NO_LOGIN,new Throwable("踢掉这个用户"));
    }

}
