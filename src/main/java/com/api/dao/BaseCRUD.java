package com.api.dao;

import java.util.List;

/**
 * 进行数据库CRUD操作接口
 * 数据库中的表实现其多态
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
public interface BaseCRUD<T> {

    BaseDao baseDao=new BaseDaoImpl();

    //增加数据接口
    public int create(T dataClass);
    //删除数据接口
    public void delete(Object[] key);
    //删除所有数据接口
    public void deleteAll(Object[] key);
    //修改数据接口
    public void update(String[] propertyName,Object[] value);
    //查询全部数据
    public List<T> readAll(Object[] key);
    //根据条件查询(主键)
    public T readByKey(Object[] key);
    //随机查询多条数据
    public List<T> readRand(int num);
}
