package pl.kti.dbservlet;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Utilities {

	public static void drawTable(Statement stmt, ResultSet queryResult, String query, PrintWriter out) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Object value = null;
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
				value = queryResult.getObject(1);

			}
			out.println("</table>");

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
