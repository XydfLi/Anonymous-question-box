package com.api.service;

import com.api.exception.AccountException;
import com.api.exception.InfoCode;
import com.api.exception.NoticeException;
import com.api.model.Account;
import com.api.model.Notice;
import com.api.modelLegal.NoticeLegal;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class NoticeServiceImpl implements NoticeService {

    @Override
    public void createNoticeLegal(Notice notice) {
        String content=notice.getContent();
        if (content==null||content=="")
            throw new NoticeException(InfoCode.NOTICE_EMPTY,null);
        if (!NoticeLegal.contentLegal(notice.getContent()))
            throw new NoticeException(InfoCode.NOTICE_CONTENT_ERROR,null);

        if (notice.getStatus()!=4){
            String accountName=notice.getAccountName();
            if (accountName==null||accountName=="")
                throw new AccountException(InfoCode.ACCOUNTNAME_EMPTY,null);
            Account account1=accountBaseCRUD.readByKey(new Object[]{accountName,"accountName"});
            if (account1==null)
                throw new AccountException(InfoCode.ACCOUNT_NOT_EXIST,null);
        }
    }

    @Override
    public void createNotice(Notice notice) {
        if (notice.getStatus()==4){
            List<Account> accountList=accountBaseCRUD.readAll(null);
            int len=0;
            if (accountList!=null)
                len=accountList.size();
            notice.setStatus(1);
            for (int i=0;i<len;i++){
                notice.setAccountName(accountList.get(i).getAccountName());
                noticeBaseCRUD.create(notice);
            }
        } else {
            notice.setStatus(1);
            noticeBaseCRUD.create(notice);
        }
    }

    @Override
    public void updateStatusLegal(int noticeId,int status,String accountName) {
        Notice notice=noticeBaseCRUD.readByKey(new Object[]{noticeId});
        if (notice==null)
            throw new NoticeException(InfoCode.NOTICE_NOT_EXIST,null);
        if (status<2||status>3)
            throw new NoticeException(InfoCode.NOTICE_STATUS_ERROR,null);
        if (!accountName.equals(notice.getAccountName()))
            throw new NoticeException(InfoCode.NOTICE_NOT_EXIST,null);
    }

    @Override
    public void updateStatus(int noticeId,int status) {
        noticeBaseCRUD.update(null,new Object[]{status,noticeId});
    }

    @Override
    public List<Notice> getNotices(String accountName) {
        return noticeBaseCRUD.readAll(new Object[]{accountName});
    }
}
