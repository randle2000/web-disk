package com.sln.webdisk.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sln.webdisk.business.User;
import com.sln.webdisk.data.UserDB;

/**
 * This servlet will process calls to:
 * 		signup/controller/doLogout
 * 		signup/controller/registerUser
 *
 * @author  Sln
 * @version 1.0
 */
public class SignupController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String defaultURL = "/signup/register.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String url;
        if (requestURI.endsWith("/doLogout")) {
        	url = doLogout(request, response);
        } else {
        	url = checkUser(request, response);
        }
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String url = defaultURL;
        if (requestURI.endsWith("/registerUser")) {
        	url = registerUser(request, response);
        } 
        getServletContext()
        	.getRequestDispatcher(url)
        	.forward(request, response);
	}

	private String doLogout(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.logout();
			request.getSession().setAttribute("user", null);
		} catch (ServletException e) {
			e.printStackTrace();
			return "/";
		}
		return "/signup/loggedout.jsp";
	}

	private String checkUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "/signup/already_loggedin.jsp"; 	
        } else {
        	return defaultURL;
        }
    }	

	private String registerUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
        	// should never happen?
            return "/signup/already_loggedin.jsp"; 	
        } else {
            String email = request.getParameter("email");
            String realName = request.getParameter("realName");
            String password = request.getParameter("password");
            if (UserDB.emailExists(email)) {
            	request.setAttribute("existingemail", email);
            	return "/signup/userexists.jsp";
            }
            user = new User();
            user.setEmail(email);
            user.setRealName(realName);
            user.setPassword(password);
            final long userId = UserDB.insert(user);
            user.setUserId(userId);
            try {
				request.login(user.getEmail(), user.getPassword());
                session.setAttribute("user", user);
			} catch (ServletException e) {
				e.printStackTrace();
			}
        	return "/signup/thankyou.jsp";
        }
	}


}
