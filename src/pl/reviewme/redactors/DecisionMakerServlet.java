package pl.reviewme.redactors;

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

import pl.reviewme.access.DBManager;

/**
 * Servlet implementation class DecisionMakerServlet
 */
@WebServlet("/DecisionMakerServlet")
public class DecisionMakerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Statement stmt = null;
    private static String query = "";
    private static Object value = null;
	private static String articleId = "";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DecisionMakerServlet() {
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
				articleId = request.getParameter("articleid");
				
				query = "select * from articles where id="+articleId+";";

				
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

				out.println("<br><br><form action=\"DecisionChangerServlet\" method=\"post\">\r\n" + 
						"  <select name=\"decision\">\r\n" + 
						"    <option value=\"NotDecidedYet\">Not Decided Yet</option>\r\n" + 
						"    <option value=\"Accepted\">Accepted</option>\r\n" + 
						"    <option value=\"Declined\">Declined</option>\r\n" + 
						"    <option value=\"Next Round\">Next Round</option>\r\n" + 
						"  </select>\r\n" + 
						"  <br><br>\r\n" + 
						"<input type=\"hidden\" name=\"articleid\" value=\""+articleId+"\"/>"+
						"  <input type=\"submit\" value=\"Submit\">\r\n" + 
						"</form>");
				
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