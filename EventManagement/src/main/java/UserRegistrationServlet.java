import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {
	// JDBC URL, username, and password of MySQL server
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/EventDB";
	private static final String JDBC_USER = "EventDBA";
	private static final String JDBC_PASSWORD = "CST8288";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		Connection connection = null;

		try {
			// Establish a connection to the database
			connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

			// SQL insert statement to add a new user to the database
			String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);

			// Execute the statement to insert the user
			preparedStatement.executeUpdate();

			// Redirect to a registration success page or login page
			response.sendRedirect("registration_success.html");
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle database errors and redirect to an error page
			response.sendRedirect("registration_error.html");
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
