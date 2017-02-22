<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>
	<!-- Import the core JSTL library -->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


	<h1>Already logged in </h1>
	<p>Email ${user.email} already logged in.</p>
	<p>Please proceed to <a href="<c:url value='/disk/controller' />">MyDisk</a>.</p>
	<p>Or logout.</p>
	<br>
	<form action="<c:url value='/signup/controller/doLogout'/>" method="get" id="float_left">
  		<input type="submit" value="Logout">
	</form>
	

</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />