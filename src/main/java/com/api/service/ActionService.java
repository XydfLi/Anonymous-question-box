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
public interface ActionService {
    BaseCRUD<Action> actionBaseCRUD=new ActionDaoImpl();
    BaseCRUD<QuestionBox> questionBoxBaseCRUD=new QuestionBoxDaoImpl();
    BaseCRUD<Account> accountBaseCRUD=new AccountDaoImpl();
    BaseCRUD<AnswerBox> answerBoxBaseCRUD=new AnswerBoxDaoImpl();

    //拉黑操作的合法性
    public void blackAccountLegal(int questionId,String accountName);
    //拉黑操作
    public void blackAccount(Action action);
    //举报用户的合法性
    public void tipOffAccountLegal(int questionId);
    //举报用户
    public void tipOffAccount(Action action);
    //解除拉黑操作的合法性
    public void NotBlackAccountLegal(int questionId,String accountName);
    //解除拉黑操作
    public void NotBlackAccount(int questionId,String accountName);
    //获得拉黑名单
    public List<QuestionBox> getBlackList(String accountName);
    //获得未处理举报名单
    public List<QuestionBox> getTipOffList();
    //封禁用户处理的合法性
    public void banAccountLegal(int questionId,String reason,int success);
    //封禁用户处理
    public void banAccount(int questionId,int success,String reason);
    //解除封禁的合法性
    public String removeBanLegal(String id,int ma);
    //解除封禁
    public void removeBan(String accountName);
}
