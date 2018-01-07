<%@ page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Redactor's Page | ReviewMe®</title>
<link rel="stylesheet" href="css/style.css">
</head>


<body>

<% 
//Preventing back button after logging out
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");

String fName = "";
String lName = "";
String userkind = "";
Date lastAccess = null;
int id = 0;
if (session == null || session.getAttribute("userkind") == null) {
	out.print("<div id=\"message\"><p>Please login first!</p></div>");
	response.sendRedirect("login.jsp");
} else{
	userkind = (String) session.getAttribute("userkind");
	if (!userkind.equalsIgnoreCase("redactor")) {
		response.sendRedirect("login.jsp");
	}else{
		id=(int)session.getAttribute("id");
	fName = (String)session.getAttribute("fname");
	lName = (String)session.getAttribute("lname");
	lastAccess = new Date(session.getCreationTime());
	}
}
%>

	<div class="container">
		<section id="content">
		<form>
			<h1>Redactor's Page</h1>
		</form>
		

	
	<form action="ArticlesCheckerServlet" method="post">
		<input class="button_2" type="submit" name="query" value="Reviewed articles"></br>
	</form>
	
	<form action="DecisionsCheckerServlet" method="post">
		<input class="button_2" type="submit" name="query" value="My decisions"></br>
	</form>
	

		<br>
		<br>
		</section>

		<section id="content">
		
<h2><%=fName%> <%=lName%></h2><h3><%=userkind %> ID: <%=id %></h3>
<% if(lastAccess != null){ %>
<p>Logged: <%=lastAccess.toString() %></p>
<%} %>

		<form action="LogoutServlet">
			<input class="button_1" type="submit" value="Log out">
		</form>
		</section>
	</div>

</body>
</html>
