package com.api.service;

import com.api.IP.IPUtil;
import com.api.Notices.ActiveAccount;
import com.api.email.ActMailbox;
import com.api.email.NoCommonIP;
import com.api.email.PasswordError;
import com.api.email.ResetPassword;
import com.api.encryption.MD5;
import com.api.exception.AccountException;
import com.api.exception.AuthorityException;
import com.api.exception.FileException;
import com.api.exception.InfoCode;
import com.api.file.MyFileUtil;
import com.api.jwt.JwtUser;
import com.api.jwt.JwtUtil;
import com.api.model.Account;
import com.api.model.IPs;
import com.api.model.Identity;
import com.api.modelLegal.AccountLegal;
import com.api.signUri.GetSignUri;
import com.api.signUri.SignUser;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 实现UserService接口
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
@Log
public class UserServiceImpl implements UserService {

    private final long fileMaxSize=2*1024*1024;//图片大小在2MB以下

    @Override
    public Account loginLegal(Account account,String ip) {
        Account account1=null;
        if (account.getAccountName()!=null){
            account1=accountBaseCRUD.readByKey(new Object[]{account.getAccountName(),"accountName"});
        }

        else if (account.getMailbox()!=null)
            account1=accountBaseCRUD.readByKey(new Object[]{account.getMailbox(),"mailbox"});
        else
            throw new AccountException(InfoCode.ACCOUNTNAME_EMPTY,null);

        if (account1==null){
            throw new AccountException(InfoCode.ACCOUNT_NOT_EXIST,null);
        }

        if (!account1.getPassword().equals(account.getPassword())){
            if (!UserService.passwordErrorCount.containsKey(account1.getAccountName())){
                UserService.passwordErrorCount.put(account1.getAccountName(),1);
            } else {
                int count=UserService.passwordErrorCount.get(account1.getAccountName());
                if ((++count)>5)//密码错误次数太多，向用户发邮件预警
                    PasswordError.sendEmail(new String[]{account1.getMailbox()});
                UserService.passwordErrorCount.put(account1.getAccountName(),count);
                throw new AccountException(InfoCode.PASSWORD_ERROR,null);
            }
        }

        //异常登录
        if (!IPUtil.isCommonIP(ip,ipsBaseCRUD.readAll(new Object[]{account1.getAccountName()})))
            NoCommonIP.sendEmail(new String[]{account1.getMailbox()},ip);

        return account1;
    }

    @Override
    public String login(Account account,String ip) {

        IPs iPs=ipsBaseCRUD.readByKey(new Object[]{account.getAccountName(),ip});
        if (iPs==null)
            ipsBaseCRUD.create(new IPs(account.getAccountName(),ip,1));
        else
            ipsBaseCRUD.update(null,new Object[]{iPs.getCount()+1,account.getAccountName(),ip});

        long nowMillion=System.currentTimeMillis();
        Date now=new Date(nowMillion);
        UserService.OnlineAccount.put(account.getAccountName(),now);//踢掉之前登陆的用户
        JwtUser jwtUser=new JwtUser(account.getAccountName(),
                Identity.getIdentity(account.getIdentity()).getValue(),
                account.getMailbox(),
                now);
        return JwtUtil.createJWT(nowMillion,3*60*60*1000,jwtUser);
    }

    @Override
    public void registerLegal(Account account) {
        if ((account.getMailbox()==null)||(account.getMailbox()==""))
            throw new AccountException(InfoCode.MAILBOX_EMPTY,null);

        AccountLegal.isEmpty(account);
        AccountLegal.allIsLegal(account);

        Account account1=accountBaseCRUD.readByKey(new Object[]{account.getAccountName(),"accountName"});
        if (account1!=null)
            throw new AccountException(InfoCode.ACCOUNTNAME_EXIST,null);
        account1=accountBaseCRUD.readByKey(new Object[]{account.getMailbox(),"mailbox"});
        if (account1!=null)
            throw new AccountException(InfoCode.MAILBOX_EXIST,null);
    }

    @Override
    public void register(Account account) {
        accountBaseCRUD.create(account);
    }

