package pl.reviewme.authors;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pl.reviewme.controller.DBManager;

/**
 * Servlet implementation class UpdateArticleServlet
 */
@WebServlet("/UpdateArticleServlet")
public class UpdateArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int articleId;
	private static String title = null;
	private static String currentTitle = null;
	private static String content = null;
	private static String currentContent = null;
	private Statement stmt = null;
	public static int articleToReviewID;
	public static int latestReviewID;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateArticleServlet() {
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
		if (session == null || session.getAttribute("userkind") == null) {
			out.print("<div id=\"message\"><p>Please login first!</p></div>");
			request.getRequestDispatcher("login.jsp").include(request, response);
		} else {
			String userkind = (String) session.getAttribute("userkind");
			if(!userkind.equalsIgnoreCase("author")) {
			out.print("<div id=\"message\"><p>Please login as an author to see this content.</p></div>");
			request.getRequestDispatcher("login.jsp").include(request, response);
			}else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				stmt = DBManager.getConnection().createStatement();

				if (request.getParameter("articleid") != null) {
					articleId = Integer.parseInt(request.getParameter("articleid"));
					
					ResultSet articleResult = stmt.executeQuery("select title, content from articles where id="+articleId+";");
					articleResult.next();
					currentTitle = articleResult.getString(1);
					currentContent = articleResult.getString(2);
					
					session.setAttribute("title", currentTitle);
					session.setAttribute("content", currentContent);
					
					response.sendRedirect("modifyArticle.jsp");
				} else {
					title = request.getParameter("title");
					content = request.getParameter("content");

					stmt.executeUpdate("UPDATE articles\r\n" + "SET title = '" + title + "', content = '" + content
							+ "', status = 'Draft', decision='Not Decided Yet' \r\n" + "WHERE id = " + articleId + ";");

					stmt.executeUpdate("update status set content='Draft' where article_id="+articleId+";");
					stmt.executeUpdate("update articles set final_mark = NULL where id="+articleId+";");
					stmt.executeUpdate("delete from reviews where article_id="+articleId+";");
					stmt.executeUpdate("delete from decisions where article_id="+articleId+";");
					
					
					out.println("<div id=\"message\"> <p>Article "+String.valueOf(articleId)+" has been modified!</p></div>");
				RequestDispatcher rd = request.getRequestDispatcher("ArticlesViewerServlet");
				rd.include(request, response);
				
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
