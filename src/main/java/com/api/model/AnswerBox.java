package com.api.model;

import lombok.*;

import java.util.Date;

/**
 * 数据库中的AnswerBox表
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerBox {
    private int questionId;//提问id
    private String accountName;//回答用户
    private String answerContent;//回答内容
    private Date answerTime;//回答时间
    private int status;//1为未回答，2为已回答
    private int deleted;//1为删除，2为不删除
}
