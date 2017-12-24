package authors;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pl.kti.dbservlet.DBManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Servlet implementation class ArticleCreationServlet
 */
@WebServlet("/ArticleCreationServlet")
public class ArticleCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int userId = 0;
	private static String content = null;
	private Statement stmt = null;
	private int latestArticleId;
	public static int articleToReviewID;
	public static int latestReviewID;

	/**
	 * Default constructor.
	 */
	public ArticleCreationServlet() {

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
		if (session == null || session.getAttribute("id")==null) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();

				content = request.getParameter("content");
				userId = (int)session.getAttribute("id");

				stmt.executeUpdate("Insert into articles(AuthorId, Content) values(" + userId + ", '" + content
						+"');");
				
				ResultSet latestArticleIdSet = stmt.executeQuery("SELECT Id FROM articles ORDER BY Id DESC LIMIT 1");
				latestArticleIdSet.next();
				latestArticleId = latestArticleIdSet.getInt(1);
				stmt.executeUpdate("Insert into status(ArticleId) values(" + latestArticleId + ");");

				out.println("inserted following content:<br>");
				out.println(content);
				out.println("<br><form name=\"queryForm\" action=\"mainAuthor.jsp\" method=\"post\">\r\n"
						+ " <button type=\"submit\"> OK </button>\r\n" + " </form>");
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
