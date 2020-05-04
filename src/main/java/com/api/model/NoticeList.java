package com.api.model;

import lombok.*;

import java.util.List;

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
public class NoticeList {
    private int num;
    private List<Notice> notices;
}
