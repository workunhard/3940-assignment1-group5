import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>\n" + "<head><title>" + "Login" + "</title><link rel=\"stylesheet\" type=\"text/css\""
				+ "href=\"css/login.css\"</head>\n" + "<body>\n"
				+ "<h1 align=\"center\">" + "Login" + "</h1>\n" + "<form action=\"login\" method=\"POST\">\n"
				+ "Username: <input type=\"text\" name=\"user_name\">\n" + "<br />\n"
				+ "Password: <input type=\"password\" name=\"password\" />\n" + "<br />\n"
				+ "<input type=\"submit\" value=\"Sign in\" />\n" + "</form>\n"
				+ "</form>\n" + "<form action=\"signup\" method=\"GET\">\n" +
				"<input type=\"submit\" value=\"SignUp\" />\n"+ "</body>\n</html\n");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		final String URL = "jdbc:mysql://localhost:3306/test";
		final Properties connectionProperties = new Properties();
		connectionProperties.put("user", "root");
		connectionProperties.put("password", "Popcorn");
		try {
			Connection con = DriverManager.getConnection(URL, connectionProperties);
			Statement searchDB = con.createStatement();
			ResultSet rs = searchDB.executeQuery("SELECT * FROM users");
			String username = request.getParameter("user_name");
			String password = request.getParameter("password");
			while(rs.next()) {
				if(rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
					HttpSession session = request.getSession(true);
					session.setAttribute("USER_ID", username);
					response.setStatus(302);
					getId(response, con, username, session);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected static void getId(HttpServletResponse response, Connection con, String username, HttpSession session) throws SQLException, IOException {
		PreparedStatement retrieveId = con.prepareStatement("select id from users where username=?");
		retrieveId.setString(1, username);
		ResultSet resultId = retrieveId.executeQuery();
		resultId.next();
		int result = resultId.getInt(1);
		session.setAttribute("id", result);
		response.sendRedirect("main");
	}
}
