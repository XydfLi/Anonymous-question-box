package com.api.Notices;

import com.api.dao.BaseCRUD;
import com.api.dao.NoticeDaoImpl;
import com.api.model.Notice;

import java.util.Date;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class TipOffNotice {
    private static BaseCRUD<Notice> noticeBaseCRUD=new NoticeDaoImpl();

    public static void createNotice(String accountName,int questionId){
        Notice notice=new Notice();
        notice.setStatus(1);
        notice.setAccountName(accountName);
        notice.setContent("系统消息：\n" +
                "您刚刚举报了id为:"+questionId+"的举报。\n" +
                "该举报正在接受处理。");
        notice.setReleaseTime(new Date());
        noticeBaseCRUD.create(notice);
    }
}
