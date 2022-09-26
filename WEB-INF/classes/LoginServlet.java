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
				+ "href=\"css/main.css\"></head>\n" + "<body>\n"
				+ "<h1 align=\"center\">" + "Login" + "</h1>\n<form action=\"login\" method=\"POST\">\n"
				+ "Username: <input type=\"text\" name=\"user_name\">\n" + "<br>\n"
				+ "Password: <input type=\"password\" name=\"password\"/>\n" + "<br><br>\n"
				+ "<input id=\"signInBtn\" type=\"submit\" value=\"Sign In\" />\n" + "</form>\n"
				+ "</form>\n" + "<p id=\"noAccount\">Not registered?</p>\n<form action=\"signup\" method=\"GET\">\n" +
				"<input id=\"signUpBtn\" type=\"submit\" value=\"Sign Up\" />\n"+ "</body>\n</html\n");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		 final String URL = "jdbc:mysql://localhost:3306/test";
//		final String URL = "jdbc:mysql://ceux8kf47jbi8xmk:x1sp0y8iza95oaiv@cwe1u6tjijexv3r6.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/t4ot5pmewuphycfy";
		final Properties connectionProperties = new Properties();
		 connectionProperties.put("user", "root");
		 connectionProperties.put("password", "");
//		connectionProperties.put("user", "ceux8kf47jbi8xmk");
//		connectionProperties.put("password", "x1sp0y8iza95oaiv");

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
				} else {
					response.sendRedirect("login");
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
