package pl.reviewme.reviewers;

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
@WebServlet("/ArticleViewerServlet")
public class ArticleViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int authorsId = 0;
	private static String query = null;
	private Statement stmt = null;
	private static int currentArticleId;

	/**
	 * Default constructor.
	 */
	public ArticleViewerServlet() {

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
		if (session == null || session.getAttribute("id") == null) {
			out.print("Please login first!");
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

				String req = request.getParameter("query");
				authorsId = (int) session.getAttribute("id");

				int index = Integer.parseInt(request.getParameter("articleid"));
				query = "Select a.Id as 'Article ID', b.fname as 'First Name', b.lname as 'Last Name', a.title as 'Title', a.content as 'Article', a.status as 'Status', a.decision as 'Decision', final_mark as 'Final Mark' from articles a, authors b where a.Id=" + index + " and b.Id = a.author_id;";

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
					
					Object value = queryResult.getObject(1);

				}
				out.println("</table>");

						
				queryResult.close();
				stmt.close();
				out.println( "</section></div></body></html>");
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
