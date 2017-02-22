<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>
	<!-- Import the core JSTL library -->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


	<h1>Such user already exists</h1>
	<p>Email ${existingemail} already exists in our database.</p>
	<p>Please login to your disk where you will be able to update your data.</p>
	<br>
	<p>Or try to signup again with another email.</p>
	<form action="<c:url value='/disk/controller'/>" method="get" id="float_left">
  		<input type="submit" value="Login">
	</form>
	
	<form action="<c:url value='/signup/controller'/>" method="get" id="float_left">
  		<input type="submit" value="Signup">
	</form>
	

</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />