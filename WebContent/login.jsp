<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login to ReviewMe®</title>
  
  
  
      <link rel="stylesheet" href="css/style.css">

  
</head>

<body>
<% 
String message = (String)session.getAttribute("message");
%>

<div class="container">
	<section id="content">
		<form action="LoginServlet" method="post">
			<h1>Login Form</h1>
			<div>
				<input type="text" placeholder="Username" required="" name="UserName" />
			</div>
			<div>
				<input type="password" placeholder="Password" required="" name="Password" />
			</div>
			<div>
				<input type="submit" value="Log in" />

				<a href="signUp.jsp">Create an account</a><p>Not registered?</p>
			</div>
		</form><!-- form -->
	</section><!-- content -->
	
	
	    <%--<c:set var="test2" value="${message}" />
	<c:choose>
    <c:when test="${message != null}">
            <c:out value="${message}" escapeXml="false" />
    </c:when>
</c:choose> --%>

</div><!-- container -->
</body>
    <script  src="js/index.js"></script>
</html>
