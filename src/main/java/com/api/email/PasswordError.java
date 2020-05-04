package com.api.email;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class PasswordError {

    public static void sendEmail(String[] tos){
        String title="匿名提问箱_预警";
        String context="尊敬的用户:\n" +
                "\b您已经密码连续输入错误超过5次，我们判断您的密码正在被人爆破。\n" +
                "如果您忘记密码，请通过修改密码找回。\n" +
                "如果不是您本人的操作,请尽快登入修改密码等私密信息!";
        BaseSend.send(context,tos,null,null,title);
    }
}
