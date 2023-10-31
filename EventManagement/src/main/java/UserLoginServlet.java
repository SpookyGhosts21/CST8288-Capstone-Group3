import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
	// JDBC URL, username, and password of MySQL server
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/EventDB";
	private static final String JDBC_USER = "EventDBA";
	private static final String JDBC_PASSWORD = "CST8288";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		Connection connection = null;

		try {
			// Establish a connection to the database
			connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

			// SQL query to retrieve user data based on the email
			String sql = "SELECT * FROM users WHERE email = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);

			// Execute the query and retrieve the user data
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String storedPassword = resultSet.getString("password");

				if (password.equals(storedPassword)) {
					// Passwords match; the user is authenticated
					HttpSession session = request.getSession();
					session.setAttribute("email", email);

					// Redirect to the user's dashboard or another secured page
					response.sendRedirect("dashboard.jsp");
				} else {
					// Passwords do not match; redirect to login page with an error message
					response.sendRedirect("login.jsp?error=invalid_password");
				}
			} else {
				// User not found; redirect to login page with an error message
				response.sendRedirect("login.jsp?error=user_not_found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle database errors and redirect to an error page
			response.sendRedirect("login_error.html");
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
