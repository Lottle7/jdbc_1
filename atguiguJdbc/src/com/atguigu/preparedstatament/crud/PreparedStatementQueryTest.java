package com.atguigu.preparedstatament.crud;

import com.atguigu.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/*
    使用PreparedStatement实现针对于不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {
    public Object getInstance(String sql,Object ...args) throws Exception {
        Connection conn = JDBCUtils.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);

        return null;
    }
}
