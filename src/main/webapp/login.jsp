<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>

<h1>Login Form</h1>
<p>Please enter a username and password to continue.</p>
<form action="j_security_check" method="post">
    <label>Email</label>
    <input type="email" name="j_username" required><br>
    <label>Password</label>
    <input type="password" name="j_password" required><br>
    <label>&nbsp;</label>
    <input type="submit" value="Login">
</form>

</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />