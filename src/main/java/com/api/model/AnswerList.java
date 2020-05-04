package com.api.model;

import lombok.*;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerList {
    private int num;
    private List<AnswerBox> answers;
}
