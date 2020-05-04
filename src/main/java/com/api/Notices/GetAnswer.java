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
public class GetAnswer {
    private static BaseCRUD<Notice> noticeBaseCRUD=new NoticeDaoImpl();

    public static void createNotice(String accountName,int questionId){
        Notice notice=new Notice();
        notice.setStatus(1);
        notice.setAccountName(accountName);
        notice.setContent("系统消息：\n" +
                "您发布的提问ID为:"+questionId+"的提问得到了回复。\n" +
                "请查看。");
        notice.setReleaseTime(new Date());
        noticeBaseCRUD.create(notice);
    }
}
