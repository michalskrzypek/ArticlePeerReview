package redactors;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pl.kti.dbservlet.DBManager;

/**
 * Servlet implementation class ArticlesCheckerServlet
 */
@WebServlet("/ArticlesCheckerServlet")
public class ArticlesCheckerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Statement stmt = null;
    private static String query = "";
    private static Object value = null;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArticlesCheckerServlet() {
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
		if (session == null || session.getAttribute("id")==null) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();
				query = "select * from articles where status in('Reviewed')";

				
				ResultSet queryResult = stmt.executeQuery(query);
				
				if(!queryResult.next()) {
					out.println("There is no ready articles!");
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
					if (queryResult.getString(5).equals("Reviewed")) {
						out.println(
								"<td><form action=\"ReviewsToArticleViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Show reviews\"></form></td>");
						}else {
							out.println(
									"<td></td>");
							
						}
					
					out.println(
							"<td><form action=\"DecisionMakerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Decide\"></form></td>");
					
					
					Object value = queryResult.getObject(1);

				}
				out.println("</table>");
			/*	if(index!=0) {
					out.println(
							"<br><form action=\"StatusServlet\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"OK\"></form>");
				}else {
					out.println(
							"<br><form action=\"mainAuthor.jsp\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"OK\"></form>");
				}*/
				
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