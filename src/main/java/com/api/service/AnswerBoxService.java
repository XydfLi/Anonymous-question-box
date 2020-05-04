package com.api.service;

import com.api.dao.*;
import com.api.model.Account;
import com.api.model.Action;
import com.api.model.AnswerBox;
import com.api.model.QuestionBox;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
public interface AnswerBoxService {
    BaseCRUD<AnswerBox> answerBoxBaseCRUD=new AnswerBoxDaoImpl();
    BaseCRUD<QuestionBox> questionBoxBaseCRUD=new QuestionBoxDaoImpl();
    BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
    BaseCRUD<Action> actionBaseCRUD=new ActionDaoImpl();

    //判断增加回答的合法性
    public void createAnswerLegal(String answerAccount,int questionId,int status,String accountName);
    //增加回答
    public void createAnswer(AnswerBox answerBox,int status,String accountName);
    //回答问题的合法性
    public void answerQuestionLegal(String content,int questionId,String accountName);
    //回答问题
    public void answerQuestion(String content,int questionId,String accountName);
    //修改删除标记合法性
    public void updateDeleteLegal(int questionId,String accountName);
    //修改删除标记
    public void updateDelete(int deleted,int questionId,String accountName);
    //读取回答的合法性
    public void readAnswerLegal(int questionId,String accountName);
    //读取回答
    public List<AnswerBox> readAnswer(int questionId);
}
