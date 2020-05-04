package com.api.email;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class NoCommonIP {

    public static void sendEmail(String[] tos,String ip){
        String title="匿名提问箱_预警";
        String context="尊敬的用户:\n" +
                "\b您刚刚在以下IP登入了账号：\n" +
                ip+"\n" +
                "如果不是您本人的操作,请尽快登入修改密码等私密信息!";
        BaseSend.send(context,tos,null,null,title);
    }
}
