<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Author's Page | ReviewMe�</title>
<link rel="stylesheet" href="css/style.css">
</head>



<body>

	<%
	if (session == null || session.getAttribute("userkind") == null) {
		response.sendRedirect("login.jsp");
	} else {
		String userkind = (String) session.getAttribute("userkind");
		if (!userkind.equalsIgnoreCase("author")) {
			response.sendRedirect("login.jsp");
		}
	}
	
	
		String theTitle = (String) session.getAttribute("title");
		String theContent = (String) session.getAttribute("content");
	%>

	<div class="response_container">
		<section id="response_content">
		<form>
			<h1>Modify the article</h1>
		</form>
		<br>

		<form name="queryForm" action="UpdateArticleServlet" method="post">
			<h3>Title:</h3>
			<textarea name="title" cols="100" rows="1"><%=theTitle%></textarea>
			<br>
			<h3>Article:</h3>
			<textarea name="content" cols="100" rows="30"><%=theContent%></textarea>
			<br> <input class="button_2" type="submit" value="Submit changes">
		</form>
		</section>
	</div>

</body>
</html>