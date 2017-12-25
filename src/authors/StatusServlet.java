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
 * Servlet implementation class StatusServlet Servlet responsible for showing all of the statuses to user
 */
@WebServlet("/StatusServlet")
public class StatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Statement stmt = null;
	private String query = null;
	private int authorsId;
	private Object value = null;
	ArrayList<Integer> articleIDs = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatusServlet() {
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
		articleIDs = new ArrayList<Integer>();

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("id")==null) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();

				String req = request.getParameter("query");
				if (req.startsWith("Send")) {
					query = "Select Id from articles where author_id=" + authorsId + ";";
				} else if (req.startsWith("Update")) {

				} else {

					authorsId = (int)session.getAttribute("id");

					query = "Select Id from articles where author_id=" + authorsId + ";"; // getting a set of author's
																							// articles' Ids
					ResultSet queryResult = stmt.executeQuery(query);

					//adding IDs from every author's article
					while (queryResult.next()) {
						articleIDs.add(queryResult.getInt(1));
					}
					if (articleIDs.isEmpty()) {
						out.println("User: " + (String)session.getAttribute("username") + " has no articles yet!");
						out.println(
								"<br><form action=\"mainAuthor.jsp\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"OK\"></form>");

					} else {
						StringBuilder sb = new StringBuilder("select * from status where ");
						for (int i = 0; i < articleIDs.size(); i++) {

							if (i == articleIDs.size() - 1) {
								sb.append("article_id=" + articleIDs.get(i) + ";");
							} else {
								sb.append("article_id=" + articleIDs.get(i) + " or ");
							}

						}

						String query2 = sb.toString(); // getting every status with author's articles

						ResultSet query2Result = stmt.executeQuery(query2);
						ResultSetMetaData meta = query2Result.getMetaData();
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

						while (query2Result.next()) {

							out.println("<tr>");
							for (int col = 1; col <= colCount; col++) {
								value = query2Result.getObject(col);
								out.println("<td>");
								if (value != null) {
									out.println(value.toString());
								}
								out.println("</td>");
							}
							out.println(
									"<form action=\"ArticlesViewerServlet\" method=\"post\"><td>"
									+     "<input type=\"hidden\" name=\"articleid\" value=\""+query2Result.getInt(2)+"\">" 
									+ "<input type=\"submit\" name=\"query\" value=\"Show Article\"></td></form>");

							Object value = query2Result.getObject(1);

						}
						out.println("</table>");
						out.println(
								"<br><form action=\"mainAuthor.jsp\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"Go back\"></form>");
						query2Result.close();
						stmt.close();
					}
				}
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
