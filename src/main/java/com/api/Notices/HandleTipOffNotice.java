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
public class HandleTipOffNotice {
    private static BaseCRUD<Notice> noticeBaseCRUD=new NoticeDaoImpl();

    public static void createNotice(String accountName,int questionId,int success){
        Notice notice=new Notice();
        notice.setStatus(1);
        notice.setAccountName(accountName);
        String content="系统消息：\n" +
                "您对提问id为："+questionId+"的用户发起的举报已得到处理，结果如下：\n";
        if (success!=0)
            content=content+"举报成功，该用户已被封禁。";
        else
            content=content+"举报失败。";
        notice.setContent(content);
        notice.setReleaseTime(new Date());
        noticeBaseCRUD.create(notice);
    }
}
