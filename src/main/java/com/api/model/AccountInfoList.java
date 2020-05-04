package com.api.model;

import lombok.*;

import java.util.List;

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
public class AccountInfoList {
    private int num;
    private List<AccountInfo> accountInfos;
}
