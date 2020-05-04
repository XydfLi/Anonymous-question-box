package com.api.service;

import com.api.dao.AccountDaoImpl;
import com.api.dao.BaseCRUD;
import com.api.dao.NoticeDaoImpl;
import com.api.model.Account;
import com.api.model.Notice;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public interface NoticeService {
    BaseCRUD<Notice> noticeBaseCRUD=new NoticeDaoImpl();
    BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();

    //发布公告的合法性
    public void createNoticeLegal(Notice notice);
    //发布公告
    public void createNotice(Notice notice);
    //修改公告状态合法性
    public void updateStatusLegal(int noticeId,int status,String accountName);
    //修改公告状态
    public void updateStatus(int noticeId,int status);
    //获得公告
    public List<Notice> getNotices(String accountName);
}
