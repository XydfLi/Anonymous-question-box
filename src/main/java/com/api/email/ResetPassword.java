package com.api.email;

/**
 * 发送重置密码的连接
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
public class ResetPassword {

    public static void sendEmail(String[] tos,String uri){
        String title="匿名提问箱_重置密码";
        String context="尊敬的用户:\n" +
                "\b点击以下链接进入重置密码界面:\n" +
                uri +
                "\n" +
                "请在十分钟内完成重置密码操作\n" +
                "如果不是您本人的操作,请尽快登入修改密码等私密信息!";
        BaseSend.send(context,tos,null,null,title);
    }
}
