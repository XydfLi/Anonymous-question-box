package com.api.modelLegal;

import com.api.exception.AccountException;
import com.api.exception.InfoCode;
import com.api.model.Account;

/**
 * 用于进行Account合法性的判断
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
public class AccountLegal {

    private static final String regex1="^[0-9]*$";
    private static final String regex2="^[A-Za-z]+$";
    private static final String regex3="^[0-9a-zA-Z_]{1,}$";
    private static final String emailRegex="^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$";
    private static final String avatarRegex="\\d+\\.(jpg|png)";//jpg、png格式

    public static boolean accountNameLegal(String accountName){
        if ((accountName==null)||(accountName==""))
            return true;
        int len=accountName.length();
        if ((len>0)&&(len<20))
            return true;
        else
            return false;
    }

    public static boolean passwordLegal(String password){
        if ((password==null)||(password==""))
            return true;
        int len=password.length();
        if ((len<=0)||(len>=20))
            return false;
        if (!password.matches(regex3))
            return false;
        if (password.matches(regex1)||password.matches(regex2))
            return false;
        for (int i=0;i<password.length();i++)
            if (password.charAt(i)!='_')
                return true;
        return false;
    }

    public static boolean identityLegal(int identity){
        if ((identity>=0)&&(identity<6))
            return true;
        return false;
    }

    public static boolean mailboxLegal(String mailbox){
        if ((mailbox==null)||(mailbox==""))
            return true;
        if (emailRegex.matches(mailbox)||mailbox.matches(emailRegex))
            return true;
        else
            return false;
    }

    public static boolean avatarLegal(String avatar){
        if ((avatar==null)||(avatar==""))
            return true;
        return avatar.matches(avatarRegex);
    }

    public static void allIsLegal(Account account){
        if (!accountNameLegal(account.getAccountName()))
            throw new AccountException(InfoCode.ACCOUNTNAME_ERROR,null);
        else if (!mailboxLegal(account.getMailbox()))
            throw new AccountException(InfoCode.MAILBOX_ERROR,null);
    }

    public static void isEmpty(Account account){
        if (account.getAccountName()==null)
            throw new AccountException(InfoCode.ACCOUNTNAME_EMPTY,null);
        else if (account.getPassword()==null)
            throw new AccountException(InfoCode.PASSWORD_EMPTY,null);
    }
}
