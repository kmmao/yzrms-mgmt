/*
 * ConnectionFactory.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-22 12:10:11
 */
package com.yz.rms.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 连接工厂类 
 * @author Qiu Dongyue <qdy@yzhtech.com>
 */
public class ConnectionFactory {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public static String url = "jdbc:mysql://172.16.100.26:3306/yzrms?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
    public static String dbName = "user3";
    public static String dbPwd = "user3456";
    public static boolean usePool = true;
    private static final String POOL_NAME = "jdbc/yzrms";

    private static Connection getConnectionByDriverManager() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(url, dbName, dbPwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }
 
    public static Connection getConnection() throws NamingException, ClassNotFoundException, SQLException {
        Connection con = null;
        if (usePool) {
            try {
                Context ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(POOL_NAME);
                con = ds.getConnection();
            } catch (NamingException | SQLException e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            con = getConnectionByDriverManager();
        }
        return con;
    }
}
