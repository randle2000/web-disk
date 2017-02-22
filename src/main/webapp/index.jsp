<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>
    <h1>Welcome to My WebDisk!</h1>

    <p>Thanks for visiting. Make yourself at home. Feel free to register or login.</p>

    <p>Click on a link below:</p>
    <ul>
        <li><a href="<c:url value='/' />">
        		  Home</a></li>
        <li><a href="<c:url value='/signup/controller' />">
                Sign Up</a></li>
        <li><a href="<c:url value='/disk/controller' />">
                MyDisk</a></li>
        <li><a href="<c:url value='/signup/controller/doLogout' />">
                Logout</a></li>
    </ul>
    
</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />