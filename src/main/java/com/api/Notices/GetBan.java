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
public class GetBan {
    private static BaseCRUD<Notice> noticeBaseCRUD=new NoticeDaoImpl();

    public static void createNotice(String accountName,int questionId,String reason){
        Notice notice=new Notice();
        notice.setStatus(1);
        notice.setAccountName(accountName);
        notice.setContent("系统消息：\n" +
                "很抱歉，您发布的提问ID为:"+questionId+"的提问违反了我们的规定。理由如下：\n" +
                reason+"\n"+
                "我们决定封禁您的账号。");
        notice.setReleaseTime(new Date());
        noticeBaseCRUD.create(notice);
    }
}
