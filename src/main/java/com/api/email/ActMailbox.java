package com.api.email;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/18
 */
public class ActMailbox {

    public static void sendEmail(String[] tos,String uri){
        String title="匿名提问箱_激活邮箱";
        String context="尊敬的用户:\n" +
                "\b点击以下链接激活邮箱:\n" +
                uri +
                "\n" +
                "请在十分钟内完成激活邮箱操作\n" +
                "如果不是您本人的操作,请尽快登入修改密码等私密信息!";
        BaseSend.send(context,tos,null,null,title);
    }
}
