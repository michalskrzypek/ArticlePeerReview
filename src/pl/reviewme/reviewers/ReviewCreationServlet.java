package pl.reviewme.reviewers;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pl.reviewme.access.DBManager;

/**
 * Servlet implementation class ReviewCreationServlet
 */
@WebServlet("/ReviewCreationServlet")
public class ReviewCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int userId = 0;
	private static String content = null;
	private static String mark = null;
	private Statement stmt = null;
	public static int articleToReviewID = 0;
	public static int latestReviewID;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewCreationServlet() {
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

			if (request.getParameter("articleid") != null) {
				articleToReviewID = Integer.parseInt((String)request.getParameter("articleid"));
				response.sendRedirect("createReview.jsp");
			} else {
				content = request.getParameter("content"); // content of a review from textarea
				mark = request.getParameter("mark"); // mark from radiobutton
				userId = (int)session.getAttribute("id"); // reviewer id

				stmt.executeUpdate("Insert into reviews(ARTICLE_ID, REVIEWER_ID, MARK, CONTENT) values("
						+ articleToReviewID + ", " + userId + ", " + mark + ",'" + content + "');");

				stmt.executeUpdate("update articles set status = 'Reviewed' where id=" + articleToReviewID + ";");
				stmt.executeUpdate("update status set content= 'Reviewed' where article_id=" + articleToReviewID + ";");

				ResultSet marksResult = stmt
						.executeQuery("select mark from reviews where article_id=" + articleToReviewID + ";");
				ArrayList<Integer> marks = new ArrayList<Integer>();
				double marksSum = 0;
				
				while(marksResult.next()) {
					int mark = marksResult.getInt(1);
					marks.add(mark);
				}
				
				for (int i =0;i<marks.size();i++) {
					marksSum += (double)marks.get(i);
				}
			
				double finalMark = marksSum/(double)marks.size();
				String finalMarkString = String.format("%.2f", finalMark);
				
				stmt.executeUpdate("update articles set final_mark='"+finalMarkString+"' where id="+articleToReviewID+";");
				
				out.println("Article with ID: " + articleToReviewID + " was reviewed");
				out.println("<form name=\"queryForm\" action=\"ArticlesToReviewViewer\" method=\"post\">\r\n"
						+ " <button type=\"submit\"> OK </button>\r\n" + " </form>");

				articleToReviewID = 0;
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
	}}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
