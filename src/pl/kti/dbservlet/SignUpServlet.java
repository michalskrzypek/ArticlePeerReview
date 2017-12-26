package pl.kti.dbservlet;

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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String fName = null;
	private static String lName = null;
	private static String userName = null;
	private static String userPassword = null;
	private static String userKind = null;

	private String query = null;

	/**
	 * Default constructor.
	 */
	public SignUpServlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Statement stmt = DBManager.getConnection().createStatement();
			fName = request.getParameter("FirstName");
			session.setAttribute("fname", fName);
			lName = request.getParameter("LastName");
			session.setAttribute("lname", lName);
			userName = request.getParameter("UserName");
			session.setAttribute("username", userName);
			userPassword = request.getParameter("Password");
			session.setAttribute("password", userPassword);
			userKind = request.getParameter("userKind");

			StringBuilder sb = new StringBuilder("");
			int errors = 0;

			if (fName.length() < 3) {
				errors++;
				sb.append("First Name is too short! (minimum 3 letters)<br>");
			}

			if (lName.length() < 3) {
				errors++;

				sb.append("Last Name is too short! (minimum 3 letters)<br>");
			}

			if (userName.length() < 6) {
				errors++;

				sb.append("Username is too short! (minimum 6 letters)<br>");
			}
			if (userPassword.length() < 6) {
				errors++;
				sb.append("Password is too short! (minimum 6 letters)<br>");

			}

			String message = sb.toString();
			if (errors > 0) {
				RequestDispatcher rd = request.getRequestDispatcher("signUp.jsp");
				rd.include(request, response);
				out.println("<div id=\"message\"><p>"+message+"</p></div>");
			} else {

				// Firstly we check if the username is already used
				query = "Select * from authors where username='" + userName + "' and password='" + userPassword
						+ "' union Select * from reviewers where username='" + userName + "' and password='"
						+ userPassword + "' union select * from redactors where username='" + userName
						+ "' and password='" + userPassword + "';";
				ResultSet queryResult = stmt.executeQuery(query);

				if (queryResult.next()) {
					RequestDispatcher rd = request.getRequestDispatcher("signUp.jsp");
					rd.include(request, response);
					out.println("<p style=\"color:red\">Username already exists!</p>");
				} else {

					// Inserting user regarding to the chosen userkind
					if (userKind.equalsIgnoreCase("Author")) {
						query = "INSERT INTO authors(fname,lname,username,password) VALUES('" + fName + "','" + lName
								+ "','" + userName + "','" + userPassword + "');";
					} else if (userKind.equalsIgnoreCase("Reviewer")) {
						query = "INSERT INTO reviewers(fname,lname,username,password) VALUES('" + fName + "','" + lName
								+ "','" + userName + "','" + userPassword + "');";
					} else if (userKind.equalsIgnoreCase("Redactor")) {
						query = "INSERT INTO redactors(fname,lname,username,password) VALUES('" + fName + "','" + lName
								+ "','" + userName + "','" + userPassword + "');";
					}

					stmt.executeUpdate(query);

					// Informing user about successful signing up
					if (userKind.equalsIgnoreCase("author")) {
						out.println("<b> Successfully created an " + userKind.toUpperCase() + ":</b><br>");
					} else {
						out.println("<b> Successfully created a " + userKind.toUpperCase() + ":</b><br>");
					}

					out.println("Username: " + userName + "<br>");
					out.println("Password: " + userPassword + "<br>");
					out.println(
							"<br><form action=\"login.jsp\"><input type=\"submit\" name=\"query\" value=\"OK, log in\"></form>");

				}
			}
			session.invalidate();
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
