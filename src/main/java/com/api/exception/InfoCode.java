package com.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * 系统状态码infoCode的枚举
 * 进行Jersey统一异常处理
 * 增加程序的可维护性和可移植性
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
@Getter
@AllArgsConstructor
@Log
public enum InfoCode {
    OK(1000,"OK"),

    DATA_EMPTY(0,"数据为空"),
    UNKNOWN_MISTAKE(1,"未知错误"),

    NO_LOGIN(1101,"请先登录"),
    NO_PERMISSION(1102,"没有权限"),
    KEY_EMPTY(1103,"秘钥为空"),
    LOGIN_EXPIRED(1104,"登入过期"),
    SIGN_ERROR(1105,"签名错误"),
    CODE_ERROR(1106,"图形验证码错误"),

    PASSWORD_EMPTY(1201,"密码为空"),
    ACCOUNTNAME_NOT_EXIST(1202,"用户名不存在"),
    PASSWORD_ERROR(1203,"密码错误"),
    ACCOUNTNAME_EMPTY(1204,"用户名为空"),
    ACCOUNTNAME_ERROR(1205,"用户名不合法"),
    INVALID_PASSWORD(1206,"密码不合法"),
    IDENTITY_ERROR(1207,"身份不合法"),
    MAILBOX_ERROR(1208,"邮箱不合法"),
    AVATAR_ERROR(1209,"头像地址不合法"),
    ACCOUNTNAME_EXIST(1210,"用户名已存在"),
    MAILBOX_EMPTY(1211,"邮箱为空"),
    ACCOUNT_BE_BANNED(1212,"您已被封禁"),
    MAILBOX_EXIST(1213,"邮箱已存在"),
    OLD_PASSWORD_EMPTY(1214,"原密码为空"),
    ACCOUNT_NOT_EXIST(1215,"用户不存在"),

    FILE_FORMAT_ERROR(1301,"文件格式错误"),
    FILE_NOT_EXIST(1302,"文件不存在"),
    FILE_EMPTY(1303,"文件为空"),
    FILE_TOO_LARGE(1304,"文件太大"),

    QUESTION_ERROR(1401,"提问内容不合法"),
    YOUR_QUESTION_EMPTY(1402,"您没有这个提问"),
    QUESTION_CONTENT_EMPTY(1403,"提问内容为空"),
    QUESTION_NOT_EXIST(1404,"提问不存在"),

    ANSWER_CONTENT_EMPTY(1501,"回答内容为空"),
    ANSWER_CONTENT_ERROR(1502,"回答内容不合法"),
    ANSWER_ILLEGAL(1503,"您不能向该用户提问"),
    ANSWER_YOU_NOT_EXIST(1504,"您不能回答该提问"),
    ANSWER_EMPTY(1505,"回答为空"),

    BLACK_NOT_EXIST(1601,"您没有拉黑过该用户"),
    BLACK_EXIST(1602,"您已拉黑该用户"),
    REASON_EMPTY(1603,"封禁理由为空"),
    REASON_ERROR(1604,"封禁理由非法"),

    NOTICE_NOT_EXIST(1701,"公告不存在"),
    NOTICE_STATUS_ERROR(1702,"公告状态码不合法"),
    NOTICE_CONTENT_ERROR(1703,"公告内容不合法"),
    NOTICE_EMPTY(1704,"公告内容为空");
//
//
//
//
//
//    PASSWORD_EMPTY(1106,"密码为空"),
//    OLD_PASSWORD_EMPTY(1107,"原密码为空"),
//    NEW_PASSWORD_EMPTY(1108,"新密码为空"),
//    INVALID_Password(1109,"密码不合法"),
//    ACCOUNT_NAME_EMPTY(1110,"用户名称为空"),
//    ACCOUNT_NAME_ERROR(1111,"用户名称不合法"),
//
//    TASK_NAME_EMPTY(1201,"考核名称为空"),
//    TASK_NAME_ERROR(1202,"考核名称不合法"),
//    TASK_CONTENT_EMPTY(1203,"考核内容为空"),
//    TASK_CONTENT_ERROR(1204,"考核内容不合法"),
//    START_DATE_EMPTY(1205,"开始日期为空"),
//    START_DATE_ERROR(1206,"开始日期不合法"),
//    DEADLINE_EMPTY(1207,"截止日期为空"),
//    DEADLINE_ERROR(1208,"截止日期不合法"),
//    TASK_ID_ERROR(1209,"考核ID不合法"),
//    TASK_ID_NOT_EXIST(1210,"考核ID不存在"),
//
//    JOB_CONTENT_ERROR(1301,"作业内容不合法"),
//    SUBMIT_ONLY_ONCE(1302,"作业只能提交一次"),
//    SUB_TIME_EMPTY(1303,"提交日期为空"),
//    SUB_TIME_ERROR(1304,"提交日期在规定时间之外"),
//    MARK_ERROR(1305,"作业标记不合法"),
//

//
//    XSS_ATTACK(1501,"服务端受到XSS攻击"),
//    CSRF_ATTACK(1502,"客户端身份错误"),
//    ILLEGAL_CHARACTERS(1503,"存在非法字符"),
//


    private int code;
    private String value;
}
