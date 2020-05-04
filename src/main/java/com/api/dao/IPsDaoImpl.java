package com.api.dao;

import com.api.model.IPs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class IPsDaoImpl implements BaseCRUD<IPs> {
    @Override
    public int create(IPs dataClass) {
        String sql="insert into IPs(accountName,IP,count) values(?,?,?)";
        Object[] paramsValue={dataClass.getAccountName(),
                dataClass.getIP(),
                dataClass.getCount()};
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
        String sql="update IPs set count=? where accountName=? and IP=?";
        baseDao.update(sql,value);
    }

    /**
     * 查询用户常用的前三个ip
     *
     * @param key
     * @return
     */
    @Override
    public List<IPs> readAll(Object[] key) {
        String sql="select * from IPs where accountName=? order by count desc limit 3";
        List<IPs> iPsList=baseDao.query(sql,key,IPs.class);

        int len=iPsList.size();
        if (len<=3)
            return iPsList;

        List<IPs> iPsList1=new ArrayList<>();
        for (int i=0;i<3;i++)
            iPsList1.add(iPsList.get(i));
        return iPsList1;
    }

    @Override
    public IPs readByKey(Object[] key) {
        String sql="select * from IPs where accountName=? and IP=?";
        List<IPs> accountList=baseDao.query(sql,key,IPs.class);
        return  (accountList!=null&&accountList.size()>0)?accountList.get(0):null;
    }

    @Override
    public List<IPs> readRand(int num) {
        return null;
    }
}
