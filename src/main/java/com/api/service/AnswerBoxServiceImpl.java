package com.api.service;

import com.api.Notices.GetAnswer;
import com.api.exception.AccountException;
import com.api.exception.AnswerBoxException;
import com.api.exception.InfoCode;
import com.api.exception.QuestionBoxException;
import com.api.model.Account;
import com.api.model.Action;
import com.api.model.AnswerBox;
import com.api.model.QuestionBox;
import com.api.modelLegal.AnswerBoxLegal;

import java.util.Date;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
public class AnswerBoxServiceImpl implements AnswerBoxService {


    @Override
    public void createAnswerLegal(String answerAccount,int questionId,int status,String accountName) {
        if (status!=0){
            if (answerAccount==null||answerAccount=="")
                throw new AccountException(InfoCode.ACCOUNTNAME_EMPTY,null);
            Account account=accountBaseCRUD.readByKey(new Object[]{answerAccount,"accountName"});

            if (account==null)
                throw new AccountException(InfoCode.ACCOUNT_NOT_EXIST,null);
            QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
            if ((questionBox==null)||((questionBox!=null)&&(!questionBox.getAccountName().equals(accountName))))
                throw new QuestionBoxException(InfoCode.YOUR_QUESTION_EMPTY,null);

            if (!isLegal(accountName,answerAccount))
                throw new AnswerBoxException(InfoCode.ANSWER_ILLEGAL,null);
        }
    }

    @Override
    public void createAnswer(AnswerBox answerBox,int status,String accountName) {
        if (status!=0){
            AnswerBox answerBox1=answerBoxBaseCRUD.readByKey(new Object[]{answerBox.getQuestionId(),accountName});
            if (answerBox1==null)
                answerBoxBaseCRUD.create(answerBox);
        } else {
            List<Account> accountList=accountBaseCRUD.readRand(20);
            String answerAccount;
            if (accountList!=null){
                int len=accountList.size();
                for (int i=0;i<len;i++){
                    answerAccount=accountList.get(i).getAccountName();
                    if (isLegal(accountName,answerAccount)){
                        answerBox.setAccountName(answerAccount);
                        AnswerBox answerBox1=answerBoxBaseCRUD.readByKey(new Object[]{answerBox.getQuestionId(),answerAccount});
                        if (answerBox1==null)
                            answerBoxBaseCRUD.create(answerBox);
                    }
                }
            }
        }
    }

    @Override
    public void answerQuestionLegal(String content, int questionId, String accountName) {
        if (content==null||content=="")
            throw new AnswerBoxException(InfoCode.ANSWER_CONTENT_EMPTY,null);
        if (!AnswerBoxLegal.answerContentLegal(content))
            throw new AnswerBoxException(InfoCode.ANSWER_CONTENT_ERROR,null);
        AnswerBox answerBox=answerBoxBaseCRUD.readByKey(new Object[]{questionId,accountName});
        if (answerBox==null)
            throw new AnswerBoxException(InfoCode.ANSWER_YOU_NOT_EXIST,null);
    }

    @Override
    public void answerQuestion(String content, int questionId, String accountName) {
        answerBoxBaseCRUD.update(new String[]{"answerContent","answerTime","status"},
                new Object[]{content,new Date(),2,questionId,accountName});
        QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
        GetAnswer.createNotice(questionBox.getAccountName(),questionId);
    }

    @Override
    public void updateDeleteLegal(int questionId, String accountName) {
        AnswerBox answerBox=answerBoxBaseCRUD.readByKey(new Object[]{questionId,accountName});
        if (answerBox==null)
            throw new AnswerBoxException(InfoCode.ANSWER_EMPTY, null);
    }

    @Override
    public void updateDelete(int deleted, int questionId, String accountName) {
        answerBoxBaseCRUD.update(new String[]{"deleted"},new Object[]{deleted,questionId,accountName});
    }

    @Override
    public void readAnswerLegal(int questionId, String accountName) {
        QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
        if ((questionBox==null)||(!questionBox.getAccountName().equals(accountName)))
            throw new QuestionBoxException(InfoCode.YOUR_QUESTION_EMPTY,null);
    }

    @Override
    public List<AnswerBox> readAnswer(int questionId) {
        return answerBoxBaseCRUD.readAll(new Object[]{questionId});
    }

    private boolean isLegal(String accountName,String answerAccount){
        if (accountName.equals(answerAccount))
            return false;

        Account account2=accountBaseCRUD.readByKey(new Object[]{answerAccount,"accountName"});

        if (!account2.isQuestionBoxStatus())
            return false;

        List<Action> actionList=actionBaseCRUD.readAll(new Object[]{"accountName",answerAccount});
        if (actionList!=null){
            int len=actionList.size();
            Action action;
            for (int i=0;i<len;i++){
                action=actionList.get(i);
                if (action.isBlacklist()){
                    QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{action.getQuestionId()});
                    if (questionBox.getAccountName().equals(accountName))
                        return false;
                }
            }
        }

        return true;
    }
}
