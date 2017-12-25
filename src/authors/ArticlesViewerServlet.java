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

	public void setCurrentArticleId(int id) {
		currentArticleId = id;
	}

	public int getCurrentArticleId() {
		return currentArticleId;
	}

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
				int index = 0;
				String req = request.getParameter("query");
				authorsId = (int)session.getAttribute("id");

				if (req.startsWith("Show Article")) {
					index = Integer.parseInt(request.getParameter("articleid"));
					query = "Select * from articles where Id=" + index + ";";
				} else {
					query = "Select * from articles where author_id=" + authorsId + ";";
				}
				ResultSet queryResult = stmt.executeQuery(query);
				
				if(!queryResult.next()) {
					out.println("You have no articles yet.");
				}else {
					
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
					if (queryResult.getString(5).equals("Draft")) {
						out.println(
								"<td><form action=\"UpdateArticleServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Modify Article\"></form></td>");
				
						out.println(
								"<br><td><form action=\"StatusUpdaterServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Send article to review\"></form></td>");
				
					} else if (queryResult.getString(5).equals("Reviewed") || queryResult.getString(5).equals("Decided")) {
						out.println(
								"<td><form action=\"ReviewsViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Show reviews\"></form></td>");
						}
					Object value = queryResult.getObject(1);

				}
				out.println("</table>");
					queryResult.close();
				}
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
