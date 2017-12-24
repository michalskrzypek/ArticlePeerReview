package authors;

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
 * Servlet implementation class ReviewsServlet Used to show reviews to every
 * author's article or to show all of the reviews to particular article or
 */
@WebServlet("/ReviewsViewerServlet")
public class ReviewsViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int authorId = 0;
	private static int reviewerId = 0;
	private static String query = null;
	private Statement stmt = null;
	private static int articleId;
	ArrayList<Integer> articleIDs = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewsViewerServlet() {
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
		if (session == null || session.getAttribute("id") == null) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();

				if (request.getParameter("articleid") != null) {
					articleId = Integer.parseInt(request.getParameter("articleid"));
				}

				authorId = (int) session.getAttribute("id");

				if (articleId != 0) {
					query = "select * from reviews where articleid=" + articleId + ";";
				} else {
					query = "Select Id from articles where AuthorId=" + authorId + ";"; // getting a set of author's
																						// articles' Ids
					ResultSet queryResult = stmt.executeQuery(query);

					articleIDs = new ArrayList<Integer>();
					// adding IDs from every author's article
					while (queryResult.next()) {
						articleIDs.add(queryResult.getInt(1));
					}

					if (articleIDs.isEmpty()) {
						out.println("User: " + (String) session.getAttribute("username") + " has no articles yet!");
						out.println(
								"<br><form action=\"mainAuthor.jsp\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"OK\"></form>");

					} else {
						StringBuilder sb = new StringBuilder("select * from reviews where ");
						for (int i = 0; i < articleIDs.size(); i++) {

							if (i == articleIDs.size() - 1) {
								sb.append("ArticleId=" + articleIDs.get(i) + ";");
							} else {
								sb.append("ArticleId=" + articleIDs.get(i) + " or ");
							}

						}

						query = sb.toString();
					}

				}

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
					value = queryResult.getObject(1);

				}
				out.println("</table>");
				articleId = 0;
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
