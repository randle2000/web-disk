<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>
	<!-- Import the core JSTL library -->
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<h1>Thank you</h1>

	<p>Thank you ${user.realName} for registering!</p>
	<p>Please proceed to your disk now.</p>
	<form action="<c:url value='/disk/controller'/>" method="get" id="float_left">
  		<input type="submit" value="MyDisk">
	</form>
	
	

</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />