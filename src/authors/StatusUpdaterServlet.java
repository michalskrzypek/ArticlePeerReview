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
 * Servlet implementation class StatusServlet Servlet responsible for sending article to review
 */
@WebServlet("/StatusUpdaterServlet")
public class StatusUpdaterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Statement stmt = null;
	private String query = null;
	private int authorsId;
	private Object value = null;
	ArrayList<Integer> articleIDs = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatusUpdaterServlet() {
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
		if (session == null || session.getAttribute("id")==null) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();
				int index = Integer.parseInt(request.getParameter("articleid")); // id of article which is to be
																					// reviewed

				String query = "UPDATE articles\r\n" + "SET status = 'Waiting for a review'" + "WHERE Id=" + index
						+ ";";

				String query2 = "UPDATE status\r\n" + "SET content = 'Waiting for a review'" + "WHERE article_id="
						+ index + ";";

				stmt.executeUpdate(query);
				stmt.executeUpdate(query2);
				out.println("Article with ID " + index + "was sent to review successfully!");
				out.println(
						"<br><form action=\"ArticlesViewerServlet\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"OK\"></form>");

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
