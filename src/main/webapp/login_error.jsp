<jsp:include page="/includes/header.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- start the middle column -->

<section>

<h1>Login Form - Error</h1>
<p>You did not log in successfully.</p>
<p>Please check your username and password and <a href="<c:url value='/disk/controller' />">try again</a>.</p>

</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />