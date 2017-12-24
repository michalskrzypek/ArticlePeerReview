<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign up to ReviewMe®</title>
</head>
<body>
	<h2 style="color: blue">SIGN UP</h2>

	<form action="SignUpServlet" method="post">
		<table style="border: 1px solid black">
			<tr>
				<td>First name:</td>
				<td><input type="text" size="15" name="FirstName" /></td>
			<tr>
			<tr>
				<td>Last name:</td>
				<td><input type="text" size="15" name="LastName"></td>
			</tr>
			<tr>
				<td>Username:</td>
				<td><input type="text" size="15" name="UserName"></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" size="16" name="Password"></td>
			</tr>
		</table>
		<input type="radio" name="userKind" value="Author" checked>
		Author <input type="radio" name="userKind" value="Reviewer">
		Reviewer <input type="radio" name="userKind" value="Redactor">
		Redactor<br>
		<br> <input type="submit" value="Sign up">
	</form>
	<br>
	<a href="login.jsp">or log in</a>
</body>
</html>