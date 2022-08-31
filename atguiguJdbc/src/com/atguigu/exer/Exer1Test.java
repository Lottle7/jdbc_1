package com.atguigu.exer;

import com.atguigu.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Exer1Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入用户名：");
        String name = sc.next();
        System.out.print("请输入邮箱：");
        String email = sc.next();
        System.out.print("请输入生日：");
        String birthday = sc.next();

        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int updateCount = update(sql,name,email,birthday);
        if(updateCount != 0 ){
            System.out.println("添加成功");
        }else {
            System.out.println("添加失败");
        }
    }

    public static int update(String sql,Object ...args) {
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
            //ps.execute();
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }
}
