package com.atguigu.preparedstatament.crud;

import com.atguigu.bean.Order;
import com.atguigu.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
    针对order表的通用查询操作
 */
public class OrderForQuery {

    @Test
    public void testOrderForQuery(){
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = QueryForOrder(sql,1);
        System.out.println(order);

    }

    public Order QueryForOrder(String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            // 执行 获取结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列的列值 通过结果集 ResultSet
                    Object columnValue = rs.getObject(i+1);
                    // 获取每个列的列名 通过ResultSetMetaData
                    // String columnName = rsmd.getColumnName(i+1);
                    // 获取列的别名 getColumnLabel
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    // 通过反射 将对象指定名 columnName 的属性赋值为 columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
    @Test
    public void testQuery1(){

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            rs = ps.executeQuery();
            if(rs.next()){
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date) rs.getObject(3);

                Order order = new Order(id,name,date);
                System.out.println(order);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
    }


}
