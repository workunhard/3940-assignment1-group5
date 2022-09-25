import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>\n" + "<head><title>" + "Login" + "</title></head>\n" + "<body>\n"
				+ "<h1 align=\"center\">" + "Login" + "</h1>\n" + "<form action=\"login\" method=\"POST\">\n"
				+ "Username: <input type=\"text\" name=\"user_id\">\n" + "<br />\n"
				+ "Password: <input type=\"password\" name=\"password\" />\n" + "<br />\n"
				+ "<input type=\"submit\" value=\"Sign in\" />\n" + "</form>\n"
				+ "</form>\n" + "<form action=\"signup\" method=\"GET\">\n" +
				"<input type=\"submit\" value=\"SignUp\" />\n"+ "</body>\n</html\n");
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
//			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception ex) {
			System.out.println("Message: " + ex.getMessage ());
			return;
		}
		try {
//			final String URL = "jdbc:mysql://localhost:3306/test";
//			final Properties connectionProperties = new Properties();
//			connectionProperties.put("user", "16045");
//			connectionProperties.put("password", "Popcorn");
//			con = DriverManager.getConnection(URL, connectionProperties);
//			Statement addToDB = con.createStatement();
//			addToDB.execute("CREATE TABLE IF NOT EXISTS users (ID raw(16) PRIMARY KEY, UserID varchar(20), Password varchar (20))");
//			Statement stmt = con.createStatement();
//			stmt.execute("CREATE TABLE IF NOT EXISTS photos (ID raw(16) PRIMARY KEY, UserID GUID FOREIGN KEY, filename varchar(20), caption varchar(20), datetaken DATE, picture BLOB)");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle");
			PreparedStatement preparedStatement = con.prepareStatement("BEGIN" +
					"    BEGIN" +
					"        EXECUTE IMMEDIATE CREATE TABLE users (ID raw(16), UserID varchar(20), Password varchar (20), CONSTRAINT user_pk PRIMARY KEY(ID));" +
					"    EXCEPTION" +
					"        WHEN OTHERS THEN" +
					"            IF SQLCODE == -955 THEN" +
					"                RAISE;" +
					"            END IF;" +
					"    END;" +
					"END;");
			preparedStatement.execute();
			PreparedStatement stmnt2 = con.prepareStatement("BEGIN" +
					"    BEGIN\n" +
					"        EXECUTE IMMEDIATE CREATE TABLE photos (ID raw(16), UserID, filename varchar(20), caption varchar(20), datetaken DATE, picture BLOB, CONSTRAINT photos_pk PRIMARY KEY(ID));" +
					"    EXCEPTION" +
					"        WHEN OTHERS THEN" +
					"            IF SQLCODE == -955 THEN" +
					"                RAISE;" +
					"            END IF;" +
					"    END;" +
					"END;");
			stmnt2.execute();
		} catch (Exception e) {
			System.out.println("Message: " + e.getMessage ());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
//		final String URL = "jdbc:mysql://localhost:3306/test";
//		final Properties connectionProperties = new Properties();
//		connectionProperties.put("user", "16045");
//		connectionProperties.put("password", "Popcorn");
		try {

			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle");
			Statement searchDB = con.createStatement();
			ResultSet rs = searchDB.executeQuery("SELECT UserID, Password FROM users");

			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();

			String title = "Logged in as: ";
			String username = request.getParameter("user_id");
			String password = request.getParameter("password");

				while(rs.next()) {
					if(rs.getString("UserID").equals(username) && rs.getString("Password").equals(password)) {
						HttpSession session = request.getSession(true);
						session.setAttribute("USER_ID", username);
						response.setStatus(302);
						response.sendRedirect("main");
					}


			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
