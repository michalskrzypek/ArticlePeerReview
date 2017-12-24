<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SQL query</title>
</head>
<body>
	<h1>Reviewer Page</h1>
	<br>
	<h3>What do you want to do?</h3>
	<br>

	<form action="ArticlesToReviewViewer" method="post">
		<input type="submit" name="query" value="Show articles to review"><br>
	</form>


	<form action="MyReviewsViewerServlet" method="post">
		<input type="submit" name="query" value="Show my reviews"><br>
	</form>

	<br>
	<br>
	<br>
	<form action="LogoutServlet">
		<button type="submit">LOG OUT</button>
	</form>
</body>
</html>