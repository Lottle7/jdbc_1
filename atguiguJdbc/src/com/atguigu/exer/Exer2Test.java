package com.atguigu.exer;

import com.atguigu.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class Exer2Test {


    public static void main(String[] args) {
        // 向examstudent表中添加一条记录
//        Scanner sc = new Scanner(System.in);
//        System.out.print("4/6级:");
//        int type = sc.nextInt();
//        System.out.print("身份证号:");
//        String IDCard = sc.next();
//        System.out.print("准考证号:");
//        String examCard = sc.next();
//        System.out.print("学生姓名:");
//        String studentName = sc.next();
//        System.out.print("所在城市:");
//        String city = sc.next();
//        System.out.print("考试成绩:");
//        int grade = sc.nextInt();
//
//        String sql = "insert into examstudent(type,IDCard,examCard,studentName,location,grade)values(?,?,?,?,?,?)";
//        int updateCount = update(sql,type,IDCard,examCard,studentName,city,grade);
//        if(updateCount > 0 ){
//            System.out.println("添加成功");
//        }else {
//            System.out.println("添加失败");
//        }

        // 查询
        System.out.println("请选择你需要输入的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner sc = new Scanner(System.in);
        String selection = sc.next();
        if ("a".equalsIgnoreCase(selection)){
            System.out.println("请输入准考证号：");
            String examCard = sc.next();
            String sql ="select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
            Student student = getInstance(Student.class,sql,examCard);
            System.out.println(student);
        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号：");
            String IDCard = sc.next();
            String sql ="select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard = ?";
            Student student = getInstance(Student.class,sql,IDCard);
            System.out.println(student);
        }else {
            System.out.println("您的输入有误 请重新进入系统！");
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

    //根据身份证号或者准考证号查询考试信息
    @Test
    public void queryWithID(){
        System.out.println("请选择你需要输入的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner sc = new Scanner(System.in);
        String selection = sc.next();
        if ("a".equalsIgnoreCase(selection)){
            System.out.println("请输入准考证号：");
            String examCard = sc.next();
            String sql ="select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
            Student student = getInstance(Student.class,sql,examCard);
            System.out.println(student);
        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号：");
            String IDCard = sc.next();
            String sql ="select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard = ?";
            Student student = getInstance(Student.class,sql,IDCard);
            System.out.println(student);
        }else {
            System.out.println("您的输入有误 请重新进入系统！");
        }
    }

    @Test
    public void testDeleteByexamCard(){
        System.out.println("请输入准考证号：");
        Scanner sc = new Scanner(System.in);
        String examCard = sc.next();
        String sql = "delete from examstudent where examCard = ?";
        int deleteCount = update(sql,examCard);
        if (deleteCount > 0 ){
            System.out.println("删除成功");
        }else {
            System.out.println("查无此人！");
        }
    }


    public static <T> T getInstance(Class<T> clazz, String sql, Object... args) {
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
                    // 获取每个列的 别名
                    String columnName = rsmd.getColumnLabel(i+1);
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
