package com.api.dao;

import com.api.model.Action;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/19
 */
public class ActionDaoImpl implements BaseCRUD<Action> {
    @Override
    public int create(Action dataClass) {
        String sql="insert into Action(accountName,blacklist,tipOff,questionId) values(?,?,?,?)";
        Object[] paramsValue={dataClass.getAccountName(),
                dataClass.isBlacklist(),
                dataClass.getTipOff(),
                dataClass.getQuestionId()};
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
        String head="update Action set ";
        String tail=" where accountName=? and questionId=?";
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
    public List<Action> readAll(Object[] key) {
        String sql="select * from Action where "+key[0]+"=?";
        List<Action> actionList=baseDao.query(sql,new Object[]{key[1]},Action.class);
        return actionList;
    }

    @Override
    public Action readByKey(Object[] key) {
        String sql="select * from Action where accountName=? and questionId=?";
        List<Action> actionList=baseDao.query(sql,key,Action.class);
        return  (actionList!=null&&actionList.size()>0)?actionList.get(0):null;
    }

    @Override
    public List<Action> readRand(int num) {
        return null;
    }
}
