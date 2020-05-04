package com.api.model;

import lombok.*;

import java.util.Date;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBox {
    private int questionId;//提问id
    private String accountName;//提问用户
    private String questionContent;//提问内容
    private Date questionTime;//提问时间
}
