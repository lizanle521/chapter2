package com.smart4j.chapter2.dao;

import com.smart4j.chapter2.model.Customer;
import com.smart4j.chapter2.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库连接工具
 * Created by Administrator on 2017/3/5.
 */
public final class DatabaseHelper<T> {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

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

    /**
     * QUERY_RUNNER 有许多handler
     * BeanHandler 返回bean对象
     * BeanListHandler 返回List对象
     * BeanMapHandler 返回map对象
     * ArrayHandler 返回Object[]对象
     * ArrayListHandler 返回List对象
     * MapHandler 返回Map对象
     * MapListHandler 返回List对象
     * ScalarHandler 返回某列的值
     * ColumnListHandler 返回某列的值列表
     * KeyHandler 返回Map对象，但需要制定列名
     *
     * 以上handler都实现了ResultSetHandler
     */
    private static final QueryRunner   QUERY_RUNNER = new QueryRunner();

    public static Connection getConnection(){
        Connection connection =  CONNECTION_HOLDER.get();
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            } catch (SQLException e) {
                logger.error("get connection failure",e);
                throw  new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    public static  void closeConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("cannot close connection",e);
                throw  new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
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
        } catch (SQLException e) {
            logger.error("query entity failure",e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 获取单个实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T getEntity(Class<T> entityClass,String sql,Object ...params){
        T entity = null;
        Connection connection = getConnection();
        try {
            entity = QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);
        } catch (SQLException e) {
            logger.error("query entity failure",e);
            throw  new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return entity;
    }

    /**
     * 执行任何查询语句
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> executeQuery(String sql,Object ...params){
        List<Map<String,Object>> result ;
        Connection connection = getConnection();
        try {
            result = QUERY_RUNNER.query(connection,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            logger.error("query failure",e);
            throw  new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * 执行更新语句（update,delete,insert)
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql,Object ...params){
        int rows = 0;
        Connection connection = getConnection();
        try {
            rows = QUERY_RUNNER.update(connection,sql,params);
        } catch (SQLException e) {
            logger.error("update sql failure",e);
            throw  new  RuntimeException(e);
        }finally {
            closeConnection();
        }
        return rows;
    }


}
