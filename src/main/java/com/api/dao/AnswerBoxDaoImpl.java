package com.api.dao;

import com.api.model.AnswerBox;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
public class AnswerBoxDaoImpl implements BaseCRUD<AnswerBox> {
    @Override
    public int create(AnswerBox dataClass) {
        String sql="insert into AnswerBox(questionId,accountName,answerContent,answerTime) values(?,?,?,?)";
        Object[] paramsValue={dataClass.getQuestionId(),
                dataClass.getAccountName(),
                dataClass.getAnswerContent(),
                dataClass.getAnswerTime()};
        baseDao.update(sql,paramsValue);
        return -1;
    }

    @Override
    public void delete(Object[] key) {

    }

    @Override
    public void deleteAll(Object[] key) {

    }

    @Override
    public void update(String[] propertyName, Object[] value) {
        String head="update AnswerBox set ";
        String tail=" where questionId=? and accountName=?";
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
    public List<AnswerBox> readAll(Object[] key) {
        String sql="select * from AnswerBox where questionId=?";
        List<AnswerBox> answerBoxList=baseDao.query(sql,key,AnswerBox.class);
        return answerBoxList;
    }

    public List<AnswerBox> AccountNameReadAll(Object[] key) {
        String sql="select * from AnswerBox where accountName=?";
        List<AnswerBox> answerBoxList=baseDao.query(sql,key,AnswerBox.class);
        return answerBoxList;
    }

    @Override
    public AnswerBox readByKey(Object[] key) {
        String sql="select * from AnswerBox where questionId=? and accountName=?";
        List<AnswerBox> answerBoxList=baseDao.query(sql,key,AnswerBox.class);
        return  (answerBoxList!=null&&answerBoxList.size()>0)?answerBoxList.get(0):null;
    }

    @Override
    public List<AnswerBox> readRand(int num) {
        return null;
    }
}
