package com.api.service;

import com.api.exception.InfoCode;
import com.api.exception.QuestionBoxException;
import com.api.model.QuestionBox;
import com.api.modelLegal.QuestionBoxLegal;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/18
 */
public class QuestionBoxServiceImpl implements QuestionBoxService {

    @Override
    public void createQuestionLegal(QuestionBox questionBox) {
        if (questionBox.getQuestionContent()==null||questionBox.getQuestionContent()=="")
            throw new QuestionBoxException(InfoCode.QUESTION_CONTENT_EMPTY,null);
        if (!QuestionBoxLegal.questionContentLegal(questionBox.getQuestionContent()))
            throw new QuestionBoxException(InfoCode.QUESTION_ERROR,null);
    }

    @Override
    public int createQuestion(QuestionBox questionBox) {
        return questionBoxBaseCRUD.create(questionBox);
    }

    @Override
    public void updateQuestionLegal(QuestionBox questionBox) {
        QuestionBox questionBox0=questionBoxBaseCRUD.readByKey(new Object[]{questionBox.getQuestionId()});

        if ((questionBox0==null)||(!questionBox0.getAccountName().equals(questionBox.getAccountName())))
            throw new QuestionBoxException(InfoCode.YOUR_QUESTION_EMPTY,null);
        if (!QuestionBoxLegal.questionContentLegal(questionBox.getQuestionContent()))
            throw new QuestionBoxException(InfoCode.QUESTION_ERROR,null);

        /*
        这里只能修改提问内容，故提问内容不能为空
         */
        if (questionBox.getQuestionContent()==null||questionBox.getQuestionContent()=="")
            throw new QuestionBoxException(InfoCode.QUESTION_CONTENT_EMPTY,null);
    }

    @Override
    public void updateQuestion(QuestionBox questionBox) {
        questionBoxBaseCRUD.update(new String[]{"questionContent"},
                new Object[]{questionBox.getQuestionContent(),questionBox.getQuestionId()});
    }

    @Override
    public List<QuestionBox> readQuestion(String accountName) {
        return questionBoxBaseCRUD.readAll(new Object[]{accountName});
    }

    @Override
    public void readAccountLegal(int questionId) {
        QuestionBox questionBox=questionBoxBaseCRUD.readByKey(new Object[]{questionId});
        if (questionBox==null)
            throw new QuestionBoxException(InfoCode.QUESTION_NOT_EXIST,null);
    }

    @Override
    public QuestionBox readAccount(int questionId) {
        return questionBoxBaseCRUD.readByKey(new Object[]{questionId});
    }
}
