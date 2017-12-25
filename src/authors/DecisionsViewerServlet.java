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
 * Servlet implementation class DecisionsViewerServlet
 */
@WebServlet("/DecisionsViewerServlet")
public class DecisionsViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Statement stmt = null;
	private static int authorId;
	private static String query = null;
private static ArrayList<Integer> articleIDs;
private static Object value = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DecisionsViewerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
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

				authorId = (int) session.getAttribute("id");
				
				query = "select id as ARTICLE_ID, decision AS DECISION from articles where author_id="+authorId+" and decision in('Accepted');";

				ResultSet queryResult = stmt.executeQuery(query);
				if(!queryResult.next()) {
					out.println("There is no accepted articles.<br><br>");
				}else {
				queryResult = stmt.executeQuery(query);
				ResultSetMetaData meta = queryResult.getMetaData();
				int colCount = meta.getColumnCount();
				out.println("Accepted Articles:");
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
							"<form action=\"ArticlesViewerServlet\" method=\"post\"><td>"
							+     "<input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getInt(1)+"\">" 
							+ "<input type=\"submit\" name=\"query\" value=\"Show Article\"></td></form>");
					value = queryResult.getObject(1);

				}
				out.println("</table><br><br>");
				}
				
				query = "select id as ARTICLE_ID, decision AS DECISION from articles where author_id="+authorId+" and decision in('Declined');";

				queryResult = stmt.executeQuery(query);
				if(!queryResult.next()) {
					out.println("There is no declined articles.<br><br>");
				}else {
				queryResult = stmt.executeQuery(query);
				ResultSetMetaData meta = queryResult.getMetaData();
				int colCount = meta.getColumnCount();
				out.println("Declined Articles:");
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
							"<form action=\"ArticlesViewerServlet\" method=\"post\"><td>"
							+     "<input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getInt(1)+"\">" 
							+ "<input type=\"submit\" name=\"query\" value=\"Show Article\"></td></form>");
					value = queryResult.getObject(1);

				}
				out.println("</table><br><br>");
				}
				
				query = "select id as ARTICLE_ID, decision AS DECISION from articles where author_id="+authorId+" and decision in('Next Round');";

				queryResult = stmt.executeQuery(query);
				if(!queryResult.next()) {
					out.println("There is no next-rounded articles.<br><br>");
				}else {
				queryResult = stmt.executeQuery(query);
				ResultSetMetaData meta = queryResult.getMetaData();
				int colCount = meta.getColumnCount();
				out.println("Next-rounded Articles:");
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
							"<form action=\"ArticlesViewerServlet\" method=\"post\"><td>"
							+     "<input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getInt(1)+"\">" 
							+ "<input type=\"submit\" name=\"query\" value=\"Show Article\"></td></form>");
					
					out.println(
							"<td><form action=\"UpdateArticleServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
									+ queryResult.getString(1)
									+ "\"/><input type=\"submit\" name=\"query\" value=\"Modify Article\"></form></td>");

				
					value = queryResult.getObject(1);

				}
				out.println("</table>");
				}
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