    @Override
    public void passwordUriLegal(Account account) {
        String mailbox=account.getMailbox();
        if ((mailbox==null)||(mailbox==""))
            throw new AccountException(InfoCode.MAILBOX_EMPTY,null);
        Account account1=accountBaseCRUD.readByKey(new Object[]{mailbox,"mailbox"});
        if (account1==null)
            throw new AccountException(InfoCode.ACCOUNTNAME_NOT_EXIST,null);
        if(!account1.getAccountName().equals(account.getAccountName()))
            throw new AccountException(InfoCode.MAILBOX_ERROR,null);
    }

    @Override
    public void passwordUri(Account account) {
        Account account1=accountBaseCRUD.readByKey(new Object[]{account.getMailbox(),"mailbox"});
        long now=System.currentTimeMillis();
        Date deadline=new Date(now+10*60*1000);

        //TODO : 修改为前端的路径
        String uri="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/views/submitPassword.html?sign="+GetSignUri.createUri(new SignUser(account1.getAccountName(),account1.getMailbox(),deadline));
        ResetPassword.sendEmail(new String[]{account.getMailbox()},uri);

        System.out.println(GetSignUri.createUri(new SignUser(account1.getAccountName(),account1.getMailbox(),deadline)));
    }

    /**
     * 判断修改密码的合法性
     * 其中account.getAccountName()是用户原密码
     *
     * @param sign
     * @param account
     * @return
     */
    @Override
    public Account updatePasswordLegal(String sign, Account account) {
        SignUser signUser=GetSignUri.isLegal(sign);
        String mailbox=signUser.getMailbox();
        if (mailbox==null||mailbox=="")
            throw new AuthorityException(InfoCode.SIGN_ERROR,null);

        Account account1=accountBaseCRUD.readByKey(new Object[]{mailbox,"mailbox"});
        if (account1==null)
            throw new AccountException(InfoCode.ACCOUNTNAME_NOT_EXIST,null);
        if (!account1.getPassword().equals(MD5.md5Password(account.getAccountName())))
            throw new AccountException(InfoCode.PASSWORD_ERROR,null);
        if (!AccountLegal.passwordLegal(account.getPassword()))
            throw new AccountException(InfoCode.INVALID_PASSWORD,null);
        account1.setPassword(account.getPassword());
        return account1;
    }

    @Override
    public void updatePassword(Account account) {
        String password=MD5.md5Password(account.getPassword());
        accountBaseCRUD.update(new String[]{"password"},new Object[]{password,account.getAccountName()});
        OnlineAccount.put(account.getAccountName(),null);//将之前登陆的用户都踢下线
    }

    @Override
    public void updateAccountLegal(Account account,String accountName) {
        Account account0=accountBaseCRUD.readByKey(new Object[]{accountName,"accountName"});
        String aN;
        Account account1;

        AccountLegal.allIsLegal(account);
        aN=account.getAccountName();
        if ((aN!=null)&&(aN!="")){
            account1=accountBaseCRUD.readByKey(new Object[]{aN,"accountName"});
            if (account1!=null)
                throw new AccountException(InfoCode.ACCOUNTNAME_EXIST,null);
        }

        String em=account.getMailbox();
        if ((em!=null)&&(em!="")){
            account1=accountBaseCRUD.readByKey(new Object[]{em,"mailbox"});
            if (account1!=null)
                throw new AccountException(InfoCode.MAILBOX_EXIST,null);

            if (account.getPassword()==null)
                throw new AccountException(InfoCode.PASSWORD_EMPTY,null);

            if (!account0.getPassword().equals(MD5.md5Password(account.getPassword())))
                throw new AccountException(InfoCode.PASSWORD_ERROR,null);
        }
    }

    @Override
    public void updateAccount(Account account,String accountName) {
        if (account.getAccountName()!=null)
            accountBaseCRUD.update(new String[]{"accountName"},new Object[]{account.getAccountName(),accountName});
        if (account.getMailbox()!=null){
            accountBaseCRUD.update(new String[]{"mailbox"},new Object[]{account.getMailbox(),accountName});
            //需要重新激活邮箱
            accountBaseCRUD.update(new String[]{"identity"},new Object[]{Identity.UNACTIVATED.getCode(),accountName});
        }

    }

