package com.atguigu.connection;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    // 方式一
    @Test
    public void testConnection1() throws SQLException {
        // 获取Driver实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        // jdbc:mysql: 协议
        // localhost : ip地址
        // 3306: 默认mysql的端口号
        // test: 数据库名
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        // 将用户名和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","Lottle777");

        Connection conn = driver.connect(url,info);

        System.out.println(conn);
    }
    // 方式二 对方式一的迭代 在如下的程序中不出现第三方的api 使程序具有更好的可移植性
    @Test
    public void testConnection2() throws Exception{
        // 获取Driver实现类对象 使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver =  (Driver) clazz.newInstance();

        // 提供要链接的数据库
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";

        // 提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","Lottle777");

        // 获取连接
        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }
    // 方式三 使用DriverManage 替换Driver
    @Test
    public void testConnection3() throws Exception {

        // 获取Driver实现类对象 使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver =  (Driver) clazz.newInstance();

        // 提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Lottle777";

        // 注册驱动
        DriverManager.registerDriver(driver);

        // 获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
    // 方式四 相比于方式三可以省略部分代码
    @Test
    public void testConnection4() throws Exception {

        // 提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Lottle777";

        // 加载Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Driver driver =  (Driver) clazz.newInstance();
        // 注册驱动 DriverManager.registerDriver(driver);

        // 获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
    // 方式五(final) 将数据库连接需要的四个基本信息声明在配置文件中 通过读取配置文件的方式获取连接
    @Test
    public void testConnection5() throws Exception {

        // 读取配置文件中的四个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.propertise");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        // 加载驱动
        Class.forName(driverClass);

        // 获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }

}
