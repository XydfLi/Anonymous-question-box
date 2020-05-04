package com.api.model;

import lombok.*;

import java.util.Date;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    private int noticeId;
    private String content;
    private Date releaseTime;
    private int status;//1为未读，2为已读，3为删除，4为公告发布给所有人
    private String accountName;
}
