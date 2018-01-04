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

import pl.reviewme.access.DBManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Servlet implementation class ReviewsViewerServlet Servlet is used to show
 * reviewer every article that either is to be reviewed or is already reviewed
 * by the reviewer
 */
@WebServlet("/ArticlesToReviewViewer")
public class ArticlesToReviewViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Object value = null;
	private static int reviewerId = 0;
	private static String query = null;
	private Statement stmt = null;
	private Statement stmt2 = null;
	private static ArrayList<Integer> articlesToReview = null;

	/**
	 * Default constructor.
	 */
	public ArticlesToReviewViewer() {

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
				stmt2 = DBManager.getConnection().createStatement();

				articlesToReview = new ArrayList<Integer>();

				reviewerId = (int) session.getAttribute("id");

				// query = "select authors.fname as 'FIRST NAME' ,authors.lname AS 'LAST NAME',
				// articles.id as 'ARTICLE ID', articles.content as 'ARTICLE', articles.status
				// as 'STATUS', articles.decision as 'DECISION' from authors, articles where
				// articles.author_id = authors.id and articles.status = 'Waiting for a
				// review';";
				query = "select id from articles where status in('Waiting for a review')";

				ResultSet initResult = stmt.executeQuery(query);
				while(initResult.next()) {
					articlesToReview.add(initResult.getInt(1));
				}
				
				query ="select id from articles where status in('Reviewed')";
				ResultSet initResult2 = stmt.executeQuery(query);
				while(initResult2.next()) {
					int i = 0;
					query = "select reviewer_id from reviews where article_id="+initResult2.getInt(1)+";";
					initResult = stmt2.executeQuery(query);
					while(initResult.next()) {
						if(initResult.getInt(1)==reviewerId) {
						i++;
					}
					}
					if(i==0) {
						articlesToReview.add(initResult2.getInt(1));
					}
				}
				
				Collections.sort(articlesToReview);
				StringBuilder sb = new StringBuilder("select * from articles where ");
				for(int i =0;i<articlesToReview.size();i++) {
					if(i!=articlesToReview.size()-1) {
						sb.append("id="+articlesToReview.get(i)+" or ");
					}else {
						sb.append("id="+articlesToReview.get(i)+";");
					}
					
				}
				query = sb.toString();
				
				if(articlesToReview.isEmpty()) {
					out.println("There are no articles to review!");
				}else {
				
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

						out.println(
								"<td><form action=\"ReviewCreationServlet\" method=\"post\"><input type=\"hidden\" name=\"articleid\" value=\""
										+ queryResult.getString(1)
										+ "\"/><input type=\"submit\" name=\"query\" value=\"Create a review\"></form></td>");
					

					Object value = queryResult.getObject(1);
				}
					out.println("</table>");
				}
					out.println(
							"<br><form action=\"mainReviewer.jsp\" method=\"post\"><input type=\"submit\" name=\"query\" value=\"Go back\"></form>");
				//	queryResult.close();
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
