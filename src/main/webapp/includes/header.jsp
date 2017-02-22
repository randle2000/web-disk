<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>

<html>
<head>
    <meta charset="utf-8">
    <title>My Web Disk</title>
    <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
</head>

<body>

    <header id="upper_links">
	    <img src="<c:url value='/images/logo.png'/>" 
    	      			alt="Disk Logo" width="58">
     	<h1>My Web Disk</h1>
     	<h2>Your free web storage!</h2>
     	<br>
	    <ul>
	        <li><a href="<c:url value='/' />">
	        		  Home</a></li>

  			<c:choose>
      			<c:when test="${user == null}">
			        <li><a href="<c:url value='/signup/controller' />">
			                Sign Up</a></li>
			                
			        <li><a href="<c:url value='/disk/controller' />">
			                MyDisk</a></li>
      			</c:when>
      			<c:otherwise>
			        <li><a href="<c:url value='/disk/controller' />">
			                MyDisk</a></li>

			        <li><a href="<c:url value='/signup/controller/doLogout' />">
			                Logout</a></li>
	        	</c:otherwise>
  			</c:choose>
	        		  
	    </ul>
    </header>
