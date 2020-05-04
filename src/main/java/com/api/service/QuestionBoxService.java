package com.api.service;

import com.api.dao.BaseCRUD;
import com.api.dao.QuestionBoxDaoImpl;
import com.api.model.QuestionBox;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/18
 */
public interface QuestionBoxService {
    BaseCRUD<QuestionBox> questionBoxBaseCRUD=new QuestionBoxDaoImpl();

    //判断增加提问的合法性
    public void createQuestionLegal(QuestionBox questionBox);
    //增加提问
    public int createQuestion(QuestionBox questionBox);
    //判断修改提问的合法性
    public void updateQuestionLegal(QuestionBox questionBox);
    //修改提问
    public void updateQuestion(QuestionBox questionBox);
    //查看提问
    public List<QuestionBox> readQuestion(String accountName);
    //查看提问人合法性
    public void readAccountLegal(int questionId);
    //查看提问人
    public QuestionBox readAccount(int questionId);
}
