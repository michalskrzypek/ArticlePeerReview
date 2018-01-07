<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Sign Up to ReviewMe®</title>
  
  
  
      <link rel="stylesheet" href="css/style.css">

  
</head>

<body>
<%
HttpSession existingSession = request.getSession(false);

String fname = "";
String lname = "";
String username = "";
String password = "";

if(existingSession.getAttribute("userkind") != null){
	
		String ukind = (String)existingSession.getAttribute("userkind");
		
		if(ukind.equalsIgnoreCase("author")){
			response.sendRedirect("mainAuthor.jsp");
		}else if(ukind.equalsIgnoreCase("reviewer")){
			response.sendRedirect("mainReviewer.jsp");
		}else if(ukind.equalsIgnoreCase("redactor")){
			response.sendRedirect("mainRedactor.jsp");
		}
		
	}else if(existingSession.getAttribute("signUpFname") != null || existingSession.getAttribute("signUpLname") != null || existingSession.getAttribute("signUpUsername") != null || existingSession.getAttribute("signUpPassword") != null){
		
	fname = (String)existingSession.getAttribute("signUpFname");	
	lname = (String)existingSession.getAttribute("signUpLname");	
	username = (String)existingSession.getAttribute("signUpUsername");	
	password = (String)existingSession.getAttribute("signUpPassword");	
		
	}

%>


<div class="container">
	<section id="content">
		<form action="SignUpServlet" method="post">
			<h1>Create an account</h1>
			<div>
				<input type="text" placeholder="First Name" required="" name="FirstName" value=<%=fname %>>
			</div>
			<div>
				<input type="text" placeholder="Last Name" required="" name="LastName" value=<%=lname %>>
			</div>
			<div>
				<input type="text" placeholder="Username" required="" name="UserName" value=<%=username %>>
			</div>
			<div>
				<input type="password" placeholder="Password" required="" name="Password" value=<%=password%>>
			</div>
			<div><input type="radio" name="userKind" value="Author" checked>Author 
			<input type="radio" name="userKind" value="Reviewer">Reviewer 
			<input type="radio" name="userKind" value="Redactor">Redactor
			</div>
			<div>
				<input class="button_1" type="submit" value="Submit" />

				 <a href="login.jsp">Log in</a><p>Already have an account?</p>
			</div>
		</form><!-- form -->
	</section><!-- content -->
</div><!-- container -->

</body>
</html>
