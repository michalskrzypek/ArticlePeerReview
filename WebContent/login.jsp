<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign up to ReviewMe®</title>
</head>
<body>
<h2>LOG IN</h2>
<div style="margin: auto; position=absolute">
<form action="LoginServlet" method="post">
<table style="border:1px solid black">
<tr><td>Username: </td><td><input type="text" size="15" name="UserName"></td></tr>
<tr><td>Password: </td><td><input type="password" size="16" name="Password"></td></tr>
</table><br>
<input type="submit" value="Log in">
</form>
<br>
<a href="signUp.jsp">or sign up</a>
</div>
</body>
</html>