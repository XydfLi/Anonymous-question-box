package com.api.service;

import com.api.Notices.GetBan;
import com.api.Notices.HandleTipOffNotice;
import com.api.Notices.RemoveBan;
import com.api.Notices.TipOffNotice;
import com.api.exception.AccountException;
import com.api.exception.ActionException;
import com.api.exception.InfoCode;
import com.api.exception.QuestionBoxException;
import com.api.model.Account;
import com.api.model.Action;
import com.api.model.Identity;
import com.api.model.QuestionBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
public class ActionServiceImpl implements ActionService {

    @Override
    public void blackAccountLegal(int questionId,String accountName) {
        Action action=actionBaseCRUD.readByKey(new Object[]{accountName,questionId});

        if ((action!=null)&&(action.isBlacklist()))
            throw new ActionException(InfoCode.BLACK_EXIST,null);
        QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
        if (questionBox==null)
            throw new QuestionBoxException(InfoCode.QUESTION_NOT_EXIST,null);
    }

    @Override
    public void blackAccount(Action action) {
        Action action1=actionBaseCRUD.readByKey(new Object[]{action.getAccountName(),action.getQuestionId()});
        if (action1==null)
            actionBaseCRUD.create(action);
        else
            actionBaseCRUD.update(new String[]{"blacklist"},new Object[]{true,action.getAccountName(),action.getQuestionId()});
        answerBoxBaseCRUD.update(new String[]{"deleted"},new Object[]{1,action.getQuestionId(),action.getAccountName()});
    }

    @Override
    public void tipOffAccountLegal(int questionId) {
        QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
        if (questionBox==null)
            throw new QuestionBoxException(InfoCode.QUESTION_NOT_EXIST,null);
    }

    @Override
    public void tipOffAccount(Action action) {
        Action action1=actionBaseCRUD.readByKey(new Object[]{action.getAccountName(),action.getQuestionId()});
        if (action1==null)
            actionBaseCRUD.create(action);
        else
            actionBaseCRUD.update(new String[]{"tipOff"},new Object[]{1,action.getAccountName(),action.getQuestionId()});
        answerBoxBaseCRUD.update(new String[]{"deleted"},new Object[]{1,action.getQuestionId(),action.getAccountName()});
        TipOffNotice.createNotice(action.getAccountName(),action.getQuestionId());//发布公告
    }

    @Override
    public void NotBlackAccountLegal(int questionId, String accountName) {
        Action action=actionBaseCRUD.readByKey(new Object[]{accountName,questionId});
        if (action==null)
            throw new ActionException(InfoCode.BLACK_NOT_EXIST,null);
    }

    @Override
    public void NotBlackAccount(int questionId, String accountName) {
        actionBaseCRUD.update(new String[]{"blacklist"},new Object[]{false,accountName,questionId});
    }

    @Override
    public List<QuestionBox> getBlackList(String accountName) {
        List<Action> actionList=actionBaseCRUD.readAll(new Object[]{"accountName",accountName});
        List<QuestionBox> questionBoxList=new ArrayList<>();
        QuestionBox questionBox;
        int len=actionList.size();
        for(int i=0;i<len;i++){
            questionBox=questionBoxBaseCRUD.readByKey(new Object[]{actionList.get(i).getQuestionId()});
            questionBox.setAccountName(null);
            questionBoxList.add(questionBox);
        }
        return questionBoxList;
    }

    @Override
    public List<QuestionBox> getTipOffList() {
        List<Action> actionList=actionBaseCRUD.readAll(new Object[]{"tipOff",1});
        List<QuestionBox> questionBoxList=new ArrayList<>();
        QuestionBox questionBox;
        int len=actionList.size();
        for (int i=0;i<len;i++){
            questionBox=questionBoxBaseCRUD.readByKey(new Object[]{actionList.get(i).getQuestionId()});
            questionBox.setAccountName(null);
            questionBoxList.add(questionBox);
        }
        return questionBoxList;
    }

    @Override
    public void banAccountLegal(int questionId,String reason,int success) {
        QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
        if (questionBox==null)
            throw new QuestionBoxException(InfoCode.QUESTION_NOT_EXIST,null);
        if (success!=0){
            if (reason==null||reason=="")
                throw new ActionException(InfoCode.REASON_EMPTY,null);
            int len=reason.length();
            if (len>3000)
                throw new ActionException(InfoCode.REASON_ERROR,null);
        }
    }

    @Override
    public void banAccount(int questionId,int success,String reason) {
        QuestionBox questionBox = questionBoxBaseCRUD.readByKey(new Object[]{questionId});

        if (success !=0){//封禁成功
            GetBan.createNotice(questionBox.getAccountName(),questionId,reason);
            accountBaseCRUD.update(new String[]{"identity"},new Object[]{Identity.BANNED.getCode(),questionBox.getAccountName()});
        }

        List<Action> actionList=actionBaseCRUD.readAll(new Object[]{"questionId",questionId});
        Action action;
        if (actionList!=null){
            int len=actionList.size();
            for (int i=0;i<len;i++){
                action=actionList.get(i);
                if (action.getTipOff()==1){
                    actionBaseCRUD.update(new String[]{"tipOff"},new Object[]{3,action.getAccountName(),action.getQuestionId()});
                    HandleTipOffNotice.createNotice(action.getAccountName(),questionId,success);
                }
            }
        }
    }

    @Override
    public String removeBanLegal(String id,int ma) {
        Account account1=null;
        if (ma==1){
            account1=accountBaseCRUD.readByKey(new Object[]{id,"accountName"});
        } else if (ma==2){
            account1=accountBaseCRUD.readByKey(new Object[]{id,"mailbox"});
        }

        if (account1==null)
            throw new AccountException(InfoCode.ACCOUNT_NOT_EXIST,null);

        return account1.getAccountName();
    }

    @Override
    public void removeBan(String accountName) {
        accountBaseCRUD.update(new String[]{"identity"},new Object[]{Identity.ORDINARY.getCode(),accountName});
        RemoveBan.createNotice(accountName);
    }
}
