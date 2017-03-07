package com.smart4j.chapter2.service;

import com.smart4j.chapter2.dao.DatabaseHelper;
import com.smart4j.chapter2.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * customerService测试方法
 * Created by Administrator on 2017/3/3.
 */

public class CustomerServiceTest {
    private final CustomerService customerService ;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init() throws IOException {
        String file = "sql/customer_init.sql";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String sql ;
        while((sql = bufferedReader.readLine()) != null){
            DatabaseHelper.executeUpdate(sql);
        }
    }

    @Test
    public void getCustomerListTest() throws Exception{
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertEquals(2,customerList.size());
    }

    @Test
    public void getCustomerTest() throws  Exception {
        Long id = 2L;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    @Test
    public  void createCustomerTest() throws  Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name","lizanle");
        map.put("telephone","15678900987");
        map.put("contact","lizanle");
        boolean result = customerService.createCustomer(map);
        Assert.assertTrue(result);
    }

    @Test
    public  void updateCustomerTest() throws  Exception {
        Long id = 2L;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("contact","eric");
        boolean result = customerService.updateCustomer(id,map);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest() throws  Exception {
        Long id = 2L;
        boolean b = customerService.deleteCustomer(id);
        Assert.assertTrue(b);
    }

}