    @Override
    public void updateQuestionBoxStatus(int status, String accountName) {
        if (status==1)
            accountBaseCRUD.update(new String[]{"questionBoxStatus"},new Object[]{1,accountName});
        else if (status==0)
            accountBaseCRUD.update(new String[]{"questionBoxStatus"},new Object[]{0,accountName});
    }

    @Override
    public void updateAvatar(InputStream fileInputStream,
                             FormDataContentDisposition disposition,
                             ServletContext ctx,
                             String accountName,
                             int size) {
        Account account=accountBaseCRUD.readByKey(new Object[]{accountName,"accountName"});

        if (fileInputStream==null||disposition==null)
            throw new FileException(InfoCode.FILE_EMPTY,null);

        //判断图片是否过大
        if (size>fileMaxSize)
            throw new FileException(InfoCode.FILE_TOO_LARGE,null);

        File file=new File(ctx.getRealPath("/WEB-INF/Avatars"));
        if (!file.exists())//如果文件夹不存在则创造一个
            file.mkdir();

        String ext=FilenameUtils.getExtension(disposition.getFileName());
        if ((!ext.toLowerCase().equals("jpg"))&&(!ext.toLowerCase().equals("png")))//只允许上传JPG、png格式的图片
            throw new FileException(InfoCode.FILE_FORMAT_ERROR,null);

        String fileName;
        if (account.getAvatar().equals("1.jpg")){
            fileName=UUID.randomUUID().toString()+"."+ext;//产生一个不重复的文件名
        } else {
            fileName=account.getAvatar();
            fileName=fileName.substring(0,fileName.length()-4)+"."+ext;
        }

        File upload=new File(ctx.getRealPath("/WEB-INF/Avatars"),fileName);
        String path=ctx.getRealPath("/WEB-INF/Avatars")+"\\"+fileName;
        try {
            FileUtils.copyInputStreamToFile(fileInputStream,upload);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!MyFileUtil.PictureIsLegal(upload)){
            upload.delete();
            throw new FileException(InfoCode.FILE_FORMAT_ERROR,null);
        }

        //清洗图片，防止图片中有非法代码、病毒等
        MyFileUtil.waterMarkByText("avatar",path,path,3,100,100,0F);

        accountBaseCRUD.update(new String[]{"avatar"},new Object[]{fileName,accountName});
    }

    @Override
    public void emailUriLegal(Account account,String accountName) {
        String mailbox=account.getMailbox();
        if ((mailbox==null)||(mailbox==""))
            throw new AccountException(InfoCode.MAILBOX_EMPTY,null);
        if (AccountLegal.mailboxLegal(account.getMailbox()))
            throw new AccountException(InfoCode.MAILBOX_ERROR,null);
        Account account1=accountBaseCRUD.readByKey(new Object[]{mailbox,"mailbox"});
        if ((account1!=null)&&(!account1.getAccountName().equals(accountName)))
            throw new AccountException(InfoCode.ACCOUNTNAME_EXIST,null);

//        Account account1=accountBaseCRUD.readByKey(new Object[]{accountName,"accountName"});
//        String mailbox=account1.getMailbox();

        long now=System.currentTimeMillis();
        Date deadline=new Date(now+10*60*1000);
        //TODO : 修改为前端的路径
        String uri="http://39.101.199.3:8080/myProj-1.0-SNAPSHOT/views/activeAccount.html?sign="+GetSignUri.createUri(new SignUser(accountName,mailbox,deadline));
        ActMailbox.sendEmail(new String[]{mailbox},uri);

        System.out.println(GetSignUri.createUri(new SignUser(accountName,mailbox,deadline)));
    }

    @Override
    public void actEmail(String sign) {
        SignUser signUser=GetSignUri.isLegal(sign);
        String mailbox=signUser.getMailbox();
        if (signUser==null)
            throw new AuthorityException(InfoCode.SIGN_ERROR,null);
        Account account1=accountBaseCRUD.readByKey(new Object[]{mailbox,"mailbox"});
        accountBaseCRUD.update(new String[]{"identity"},new Object[]{Identity.ORDINARY.getCode(),account1.getAccountName()});
        accountBaseCRUD.update(new String[]{"mailbox"},new Object[]{signUser.getMailbox(),account1.getAccountName()});
        ActiveAccount.createNotice(account1.getAccountName());
    }

}
