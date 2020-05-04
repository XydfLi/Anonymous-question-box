package com.api.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * Druid数据库连接池
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/20
 */
public class DruidUtil {

    private static DataSource dataSource;

    static {
        try {
            Properties properties=new Properties();
            InputStream in=DruidUtil.class.getClassLoader().getResourceAsStream("config/jdbc.properties");
            properties.load(in);
            dataSource=DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection conn,Statement state,ResultSet result) {
        try {
            if (result!=null)
                result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn!=null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (state!=null)
                        state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
