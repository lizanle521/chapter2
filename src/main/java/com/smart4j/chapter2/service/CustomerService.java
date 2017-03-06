package com.smart4j.chapter2.service;

import com.google.common.collect.Lists;
import com.smart4j.chapter2.dao.DatabaseHelper;
import com.smart4j.chapter2.model.Customer;
import com.smart4j.chapter2.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 提供客户数据服务
 * Created by Administrator on 2017/3/3.
 */
public class CustomerService {

    /**
     * 获取客户列表
     * @param keyWord
     * @return
     */
    public List<Customer> getCustomerList(){
        String sql = "select * from customer";
        return  DatabaseHelper.getEntityList(Customer.class,sql);
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public Customer getCustomer(Long id){
        return DatabaseHelper.getEntity(Customer.class,"select * from customer where id = ? ",id);
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class,fieldMap);
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(Long id,Map<String,Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class,id,fieldMap);
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    public boolean deleteCustomer(Long id){
        return DatabaseHelper.deleteEntity(Customer.class,id);
    }
}
