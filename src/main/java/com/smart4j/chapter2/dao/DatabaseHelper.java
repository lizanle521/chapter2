package com.smart4j.chapter2.dao;

import com.smart4j.chapter2.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 数据库连接工具
 * Created by Administrator on 2017/3/5.
 */
public final class DatabaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties properties = PropsUtil.loadProperties("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("load driver failure ",e);
        }
    }

    private static final QueryRunner   QUERY_RUNNER = new QueryRunner();

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            logger.error("get connection failure",e);
        }
        return connection;
    }

    public static  void closeConnection(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("cannot close connection",e);
            }
        }
    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> getEntityList(Class<T> entityClass,String sql,Object ...params){
        List<T> entityList = null;
        Connection connection = getConnection();
        try {
            entityList = QUERY_RUNNER.query(connection,sql,new BeanListHandler<T>(entityClass),params);
            return entityList;
        } catch (SQLException e) {
            logger.error("query entity failure",e);
        } finally {
            closeConnection(connection);
        }
        return entityList;
    }
}
