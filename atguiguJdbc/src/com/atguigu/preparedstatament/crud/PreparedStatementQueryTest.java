package com.atguigu.preparedstatament.crud;

import com.atguigu.bean.Customer;
import com.atguigu.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/*
    使用PreparedStatement实现针对于不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {

    @Test
    public void testGetForList(){
        String sql = "select id,name,email from customers where id < ?";
        List<Customer> list = getForList(Customer.class,sql,12);
        list.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            // 获取结果集的元数据 ： ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData 获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            // 创建集合对象
            ArrayList<T> list = new ArrayList<T>();
            while(rs.next()){
                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理一行数据中的每一个列 给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValve = rs.getObject(i+1);
                    // 获取每个列的列名
                    String columnName = rsmd.getColumnName(i+1);
                    // 给t指定的某个属性赋值为columValve 通过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,columValve);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
    }

    @Test
    public void testGetinstance() throws Exception {
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = getInstance(Customer.class,sql,12);
        System.out.println(customer);
    }
    /*
        针对于不同表的查询操作 返回表中的一条记录
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            // 获取结果集的元数据 ： ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData 获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValve = rs.getObject(i+1);
                    // 获取每个列的列名
                    String columnName = rsmd.getColumnName(i+1);
                    // 给t指定的某个属性赋值为columValve 通过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,columValve);
                }
                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
}
