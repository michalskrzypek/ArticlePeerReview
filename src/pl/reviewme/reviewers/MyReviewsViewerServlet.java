package pl.reviewme.reviewers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pl.reviewme.access.DBManager;

/**
 * Servlet implementation class ReviewViewerServlet used to
 * show a reviewer's review to particular article
 */
@WebServlet("/MyReviewsViewerServlet")
public class MyReviewsViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int reviewerId = 0;
	private static String query = null;
	private Statement stmt = null;
	private static int articleId;
	ArrayList<Integer> articleIDs = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyReviewsViewerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userkind") == null) {
			out.print("<div id=\"message\"><p>Please login first!</p></div>");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			String userkind = (String) session.getAttribute("userkind");
			if(!userkind.equalsIgnoreCase("reviewer")) {
			out.print("<div id=\"message\"><p>Please login as a reviewer to see this content.</p></div>");
			request.getRequestDispatcher("login.jsp").include(request, response);
			}else {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();
				out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
						"<html lang=\"en\">\r\n" + 
						"<head>\r\n" + 
						"  <meta charset=\"UTF-8\">\r\n" + 
						"  <title>ReviewMe®</title>\r\n" + 
						"  \r\n" + 
						"  \r\n" + 
						"  \r\n" + 
						"      <link rel=\"stylesheet\" href=\"css/output_style.css\">\r\n" + 
						"\r\n" + 
						"  \r\n" + 
						"</head>\r\n" + 
						"\r\n" + 
						"<body><div class=\"container\">\r\n" + 
						"	<section id=\"content\" style=\"width:auto\">");
				reviewerId = (int)session.getAttribute("id");
				query = "select id as 'ID',article_id 	as 'Article ID', reviewer_id as 'Reviewer ID',mark as 'Mark',content as 'Review' from reviews where reviewer_id=" + reviewerId + ";";
				
				ResultSet queryResult = stmt.executeQuery(query);
				queryResult = stmt.executeQuery(query);
				ResultSetMetaData meta = queryResult.getMetaData();
				int colCount = meta.getColumnCount();
				out.println("<table border=\"1\">");

				// header row:
				out.println("<tr>");
				for (int col = 1; col <= colCount; col++) {
					
						out.println("<th>");
						out.println(meta.getColumnLabel(col));
						out.println("</th>");
					
				}

				out.println("</tr>");

				while (queryResult.next()) {

					out.println("<tr>");
					for (int col = 1; col <= colCount; col++) {

							value = queryResult.getObject(col);
							out.println("<td>");
							if (value != null) {
								out.println(value.toString());
							}
							out.println("</td>");

					}
					
					out.println(
							"<td><form action=\"ArticleViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(2)+"\"/><button id=\"action\" type=\"submit\" name=\"query\">Show Article</button></form></td>");
					value = queryResult.getObject(1);

				}
				out.println("</table>");
				out.println(
						"<br><form action=\"mainReviewer.jsp\" method=\"post\"><button id=\"logout\" type=\"submit\" name=\"query\">Main Page</button></form>");
				articleId = 0;
				queryResult.close();
				stmt.close();
				out.println( "</section></div></body></html>");

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}}
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
