package murach.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/sqlGateway" })
@SuppressWarnings("serial")
public class SqlGatewayServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		String sqlStatement = req.getParameter("sqlStatement");
		String sqlResult = "";

		// load the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// get a connection
			//String dbURL = "jdbc:mysql://localhost:3306/murach";
			//String username = "root";
			//String password = "1234";
			
			String dbURL = System.getenv("DATASOURCE_URL");
			String username = System.getenv("DATASOURCE_USERNAME");
			String password = System.getenv("DATASOURCE_PASSWORD");
			
			Connection connection = DriverManager.getConnection(dbURL, username, password);

			// create statement
			Statement statement = connection.createStatement();

			// parse the SQL string
			sqlStatement = sqlStatement.trim();
			if (sqlStatement.length() >= 6) {
				String sqlType = sqlStatement.substring(0, 6);
				if (sqlType.equalsIgnoreCase("select")) {
					// create the HTML for the result set
					ResultSet resultSet = statement.executeQuery(sqlStatement);
					sqlResult = SQLUtil.getHtmlTable(resultSet);
					resultSet.close();
				} else {
					int i = statement.executeUpdate(sqlStatement);
					if (i == 0) { // a DDL statement
						sqlResult = "<p>The statement executed successfully.</p>";
					} else { // an INSERT, UPDATE, or DELETE statement} else {
						sqlResult = "<p>The statement executed successfully.<br>" + i + " row(s) affected.</p>";
					}
				}
			}
			statement.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			sqlResult = "<p>Error loading the database driver: <br>" + e.getMessage() + "</p>";

		} catch (SQLException e) {
			sqlResult = "<p>Error executing the SQL statement: <br>" + e.getMessage() + "</p>";
		}
		HttpSession session = req.getSession();
		session.setAttribute("sqlResult", sqlResult);
		session.setAttribute("sqlStatement", sqlStatement);

		String url = "/index.jsp";
		getServletContext().getRequestDispatcher(url).forward(req, resp);
	}
}
