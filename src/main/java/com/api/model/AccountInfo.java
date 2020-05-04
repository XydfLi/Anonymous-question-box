package com.api.model;

import lombok.*;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/05/01
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    private String accountName;//用户名
    private int identity;//身份
    private int beQuestionedNum;//收到提问的个数
    private int questionNum;//发起提问的个数
    private int questionAnsNum;//回复提问的个数
}
