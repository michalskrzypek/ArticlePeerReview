package authors;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.glass.ui.Timer;

import pl.kti.dbservlet.DBManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class ArticleServlet
 */
@WebServlet("/ArticlesViewerServlet")
public class ArticlesViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int authorsId = 0;
	private static String query = null;
	private Statement stmt = null;
	private static ResultSet queryResult = null;
	private static ResultSetMetaData meta = null;
	private static int currentArticleId;

	/**
	 * Default constructor.
	 */
	public ArticlesViewerServlet() {

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
		String userkind = (String) session.getAttribute("userkind");
		if (session == null || !(userkind.equalsIgnoreCase("author"))) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();
				int index = 0;
				String req = request.getParameter("query");
				authorsId = (int) session.getAttribute("id");

				// Displaying draft articles
				query = "Select * from articles where author_id=" + authorsId + " and status = 'Draft';";

				queryResult = stmt.executeQuery(query);

				if (!queryResult.next()) {
					out.println("You have no drafts.<br><br>");
				} else {

					queryResult = stmt.executeQuery(query);
					ResultSetMetaData meta = queryResult.getMetaData();
					int colCount = meta.getColumnCount();
					out.println("Drafts:");
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
								"<td><form action=\"UpdateArticleServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
										+ queryResult.getString(1)
										+ "\"/><input type=\"submit\" name=\"query\" value=\"Modify Article\"></form></td>");

						out.println(
								"<br><td><form action=\"StatusUpdaterServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
										+ queryResult.getString(1)
										+ "\"/><input type=\"submit\" name=\"query\" value=\"Send article to review\"></form></td>");

						Object value = queryResult.getObject(1);

					}
					out.println("</table><br><br>");
				}

				// Displaying waiting-for-a-review articles
				query = "Select * from articles where author_id=" + authorsId + " and status = 'Waiting for a review';";

				queryResult = stmt.executeQuery(query);

				if (!queryResult.next()) {
					out.println("You have no articles waiting for a review.<br><br>");
				} else {

					queryResult = stmt.executeQuery(query);
					meta = queryResult.getMetaData();
					int colCount = meta.getColumnCount();
					out.println("Waiting for a review articles:<br>");
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

						Object value = queryResult.getObject(1);

					}
					out.println("</table><br><br>");
				}

				// Displaying already reviewed articles
				query = "Select * from articles where author_id=" + authorsId + " and status = 'Reviewed';";

				queryResult = stmt.executeQuery(query);

				if (!queryResult.next()) {
					out.println("You have no reviewed articles.<br><br>");
				} else {

					queryResult = stmt.executeQuery(query);
					meta = queryResult.getMetaData();
					int colCount = meta.getColumnCount();
					out.println("Already reviewed articles:<br>");
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
								"<td><form action=\"ReviewsViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
										+ queryResult.getString(1)
										+ "\"/><input type=\"submit\" name=\"query\" value=\"Show reviews\"></form></td>");
					}

				}
				out.println("</table><br><br>");

				// Displaying already decided articles
				query = "Select * from articles where author_id=" + authorsId + " and status = 'Decided';";

				queryResult = stmt.executeQuery(query);

				if (!queryResult.next()) {
					out.println("You have no already decided articles.<br><br>");
				} else {

					queryResult = stmt.executeQuery(query);
					meta = queryResult.getMetaData();
					int colCount = meta.getColumnCount();
					out.println("Articles with decision:<br>");
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
						if(queryResult.getString(6).equals("Next Round")) {
							out.println(
									"<td><form action=\"UpdateArticleServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
											+ queryResult.getString(1)
											+ "\"/><input type=\"submit\" name=\"query\" value=\"Modify Article\"></form></td>");

						}
						out.println(
								"<td><form action=\"ReviewsViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
										+ queryResult.getString(1)
										+ "\"/><input type=\"submit\" name=\"query\" value=\"Show reviews\"></form></td>");
					}

				}
				out.println("</table><br><br>");

				out.println(
						"<td><form action=\"mainAuthor.jsp\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"Go to the main page\"></form></td>");
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