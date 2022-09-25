import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

public class SignUpServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>\n" + "<head><title>" + "Sign Up" + "</title></head>\n" + "<body>\n"
                + "<h1 align=\"center\">" + "Sign Up" + "</h1>\n" + "<form action=\"signup\" method=\"POST\">\n"
                + "Username: <input type=\"text\" name=\"user_id\">\n" + "<br />\n"
                + "Password: <input type=\"password\" name=\"password\" />\n" + "<br />\n"
                + "<input type=\"submit\" value=\"Sign Up\" />\n" + "</form>\n"
                + "</form>\n" + "</body>\n</html\n");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Message: " + ex.getMessage ());
            return;
        }
        try {
            final String URL = "jdbc:mysql://localhost:3306/test";
            final Properties connectionProperties = new Properties();
            connectionProperties.put("user", "root");
            connectionProperties.put("password", "");
            String username = request.getParameter("user_id");
            String password = request.getParameter("password");
            con = DriverManager.getConnection(URL, connectionProperties);
            Statement addToDB = con.createStatement();
            addToDB.execute("CREATE TABLE users (ID raw(16) PRIMARY KEY, UserID varchar(20), Password varchar (20))");
            addToDB.execute("INSERT INTO users (UserID , Password ) values ("+ username + " , "+ password +")");
//            String username = request.getParameter("user_id");
//            String password = request.getParameter("password");
//            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle1");
//            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO users (UserID , Password ) values (+"+ username+"+ , +"+password+"+)");
//            preparedStatement.execute();
        } catch (Exception e) {
            response.sendRedirect("login");
        }
    }
}
