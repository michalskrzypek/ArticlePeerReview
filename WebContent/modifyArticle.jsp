<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create an article</title>

</head>
<body>
<h3>modify article</h3>

<%
String theTitle = (String)session.getAttribute("title");
String theContent = (String)session.getAttribute("content");
%>

<form name="queryForm" action="UpdateArticleServlet" method="post">
Title: <textarea name="title" cols="100"><%=theTitle%></textarea><br>
Content: <textarea name="content" cols="100" rows="10"><%=theContent%></textarea><br>
<input type="submit" value="Submit changes">
</form>
</body>
</html>