package com.sln.webdisk.data;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;

/**
* Static singleton class
* retrieves DataSource object from resource specified in META-INF/context.xml 
*
* @author  Sln
* @version 1.0
*/
public class ConnectionPool {

    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;

    /**
     * @return static instance of itself.
     */    
    public synchronized static ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public ConnectionPool() {
        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/webdiskDB");
        } catch (NamingException e) {
            System.err.println(e);
        }
    }

    /**
     * Use this to get a connection from DataSource.
     * @return Connection.
     */    
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
//        	String DATABASE_URL = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/heroku_fda2a88d3f1d69f?reconnect=true";
//        	String DATABASE_USERNAME = "b05ab820ed6e39";
//        	String DATABASE_PASSWORD = "d6213621";
//        	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        	Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//        	if (connection == null) {
//            	System.out.println("conn null");
//        	} else {
//            	System.out.println("conn not null");
//        		
//        	}
//        	return connection;
        } catch (SQLException sqle) {
            System.err.println(sqle);
            return null;
        }
    }

    /**
     * Just a helper class to close the connection.
     * @param c connection to close.
     */    
    public void freeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }
}