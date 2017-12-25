<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SQL query</title>
</head>
<body>
	<h1>Author Page</h1>
	<h3>What do you want to do?</h3>
	<br>


	<form action="createArticle.jsp" method="post">
		<input type="submit" name="query" value="Create an article"></br>
	</form>


	<form action="ArticlesViewerServlet" method="post">
		<input type="submit" name="query" value="Show my articles"><br>
	</form>

	<form action="DecisionsViewerServlet" method="post">
		<input type="submit" name="query"
			value="Show all decisions to my articles">
	</form>



	<%--<form action="ReviewsViewerServlet" method="post">
		<input type="submit" name="query"
			value="Show all reviews to my articles">
	</form>
 --%>

	<%--<form action="StatusServlet" method="post">
		<input type="submit" name="query" value="Show all statuses to ma articles"><br>
	</form>
 --%>

	<br>
	<br>
	<br>
	<form action="LogoutServlet">
		<button type="submit">LOG OUT</button>
	</form>
</body>
</html>