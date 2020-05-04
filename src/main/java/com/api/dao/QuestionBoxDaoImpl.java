package com.api.dao;

import com.api.model.QuestionBox;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/18
 */
public class QuestionBoxDaoImpl implements BaseCRUD<QuestionBox> {

    @Override
    public int create(QuestionBox dataClass) {
        String sql="insert into QuestionBox(accountName,questionContent,questionTime) values(?,?,?)";
        Object[] paramsValue={dataClass.getAccountName(),
                dataClass.getQuestionContent(),
                dataClass.getQuestionTime()};
        return baseDao.update(sql,paramsValue);
    }

    @Override
    public void delete(Object[] key) {
        String sql="delete from QuestionBox where questionId=?";
        Object[] paramsValue={key[0]};
        baseDao.update(sql,paramsValue);
    }

    @Override
    public void deleteAll(Object[] key) {
        String sql="delete from QuestionBox";
        baseDao.update(sql,null);
    }

    @Override
    public void update(String[] propertyName, Object[] value) {
        String head="update QuestionBox set ";
        String tail=" where questionId=?";
        String sql=head;

        int count=propertyName.length;
        //填入各个属性
        for(int i=0;i<count;i++)
        {
            if(i==0){
                sql=sql+propertyName[i];
                sql=sql+"=?";
            } else {
                sql=sql+",";
                sql=sql+propertyName[i];
                sql=sql+"=?";
            }
        }

        sql=sql+tail;
        baseDao.update(sql,value);
    }

    @Override
    public List<QuestionBox> readAll(Object[] key) {
        String sql="select * from QuestionBox where accountName=?";
        List<QuestionBox> questionBoxList=baseDao.query(sql,key,QuestionBox.class);
        return questionBoxList;
    }

    @Override
    public QuestionBox readByKey(Object[] key) {
        String sql="select * from QuestionBox where questionId=?";
        List<QuestionBox> questionBoxList=baseDao.query(sql,key,QuestionBox.class);
        return  (questionBoxList!=null&&questionBoxList.size()>0)?questionBoxList.get(0):null;
    }

    @Override
    public List<QuestionBox> readRand(int num) {
        return null;
    }
}
