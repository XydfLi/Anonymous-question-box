package com.api.signUri;

import lombok.*;

import java.util.Date;

/**
 * 定义sign中携带的信息
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
public class SignUser {
    private String accountName;
    private String mailbox;
    private Date deadline;
}
