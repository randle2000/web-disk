<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>
	<!-- Import the core JSTL library -->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<h1>Register Form</h1>
	<p>Please enter your name, email and password to register.</p>
	<p>Or click <a href="<c:url value='/disk/controller' />">MyDisk</a> if you're already registered.</p>
	
	  
	<form action="<c:url value='/signup/controller/registerUser'/>" method="post">
	    <label>Your name</label>
	    <input type="text" name="realName" required><br>
	    <label>Email</label>
	    <input type="email" name="email" required><br>
	    <label>Password</label>
	    <input type="password" name="password" required><br>
	    <label>&nbsp;</label>
	    <input type="submit" value="Register">
	</form>

</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />