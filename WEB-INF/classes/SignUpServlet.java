import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class SignUpServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>\n" + "<head><title>" + "Sign Up" + "</title><link rel=\"stylesheet\" type=\"text/css\"\n" +
                "href=\"css/main.css\"></head>\n<body>\n"
                + "<h1 align=\"center\">" + "Sign Up" + "</h1>\n" + "<form action=\"signup\" method=\"POST\">\n"
                + "Username: <input type=\"text\" name=\"user_name\">\n" + "<br />\n"
                + "Password: <input type=\"password\" name=\"password\" />\n" + "<br><br>\n"
                + "<input id=\"signInBtn\" type=\"submit\" value=\"Sign Up\" />\n" + "</form>\n"
                + "</form>\n" + "</body>\n</html\n");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (Exception ex) {
                System.out.println("Driver exception: " + ex.getMessage());
                return;
            }
            // Connect to DB and insert new user
            final String URL = "jdbc:mysql://localhost:3306/test";
            String username = request.getParameter("user_name");
            String password = request.getParameter("password");
            Connection con = DriverManager.getConnection(URL, "root", "");
            Statement addToDB = con.createStatement();
            addToDB.execute("INSERT INTO users (id, username , password ) VALUES (0, "+ username + ", "+ password +")");

            // Create session
            HttpSession session = request.getSession();

            // Create session variable for logged-in user
            session.setAttribute("USER_ID", username);

            // Create session variable for associated id (reference to uploaded images)
            LoginServlet.getId(response, con, username, session);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
