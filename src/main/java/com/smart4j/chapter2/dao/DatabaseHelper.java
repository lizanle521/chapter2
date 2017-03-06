package com.smart4j.chapter2.dao;

import com.google.common.base.CaseFormat;
import com.smart4j.chapter2.model.Customer;
import com.smart4j.chapter2.util.PropsUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

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

    private static final ThreadLocal<Connection> CONNECTION_HOLDER ;
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
    private static final QueryRunner   QUERY_RUNNER ;

    private static  final BasicDataSource DATA_SOURCE;

    static {
        Properties properties = PropsUtil.loadProperties("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");
        QUERY_RUNNER = new QueryRunner();
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }



    public static Connection getConnection(){
        Connection connection =  CONNECTION_HOLDER.get();
        if(connection == null) {
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                logger.error("get connection failure",e);
                throw  new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
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
        }
        return rows;
    }

    /**
     * 插入实体
     * @param entityClass 实体类
     * @param fieldMap 属性类
     * @param <T>
     * @return 是否插入成功的布尔值
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            logger.error("field Map empty is not allowed");
            return false;
        }
        String sql = "insert into " + getTableName(entityClass);

        Set<Map.Entry<String, Object>> entrySet = fieldMap.entrySet();
        StringBuilder fieldBuilder = new StringBuilder(fieldMap.size()).append("(");
        StringBuilder valueBuilder = new StringBuilder(fieldMap.size()).append("(");
        for(Map.Entry<String,Object> entry : entrySet){
            fieldBuilder.append(entry.getKey()).append(",");
            valueBuilder.append("?,");
        }
        fieldBuilder.replace(fieldBuilder.lastIndexOf(","),fieldBuilder.length(),")");
        valueBuilder.replace(valueBuilder.lastIndexOf(","),valueBuilder.length(),")");
        sql += fieldBuilder + " VALUES " + valueBuilder;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params) == 1;
    }

    /**
     * 更新实体
     * @param entityClass
     * @param id 需要更新的实体的id
     * @param fieldMap 需要更新的实体的列 和 列的值
     * @param <T> 泛型
     * @return 更新是否成功的布尔值
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            logger.error("fieldMap cannot be empty");
            return false;
        }
        String sql = " update " + getTableName(entityClass) + " set ";
        Set<Map.Entry<String, Object>> entrySet = fieldMap.entrySet();
        StringBuilder setBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : entrySet) {
            setBuilder.append(entry.getKey()).append("=?,");
        }
        sql += setBuilder.substring(0,setBuilder.lastIndexOf(",")) + " where id = ?";
        List<Object> params = new ArrayList();
        params.addAll(fieldMap.values());
        params.add(id);
        return executeUpdate(sql,params.toArray()) == 1;
    }

    /**
     * 删除一条实体记录
     * @param entityClass
     * @param id
     * @param <T>
     * @return 删除成功的布尔值
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "delete from " + getTableName(entityClass) + " where id = ?";
        return executeUpdate(sql,id) == 1;
    }

    /**
     * 将驼峰方式命名的类名变成mysql表名（下划线形式）
     * @param entityClass
     * @param <T>
     * @return
     */
    private static <T> String getTableName(Class<T> entityClass) {
        String name = entityClass.getSimpleName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,name);
    }


}
