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
public class AccountQInfo {
    private int beQuestionedNum;//收到提问的个数
    private List<QuestionBox> questionBoxesG;//收到的提问
    private int questionAnsNum;//回复提问的个数
    private List<AnswerBox> answerBoxesY;//回复的提问
    private int questionNum;//发起提问的个数
    private List<QuestionBox> questionBoxes;//发起的提问
}
