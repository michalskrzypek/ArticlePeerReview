<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a review | ReviewMe®</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>

<%
if (session == null || session.getAttribute("userkind") == null) {
	response.sendRedirect("login.jsp");
} else {
	String userkind = (String) session.getAttribute("userkind");
	if (!userkind.equalsIgnoreCase("reviewer")) {
		response.sendRedirect("login.jsp");
	}
}
%>
	<div class="response_container">
		<section id="response_content">
		<form>
			<h1>Create a review</h1>
		</form>
		<br>
<form name="queryForm" action="ReviewCreationServlet" method="post">
<textarea name="content" cols="100" rows="10"></textarea><br>
<br>
<h3>Mark:</h3>
  <input type="radio" name="mark" value="1" checked> 1
  <input type="radio" name="mark" value="2"> 2
  <input type="radio" name="mark" value="3"> 3
  <input type="radio" name="mark" value="4"> 4
  <input type="radio" name="mark" value="5"> 5
  <br>
  
<input class="button_2" type="submit" value="Send your review">
</form>
		</section>
	</div>
</body>
</html>