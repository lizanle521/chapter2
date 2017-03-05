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
        Connection connection = null;
        try {
            List<Customer> customerList = Lists.newArrayList();
            String sql = "select * from customer";
            connection = DatabaseHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Customer customer = new Customer();
                customer.setId(resultSet.getLong("id"));
                customer.setContact(resultSet.getString("contact"));
                customer.setName(resultSet.getString("name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setTelephone(resultSet.getString("telephone"));
                customerList.add(customer);
            }
            return  customerList;
        } catch (SQLException e) {
            logger.error("sql execure failure",e);
        }finally {
            DatabaseHelper.closeConnection(connection);
        }
        return null;
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public Customer getCustomer(Long id){
        return null;
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        return false;
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(Long id,Map<String,Object> fieldMap){
        return false;
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    public boolean deleteCustomer(Long id){
        return false;
    }
}
