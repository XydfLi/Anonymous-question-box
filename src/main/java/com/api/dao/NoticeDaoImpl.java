package com.api.dao;

import com.api.model.Notice;

import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class NoticeDaoImpl implements BaseCRUD<Notice> {
    @Override
    public int create(Notice dataClass) {
        String sql="insert into Notice(content,releaseTime,accountName) values(?,?,?)";
        Object[] paramsValue={dataClass.getContent(),
                dataClass.getReleaseTime(),
                dataClass.getAccountName()};
        return baseDao.update(sql,paramsValue);
    }

    @Override
    public void delete(Object[] key) {

    }

    @Override
    public void deleteAll(Object[] key) {

    }

    @Override
    public void update(String[] propertyName, Object[] value) {
        String sql="update Notice set status=? where noticeId=?";
        baseDao.update(sql,value);
    }

    @Override
    public List<Notice> readAll(Object[] key) {
        String sql="select * from Notice where accountName=?";
        List<Notice> noticeList=baseDao.query(sql,key,Notice.class);
        return noticeList;
    }

    @Override
    public Notice readByKey(Object[] key) {
        String sql="select * from Notice where noticeId=?";
        List<Notice> noticeList=baseDao.query(sql,key,Notice.class);
        return  (noticeList!=null&&noticeList.size()>0)?noticeList.get(0):null;
    }

    @Override
    public List<Notice> readRand(int num) {
        return null;
    }
}
