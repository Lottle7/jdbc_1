package com.atguigu.preparedstatament.crud;

import com.atguigu.connection.ConnectionTest;
import com.atguigu.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/*
    使用 preparedstatement来替换statement 实现对数据表的增删改查操作
 */
public class PreparedStatementUpdateTest {

    @Test
    public void testCommonUpdate(){
        String sql = "delete from customers where id = ?";
        update(sql,3);
    }
    // 通用的增删改操作
    public void update(String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 获取数据库的连接
            conn = JDBCUtils.getConnection();
            // 预编译sql语句 返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            // 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }
            // 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
    }
    //修改customers表的一条记录
    @Test
    public void testUpdate() {
        // 获取数据库的连接
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            // 预编译sql语句 返回preparedstatemen的实例
            String sql ="update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            // 填充占位符
            ps.setObject(1,"莫扎特");
            ps.setObject(2,18);
            // 执行
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
    }

    // 向 customers 表中添加一条记录
    @Test
    public void testInsert()  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 读取配置文件中的四个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.propertise");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            // 加载驱动
            Class.forName(driverClass);

            // 获取连接
            conn = DriverManager.getConnection(url,user,password);
            //System.out.println(conn);

            // 预编译sql语句
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            ps = conn.prepareStatement(sql);

            //填充占位符
            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@163.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");
            ps.setDate(3,new Date(date.getTime()));

            // 执行sql
            ps.execute();

        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            // 资源关闭
            try {
                if(ps != null)
                    ps.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            try{
                if (conn != null)
                    conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
