package com.sln.webdisk.data;

// It seems that mocking of static methods is not a every good idea - it leads to some weird results
// The need to mock static methods is also a sign of bad design

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sln.webdisk.business.User;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class UserDBTest {
//	private static final String TABLES_SQL_FILE = "src/main/webapp/WEB-INF/tables.sql";
	private static String DATABASE_URL = null;
	private static String DATABASE_USERNAME;
	private static String DATABASE_PASSWORD;
	private static String TEMPORARY_DATABASE_NAME;
	private static String TABLES_SQL_FILE;
	
	private static Connection connection = null;
	
//	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();	
	private User user1, user2, user3;
	
	// Java properties example: https://www.mkyong.com/java/java-properties-file-examples/
	static {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "mytest.properties";
			input = UserDBTest.class.getClassLoader().getResourceAsStream(filename);
			if(input==null)	{
	            System.out.println("Sorry, unable to find file " + filename);
			} else {
				//load a properties file from class path, inside static method
				prop.load(input);
				//get the property value and print it out
				DATABASE_URL = prop.getProperty("dbUrl");
				DATABASE_USERNAME = prop.getProperty("dbUsername");
				DATABASE_PASSWORD = prop.getProperty("dbPassword");
				TEMPORARY_DATABASE_NAME = prop.getProperty("temDbName");
				TABLES_SQL_FILE = prop.getProperty("tablesSqlFile");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private static void createTemporaryDatabase(Connection connection) throws SQLException, FileNotFoundException {
		List<String> sqlCommands = new ArrayList<>();
		
		sqlCommands.add("DROP DATABASE IF EXISTS " + TEMPORARY_DATABASE_NAME);
		sqlCommands.add("CREATE DATABASE " + TEMPORARY_DATABASE_NAME);
		sqlCommands.add("USE " + TEMPORARY_DATABASE_NAME);

		//File sqlFile = Paths.get(TABLES_SQL_FILE).toFile();
		//Scanner sqlScanner = new Scanner(new BufferedReader(new FileReader(sqlFile)));
		InputStream is = UserDBTest.class.getClassLoader().getResourceAsStream(TABLES_SQL_FILE);
		Scanner sqlScanner = new Scanner(new BufferedReader(new InputStreamReader(is)));
		sqlScanner.useDelimiter(";");
		while (sqlScanner.hasNext()) {
			String st = sqlScanner.next().trim();
			if (!st.isEmpty()) {  // only add non-empty commands
				sqlCommands.add(st);
			} 
		}
		sqlScanner.close();
		
		Statement stmt = connection.createStatement();
		for (String sql : sqlCommands) {
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
		stmt.close();
	}
	
	private static void clearUsersTable(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.execute("DELETE FROM users");
		stmt.close();
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
    	connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    	createTemporaryDatabase(connection);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("DROP DATABASE " + TEMPORARY_DATABASE_NAME);
		connection.close();
	}
	
	@Before
	public void setUp() throws SQLException {
    	// methods in data package always close the connection after they are finished with it
    	// to prevent this I have to use connectionSpy instead of connection
    	Connection connectionSpy = Mockito.spy(connection);	 
    	Mockito.doNothing().when(connectionSpy).close();
    	// mocking pool so it will return our connectionSpy
        ConnectionPool pool = Mockito.mock(ConnectionPool.class);
        Mockito.when(pool.getConnection()).thenReturn(connectionSpy);
    	// using PowerMockito because Mockito cannot mock static methods
    	// (i.e. ConnectionPool.getInstance() in our case)
		PowerMockito.mockStatic(ConnectionPool.class);
        PowerMockito.when(ConnectionPool.getInstance()).thenReturn(pool);
		
//	    System.setOut(new PrintStream(outContent));
//	    System.setErr(new PrintStream(errContent));

		// Instantiate some sample users
		user1 = new User();
		user1.setEmail("user1@ccc.com");
		user1.setPassword("user1");
		user1.setRealName("user1");
		
		user2 = new User();
		user2.setEmail("user2@ccc.com");
		user2.setPassword("user2");
		user2.setRealName("user2");

		user3 = new User();
		user3.setEmail("user3@ccc.com");
		user3.setPassword("user3");
		user3.setRealName("user3");
		
		clearUsersTable(connection);
	}

	@After
	public void tearDown() {
//	    System.setOut(null);
//	    System.setErr(null);
	}		

	@Test
	public void canInsertNewUser() throws Throwable {
		long lastId = UserDB.insert(user1);
		user1.setUserId(lastId);
		assertThat(lastId, is(not(-1)));
	}
	
	@Ignore("doesn't work. PowerMock cannot go through catched exception in UserDB") 
	@Test
	public void cannotInsertTwoIdenticalUsers() {
		long lastId = UserDB.insert(user1);
		user1.setUserId(lastId);
		lastId = UserDB.insert(user1);
		assertThat(lastId, is(-1L));
	}

	@Ignore("will do later")
	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	
	@Test
	public void testAaa() {
//		User user = Mockito.mock(User.class);
//		when(user.getEmail()).thenReturn("aaa@aaa.com");
//		when(user.getUserId()).thenReturn((long) 100);
//		
//		assertEquals(100, UserDB.aaa(user));
//		verify(user, atLeastOnce()).getEmail();
//		verify(user, never()).getRealName();
		
//		PowerMockito.mockStatic(UserDB.class);
//        Mockito.when(UserDB.insert(user)).thenReturn((long) 999);
//        System.out.println(UserDB.insert(user));
        
	}
	
	@Test
	public void testInsertAndSelectUser() {
		long lastId = UserDB.insert(user1);
		user1.setUserId(lastId);
		String email = user1.getEmail();
		assertThat(UserDB.emailExists(email), is(true));
		User user = UserDB.selectUser(email);
		assertThat(user, is(equalTo(user1)));
	}
}
