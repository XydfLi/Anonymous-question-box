package com.api.model;

import lombok.*;

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
public class Action {
    private String accountName;//执行动作的用户
    private boolean blacklist;//是否拉黑（1为是，0为否）
    private int tipOff;//是否举报（1为举报，2为解除举报，3为管理员已受理）
    private int questionId;//提问id
}
