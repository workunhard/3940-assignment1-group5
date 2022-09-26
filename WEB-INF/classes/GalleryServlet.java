import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.*;
import javax.servlet.http.*;
public class GalleryServlet extends HttpServlet {
  private int mCount;
 
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
      if (!isLoggedIn(request)) {
          response.setStatus(302);
          response.sendRedirect("login");
      }
      HttpSession session = request.getSession(false);
      response.setContentType("text/html");
      response.setCharacterEncoding("UTF-8");

    // instead of hardcoding to the first image in the list as done below
    // save search criteria as session variable and use it to filter the files in the above array
    // check if prev or next button pressed and then rotate through the array accordingly

      ArrayList<String> photos = new ArrayList<>();
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
      } catch (ClassNotFoundException e) {
          e.printStackTrace();
      }
      final String URL = "jdbc:mysql://localhost:3306/test";
      final Properties connectionProperties = new Properties();
      connectionProperties.put("user", "root");
      connectionProperties.put("password", "Popcorn");
      try{
          Connection con = DriverManager.getConnection(URL, connectionProperties);
          Statement searchDB = con.createStatement();
          ResultSet rs = searchDB.executeQuery("SELECT filename, caption, datetime FROM photos WHERE UserID = " + session.getAttribute("id") + "");
          while(rs.next()) {
                  photos.add(rs.getString("filename"));
          }
//          for (String photo : photos) {
//              System.out.println(photo);
//          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

    String alt_text = "SOME IMAGE";
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<meta charset='UTF-8'>");
    out.println("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\"></head>");
    out.println("<body>");
    out.println("<p id=\"sessionId\"> Current User: " + session.getAttribute("USER_ID"));
    out.println("<div>");
    out.println("<form action='/3940-assignment1-grp5/gallery' method='GET'>");
    out.println("<div>");
    out.println("<img id = \"img_src\" src=./images/" + photos.get(0) + " alt=" + alt_text + " width=400 height=300>");
    out.println("<div>");
    out.println("<br>");
    out.println("<div class='button'>");
    out.println("<form action=\"prev\" method=\"GET\"><input type=\"submit\" value=\"Prev\"></input></form>"
            + "<form action=\"next\" method=\"GET\"><input type=\"submit\" value=\"Next\"></input></form>");
    out.println();
    out.println("<form action=\"delete\" method=\"GET\"><input type=\"submit\" value=\"Delete\"></input></form>");
    out.println("</div></div><br>");
    out.println("</form>");
    out.println("<div>");
    out.println("<form action='main' method='GET'>");
    out.println("<input type=\"submit\" value=\"Return to Main\"></input>");
    out.println("</div><br>");
    out.println("</form>");
    out.println("</body></html>");
    out.close();
   }
   private boolean isLoggedIn(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null || !req.isRequestedSessionIdValid()) {
			return false;
		} else {
			return true;
		}
	}
}