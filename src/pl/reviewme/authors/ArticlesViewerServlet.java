package pl.reviewme.authors;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.glass.ui.Timer;

import pl.reviewme.controller.DBManager;

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
		if (session == null || session.getAttribute("userkind") == null) {
			out.print("<div id=\"message\"><p>Please login first!</p></div>");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			String userkind = (String) session.getAttribute("userkind");
			if (!userkind.equalsIgnoreCase("author")) {
				out.print("<div id=\"message\"><p>Please login as an author to see this content.</p></div>");
				request.getRequestDispatcher("login.jsp").include(request, response);
			} else {

				try {
					Class.forName("com.mysql.jdbc.Driver");
					stmt = DBManager.getConnection().createStatement();
					out.println(
							"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n"
									+ "<html lang=\"en\">\r\n" + "<head>\r\n" + "  <meta charset=\"UTF-8\">\r\n"
									+ "  <title>ReviewMe®</title>\r\n" + "  \r\n" + "  \r\n" + "  \r\n"
									+ "      <link rel=\"stylesheet\" href=\"css/style.css\">\r\n" + "\r\n" + "  \r\n"
									+ "</head>\r\n" + "\r\n" + "<body><div class=\"response_container\">\r\n"
									+ "	<section id=\"response_content\">");

					authorsId = (int) session.getAttribute("id");

					if(request.getParameter("articleid") !=null) {
						int articleId = Integer.parseInt(request.getParameter("articleid"));
						query = "Select Id as 'ID', author_id, title as 'Title', content as 'Article', status as 'Status', decision as 'Decision' from articles where id="
								+ articleId + ";";
						
						queryResult = stmt.executeQuery(query);
						ResultSetMetaData meta = queryResult.getMetaData();
						int colCount = meta.getColumnCount();

						out.println("<table>");

						// header row:
						out.println("<tr>");
						for (int col = 1; col <= colCount; col++) {
							if (col != 2) {
								out.println("<th>");
								out.println(meta.getColumnLabel(col));
								out.println("</th>");
							}
						}

						out.println("</tr>");

						while (queryResult.next()) {

							out.println("<tr>");
							for (int col = 1; col <= colCount; col++) {
								if (col != 2) {
									value = queryResult.getObject(col);
									out.println("<td>");
									if (value != null) {
										out.println(value.toString());
									}
									out.println("</td>");
								}
							}

						}
						out.println("</table><br><br>");
					}else {
					
					// Displaying draft articles
						query = "Select Id as 'ID', author_id, title as 'Title', content as 'Article', status as 'Status', decision as 'Decision' from articles where author_id="
							+ authorsId + " and status = 'Draft';";

					queryResult = stmt.executeQuery(query);

					out.println("<form><h1>Drafts:</h1></form>");

					if (!queryResult.next()) {
						out.println("You have no drafts.<br><br>");
					} else {

						queryResult = stmt.executeQuery(query);
						ResultSetMetaData meta = queryResult.getMetaData();
						int colCount = meta.getColumnCount();

						out.println("<table>");

						// header row:
						out.println("<tr>");
						for (int col = 1; col <= colCount; col++) {
							if (col != 2) {
								out.println("<th>");
								out.println(meta.getColumnLabel(col));
								out.println("</th>");
							}
						}

						out.println("</tr>");

						while (queryResult.next()) {

							out.println("<tr>");
							for (int col = 1; col <= colCount; col++) {
								if (col != 2) {
									value = queryResult.getObject(col);
									out.println("<td>");
									if (value != null) {
										out.println(value.toString());
									}
									out.println("</td>");
								}
							}

							out.println(
									"<td><form action=\"UpdateArticleServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
											+ queryResult.getString(1)
											+ "\"/><input class=\"button_3\" type=\"submit\" name=\"query\" value=\"Modify Article\"></form></td>");

							out.println(
									"<td><form action=\"StatusUpdaterServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
											+ queryResult.getString(1)
											+ "\"/><input class=\"button_3\" type=\"submit\" name=\"query\" value=\"Send To Review\"></form></td>");

							Object value = queryResult.getObject(1);

						}
						out.println("</table><br><br>");
					}

					// Displaying waiting-for-a-review articles
					query = "Select Id as 'ID', author_id, title as 'Title', content as 'Article', status as 'Status', decision as 'Decision' from articles where author_id="
							+ authorsId + " and status = 'Waiting for a review';";

					queryResult = stmt.executeQuery(query);
					out.println("<form><h1>Waiting for reviews:</form></h1>");
					if (!queryResult.next()) {
						out.println("You have no articles waiting for a review.<br><br>");
					} else {

						queryResult = stmt.executeQuery(query);
						meta = queryResult.getMetaData();
						int colCount = meta.getColumnCount();

						out.println("<table>");

						// header row:
						out.println("<tr>");
						for (int col = 1; col <= colCount; col++) {
							if (col != 2) {
								out.println("<th>");
								out.println(meta.getColumnLabel(col));
								out.println("</th>");
							}
						}

						out.println("</tr>");

						while (queryResult.next()) {

							out.println("<tr>");
							for (int col = 1; col <= colCount; col++) {
								if (col != 2) {
									value = queryResult.getObject(col);
									out.println("<td>");
									if (value != null) {
										out.println(value.toString());
									}
									out.println("</td>");
								}
							}

							Object value = queryResult.getObject(1);

						}
						out.println("</table><br><br>");
					}

					// Displaying already reviewed articles
					query = "Select Id as 'ID', author_id, title as 'Title', content as 'Article', status as 'Status', decision as 'Decision', final_mark as 'Final Mark'  from articles where author_id="
							+ authorsId + " and status = 'Reviewed';";

					queryResult = stmt.executeQuery(query);

					out.println("<form><h1>Waiting for a decision:</form></h1>");
					if (!queryResult.next()) {
						out.println("You have no reviewed articles.<br><br>");
					} else {

						queryResult = stmt.executeQuery(query);
						meta = queryResult.getMetaData();
						int colCount = meta.getColumnCount();

						out.println("<table>");

						// header row:
						out.println("<tr>");
						for (int col = 1; col <= colCount; col++) {
							if (col != 2) {
								out.println("<th>");
								out.println(meta.getColumnLabel(col));
								out.println("</th>");
							}
						}

						out.println("</tr>");

						while (queryResult.next()) {

							out.println("<tr>");
							for (int col = 1; col <= colCount; col++) {
								if (col != 2) {
									value = queryResult.getObject(col);
									out.println("<td>");
									if (value != null) {
										out.println(value.toString());
									}
									out.println("</td>");
								}
							}

							out.println(
									"<td><form action=\"ReviewsViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
											+ queryResult.getString(1)
											+ "\"/><input class=\"button_3\" type=\"submit\" name=\"query\" value=\"Show Reviews\"></form></td>");
						}

					}
					out.println("</table><br><br>");

					// Displaying already decided articles
					query = "Select Id as 'ID', author_id, title as 'Title', content as 'Article', status as 'Status', decision as 'Decision', final_mark as 'Final Mark' from articles where author_id="
							+ authorsId + " and status = 'Decided';";

					queryResult = stmt.executeQuery(query);

					out.println("<form><h1>Articles with decision:</form></h1>");

					if (!queryResult.next()) {
						out.println("You have no already decided articles.<br><br>");
					} else {

						queryResult = stmt.executeQuery(query);
						meta = queryResult.getMetaData();
						int colCount = meta.getColumnCount();

						out.println("<table>");

						// header row:
						out.println("<tr>");
						for (int col = 1; col <= colCount; col++) {
							if (col != 2) {
								out.println("<th>");
								out.println(meta.getColumnLabel(col));
								out.println("</th>");
							}
						}

						out.println("</tr>");
						while (queryResult.next()) {

							out.println("<tr>");
							for (int col = 1; col <= colCount; col++) {
								if (col != 2) {
								value = queryResult.getObject(col);
								out.println("<td>");
								if (value != null) {
									out.println(value.toString());
								}
								out.println("</td>");
								}
							}

							if (queryResult.getString(6).equals("Next Round")) {
								out.println(
										"<td><form action=\"UpdateArticleServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
												+ queryResult.getString(1)
												+ "\"/><input class=\"button_3\" type=\"submit\" name=\"query\" value=\"Modify Article\"></form></td>");

							}
							out.println(
									"<td><form action=\"ReviewsViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
											+ queryResult.getString(1)
											+ "\"/><input class=\"button_3\" type=\"submit\" name=\"query\" value=\"Show Reviews\"></form></td>");
						}

					}
					out.println("</table><br><br>");

					out.println(
					"<form action=\"mainAuthor.jsp\" method=\"post\"><input class=\"button_1\" type=\"submit\" name=\"query\" value=\"Main Page\"></form>");
					
					}
					queryResult.close();

					stmt.close();
					out.println("</section></div></body></html>");

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