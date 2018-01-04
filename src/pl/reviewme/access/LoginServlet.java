package pl.reviewme.access;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class LoginServlet Servlet is used to log in users
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int id = 0;
	private static String firstName = null;
	private static String lastName = null;
	private static String userName = null;
	private static String userPassword = null;
	private static String userKind = null;
	private String query = null;
	ResultSet queryResult = null;

	/**
	 * Default constructor.
	 */
	public LoginServlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession existingSession = request.getSession(false);
		if (existingSession != null && existingSession.getAttribute("userkind") != null) {
		String ukind = (String)existingSession.getAttribute("userkind");
		
		if(ukind.equalsIgnoreCase("author")){
			out.println("<div id=\"message\"><p>You are already logged in.</div></p>");
			RequestDispatcher rd = request.getRequestDispatcher("mainAuthor.jsp");
			rd.include(request, response);
		}else if(ukind.equalsIgnoreCase("reviewer")){
			out.println("<div id=\"message\"><p>You are already logged in.</div></p>");
			RequestDispatcher rd = request.getRequestDispatcher("mainReviewer.jsp");
			rd.include(request, response);
		}else if(ukind.equalsIgnoreCase("redactor")){
			out.println("<div id=\"message\"><p>You are already logged in.</div></p>");
			RequestDispatcher rd = request.getRequestDispatcher("mainRedactor.jsp");
			rd.include(request, response);
		}
		}else {

		try {
			HttpSession session = request.getSession();
			
			Class.forName("com.mysql.jdbc.Driver");
			Statement stmt = DBManager.getConnection().createStatement();

			userName = request.getParameter("UserName");
			userPassword = request.getParameter("Password");

			query = "Select * from authors where username='" + userName
					+ "' union Select * from reviewers where username='" + userName
					+ "' union select * from redactors where username='" + userName + "';";
			queryResult = stmt.executeQuery(query);

			// Firstly, we check if there's a user with the username
			if (queryResult.next()) {

				query = "Select * from authors where username='" + userName + "' and password='" + userPassword
						+ "' union Select * from reviewers where username='" + userName + "' and password='"
						+ userPassword + "' union select * from redactors where username='" + userName
						+ "' and password='" + userPassword + "';";
				queryResult = stmt.executeQuery(query);

				// checking if there's user with compatible both username and password
				if (queryResult.next()) {

					id = queryResult.getInt(1);
					firstName = queryResult.getString(2);
					lastName = queryResult.getString(3);
					userKind = queryResult.getString(4);
					userName = queryResult.getString(5);
					out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
							"<html lang=\"en\">\r\n" + 
							"<head>\r\n" + 
							"  <meta charset=\"UTF-8\">\r\n" + 
							"  <title>ReviewMe®</title>\r\n" + 
							"  \r\n" + 
							"  \r\n" + 
							"  \r\n" + 
							"      <link rel=\"stylesheet\" href=\"css/style.css\">\r\n" + 
							"\r\n" + 
							"  \r\n" + 
							"</head>\r\n" + 
							"\r\n" + 
							"<body><div class=\"container\">");

					out.println("<h4>Successfully logged in as " + userName
							+ ". You will be automatically redirect to main " + userKind + "'s page.</h4>");

					// creating a new session for a particular user and setting necessary attributes
					session = request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("fname", firstName);
					session.setAttribute("lname", lastName);
					session.setAttribute("username", userName);
					session.setAttribute("userkind", userKind);

					if (userKind.equalsIgnoreCase("author")) {
						response.setHeader("Refresh", "5; URL=mainAuthor.jsp");
					} else if (userKind.equalsIgnoreCase("reviewer")) {
						response.setHeader("Refresh", "5; URL=mainReviewer.jsp");
					} else if (userKind.equalsIgnoreCase("redactor")) {
						response.setHeader("Refresh", "5; URL=mainRedactor.jsp");
					}

				} else {
					RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
					rd.include(request, response);
					out.println("<div id=\"message\"><p>Wrong password!</p></div>");
				}

			} else {
				
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.include(request, response);
				out.println("<div id=\"message\"><p>Wrong username!</p></div>");


			}
			out.println( "</div></body></html>");
			queryResult.close();
			stmt.close();
		} catch (ClassNotFoundException e) {
			out.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
