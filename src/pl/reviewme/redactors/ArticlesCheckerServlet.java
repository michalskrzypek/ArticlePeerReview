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

import pl.reviewme.controller.DBManager;

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
				out.println(
						"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n"
								+ "<html lang=\"en\">\r\n" + "<head>\r\n" + "  <meta charset=\"UTF-8\">\r\n"
								+ "  <title>ReviewMe®</title>\r\n" + "  \r\n" + "  \r\n" + "  \r\n"
								+ "      <link rel=\"stylesheet\" href=\"css/style.css\">\r\n" + "\r\n" + "  \r\n"
								+ "</head>\r\n" + "\r\n" + "<body><div class=\"response_container\">\r\n"
								+ "	<section id=\"response_content\">");

				
				query = "Select articles.Id as 'Article ID', articles.author_id as 'Author ID' , b.fname as 'First Name', b.lname as 'Last Name', articles.title as 'Title', articles.content as 'Article', articles.status as 'Status', articles.decision as 'Decision', articles.final_mark as 'Final Mark' from articles, authors b where status in('Reviewed') and b.Id = articles.author_id";

				
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
					if (col != 2 && col != 7) {
						out.println("<th>");
						out.println(meta.getColumnLabel(col));
						out.println("</th>");
					}
				}

				out.println("</tr>");

				while (queryResult.next()) {

					out.println("<tr>");
					for (int col = 1; col <= colCount; col++) {
						if (col != 2 && col != 7) {	
							value = queryResult.getObject(col);
							out.println("<td>");
							if (value != null) {
								out.println(value.toString());
							}
							out.println("</td>");
					
					
					}
					}
						
						out.println(
									"<td><form action=\"ReviewsToArticleViewerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Show reviews\"></form></td>");
					
						
						out.println(
								"<td><form action=\"DecisionMakerServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""+queryResult.getString(1)+"\"/><input type=\"submit\" name=\"query\" value=\"Decide\"></form></td>");
						
					
				
				}
					
				}
				
				out.println("</table>");
				out.println(
						"<form action=\"mainRedactor.jsp\" method=\"post\"><input class=\"button_1\" type=\"submit\" name=\"query\" value=\"Main Page\"></form>");
					
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}