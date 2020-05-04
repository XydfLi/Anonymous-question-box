package com.api.dao;

import com.api.model.Account;

import java.util.List;

/**
 * Account的CRUD
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
public class AccountDaoImpl implements BaseCRUD<Account> {

    /**
     * 实现数据库Account表的增加数据操作
     *
     * @param dataClass Account对象
     * @return -1 无用数据
     */
    @Override
    public int create(Account dataClass) {
        String sql="insert into Account(accountName,password,identity,mailbox,avatar,questionBoxStatus) values(?,?,?,?,?,?)";
        Object[] paramsValue={dataClass.getAccountName(),
                dataClass.getPassword(),
                dataClass.getIdentity(),
                dataClass.getMailbox(),
                dataClass.getAvatar(),
                dataClass.isQuestionBoxStatus()};
        baseDao.update(sql,paramsValue);
        return -1;
    }

    /**
     * 实现数据库Account表的删除数据操作
     *
     * @param key Objective数组，仅含有用户id
     */
    @Override
    public void delete(Object[] key) {
        String sql="delete from Account where accountName=?";
        Object[] paramsValue={key[0]};
        baseDao.update(sql,paramsValue);
    }

    /**
     * 实现数据库Account表的删除数据操作（全部）
     *
     * @param key 空值null
     */
    @Override
    public void deleteAll(Object[] key) {
        String sql="delete from Account";
        baseDao.update(sql,null);
    }

    /**
     * 实现数据库Account表的更新数据操作
     *
     * @param propertyName 需要更新属性的数组，与value数组顺序一致
     * @param value 需要更新属性的值的数组，与propertyName数组顺序一致
     */
    @Override
    public void update(String[] propertyName, Object[] value) {
        String head="update Account set ";
        String tail=" where accountName=?";
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

    /**
     * 实现数据库Account表的读取数据操作（全部）
     *
     * @param key 空值null
     * @return Account列表
     */
    @Override
    public List<Account> readAll(Object[] key) {
        String sql="select * from Account";
        List<Account> accountList=baseDao.query(sql,null,Account.class);
        return accountList;
    }

    /**
     * 实现数据库Account表的读取数据操作
     *
     * @param key Objective数组，仅含有用户id,key[1]为查询的键
     * @return Account 对象
     */
    @Override
    public Account readByKey(Object[] key) {
        String sql="select * from Account where "+key[1]+"=?";
        List<Account> accountList=baseDao.query(sql,new Object[]{key[0]},Account.class);
        return  (accountList!=null&&accountList.size()>0)?accountList.get(0):null;
    }

    @Override
    public List<Account> readRand(int num) {
        String sql="SELECT * FROM Account WHERE accountName>=((SELECT MAX(accountName) FROM account)-(SELECT MIN(accountName) FROM account))*RAND()+" +
                "(SELECT MIN(accountName) FROM account)  LIMIT "+num;
        List<Account> accountList=baseDao.query(sql,null,Account.class);
        return accountList;
    }
}
