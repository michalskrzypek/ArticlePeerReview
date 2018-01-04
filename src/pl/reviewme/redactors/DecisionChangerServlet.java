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
 * Servlet implementation class DecisionChangerServlet
 */
@WebServlet("/DecisionChangerServlet")
public class DecisionChangerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Statement stmt = null;
	private static String query = "";
	private static String decision = "";
	private static String articleId = "";
	private static int redactorId;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DecisionChangerServlet() {
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
		if (session == null || session.getAttribute("id") == null) {
			out.print("Please login first!");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();
				redactorId = (int) session.getAttribute("id");
				articleId = request.getParameter("articleid");
				decision = request.getParameter("decision");
				
				query = "Insert into decisions(article_id, redactor_id, content) values("+articleId+", "+redactorId+", '"+decision+"')";
				stmt.executeUpdate(query);
				
				query = "update articles set decision='"+decision+"', status='Decided' where id="+articleId+";";
				stmt.executeUpdate(query);
				
				query = "update status set content='Decided' where article_id="+articleId+";";
				stmt.executeUpdate(query);
				
				stmt.close();
				
				out.println("You "+decision+" the article "+articleId+". <br><br><form action=\"mainRedactor.jsp\" method=\"post\"><input type=\"submit\" value=\"OK\"/></form>");
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