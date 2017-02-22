package com.sln.webdisk.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sln.webdisk.business.User;
import com.sln.webdisk.data.UserDB;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserDB.class)
public class SingupControllerTest {
	@Mock HttpServletRequest request;
	@Mock HttpServletResponse response;
	@Mock HttpSession session;
	@Mock RequestDispatcher rd;
	@Mock ServletContext myMockServletContext;
	
	SignupController signupController;
	 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// if you're using @Mock annotations you need to run this once
		// OR anotate class with @RunWith(MockitoJUnitRunner.class)
		// see https://static.javadoc.io/org.mockito/mockito-core/2.7.6/org/mockito/Mockito.html#9
		MockitoAnnotations.initMocks(this);
		
		signupController = new SignupController() {
			@Override
			public ServletContext getServletContext() {
				return myMockServletContext;
			}
		};
	}

	@Test
	public void testDoLogout() throws ServletException, IOException {
		// Example taken from: http://javaworld-abhinav.blogspot.com/2014/06/testing-servlets-using-junit-and-mockito.html
		// You should mock either through @Mock annotations OR as below
		// If you use @Mock annotations don't forget to run MockitoAnnotations.initMocks(this);
		// OR anotate class with @RunWith(MockitoJUnitRunner.class)
		//HttpServletRequest request = mock(HttpServletRequest.class);
		//HttpServletResponse response = mock(HttpServletResponse.class);
		//HttpSession session = mock(HttpSession.class);
		//RequestDispatcher rd=mock(RequestDispatcher.class);
		
		when(request.getRequestURI()).thenReturn("/doLogout");
		when(request.getSession()).thenReturn(session);
		//when(myMockServletContext.getRequestDispatcher("/signup/loggedout.jsp")).thenReturn(rd);
		when(myMockServletContext.getRequestDispatcher(anyString())).thenReturn(rd);
		
		signupController.doGet(request, response);

		verify(session).setAttribute("user", null);
		verify(request).logout();
		verify(myMockServletContext).getRequestDispatcher("/signup/loggedout.jsp");
		verify(rd).forward(request, response);
	}
	
	@Test
	public void testRegisterUser() throws ServletException, IOException {
		// setup some sample data
		long userId = 100;
		String email = "some@email.com";
		String realName = "Some Real Name";
		String password = "SomePassword";
		User user = new User();
		user.setUserId(userId);
		user.setEmail(email);
		user.setPassword(password);
		user.setRealName(realName);
		
		// setup some pre-conditions
		// PowerMockito for static methods
		PowerMockito.mockStatic(UserDB.class);
        when(UserDB.emailExists(email)).thenReturn(false);
        when(UserDB.insert(Mockito.any(User.class))).thenReturn(userId);
        // Mockito
		when(request.getRequestURI()).thenReturn("/registerUser");
		when(request.getSession()).thenReturn(session);
		when(myMockServletContext.getRequestDispatcher(Mockito.anyString())).thenReturn(rd);
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getParameter("email")).thenReturn(email);
		when(request.getParameter("password")).thenReturn(password);
		when(request.getParameter("realName")).thenReturn(realName);
		
		// execute my class under the test
		signupController.doPost(request, response);
		
		// verify if behavior was as expected
		verify(session).setAttribute(Mockito.eq("user"), Mockito.isA(User.class));
		verify(session).setAttribute(Mockito.eq("user"), Mockito.argThat(addedUser -> addedUser.equals(user)));
		verify(request).login(email, password);
		verify(myMockServletContext).getRequestDispatcher("/signup/thankyou.jsp");
		verify(rd).forward(request, response);
	}

}
