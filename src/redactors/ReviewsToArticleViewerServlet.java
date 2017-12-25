package redactors;

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

import pl.kti.dbservlet.DBManager;

/**
 * Servlet implementation class ReviewsToArticleViewerServlet
 */
@WebServlet("/ReviewsToArticleViewerServlet")
public class ReviewsToArticleViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int authorId = 0;
	private static int reviewerId = 0;
	private static String query = null;
	private Statement stmt = null;
	private static String articleId;
	ArrayList<Integer> articleIDs = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewsToArticleViewerServlet() {
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
		
		if (session == null ||session.getAttribute("id") == null) {
			out.print("Please login as a redactor!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();
				articleId = request.getParameter("articleid");

				query = "select * from reviews where article_id=" + articleId + ";";

				ResultSet queryResult = stmt.executeQuery(query);

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
					value = queryResult.getObject(1);

				}
				out.println("</table>");
				queryResult.close();
				stmt.close();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
