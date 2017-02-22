<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp" />

<!-- start the middle column -->

<section>
    <h1>You have logged it!</h1>
    <p>Thank you, ${user.realName}.</p>
    <p>Welcome to your Disk</p>
    <hr>
    
	<h1>Your files:</h1>
	<c:choose>
	    <c:when test="${userFiles == null}">
	        <p>Your Disk is empty.</p>
	    </c:when>
	    <c:otherwise>
	      <table width="100%">
	      	<col width="10%">
	      	<col width="50%">
	      	<col width="10%">
	      	<col width="20%">
	      	<col width="5%">
	      	<col width="5%">
	         <tr>
	          <th style="text-align:right">FileID</th>
	          <th>File Name</th>
	          <th style="text-align:right">Size</th>
	          <th style="text-align:right">Upload Date</th>
	          <th>&nbsp;</th>
	          <th>&nbsp;</th>
	       </tr>
	        <c:forEach var="item" items="${userFiles}">
	          <tr class="cart_row">
	            <td style="text-align:right">${item.fileId}</td>
	            <td>${item.fileName}</td>
	            <td style="text-align:right">${item.fileSize}</td>
	            <td style="text-align:right">${item.uploadDate}</td>
	            <td>
		              <form action="<c:url value='/disk/controller/downloadFile'/>" method="get">
		                <input type="hidden" name="fileId" value="<c:out value='${item.fileId}'/>">
		                <input type="hidden" name="fileName" value="<c:out value='${item.fileName}'/>">
		                <input type="submit" value="Download">
		              </form>
	            </td>
	            <td>
		              <form action="<c:url value='/disk/controller/deleteFile'/>" method="post">
		                <input type="hidden" name="fileId" value="<c:out value='${item.fileId}'/>">
		                <input type="hidden" name="fileName" value="<c:out value='${item.fileName}'/>">
		                <input type="submit" value="Delete">
		              </form>
	            </td>
	          </tr>
	        </c:forEach>
	          <tr>
	            <td colspan="4">
	            	&nbsp;
	            </td>
	            <td colspan="2">&nbsp;</td>
	          </tr>
	        </table>
	    </c:otherwise>
	</c:choose>
    
    
    
    <hr>
    <h1 style="color:#0081c3;">You can upload new file here</h1>
    <p>(You can upload multiple files with the same name)</p>
    <p>(Up to 1MB per file; Filename should be less than 50 chars)</p>
    <form method="post" action="<c:url value='/disk/controller/uploadFile'/>" enctype="multipart/form-data">
    	Upload file: <input type="file" name="file" size="50"/>
    	<input type="submit" value="Upload">
    </form>
    <br>
    <p style="color:red;"><strong>${message}</strong></p>
</section>

<!-- end the middle column -->

<jsp:include page="/includes/footer.jsp" />