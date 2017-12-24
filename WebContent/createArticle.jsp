<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create an article</title>
</head>
<body>
<h3>create article</h3>
<form name="queryForm" action="ArticleCreationServlet" method="post">
<textarea name="content" col="100" rows="10"></textarea><br>
<input type="submit" value="Send your article">
</form>
</body>
</html>