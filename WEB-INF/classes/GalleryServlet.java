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
        if(session.getAttribute("position") == null) {
            session.setAttribute("position", 0);
        }

      ArrayList<String> photos = new ArrayList<>();
      ArrayList<String> captions = new ArrayList<>();
      ArrayList<String> dates = new ArrayList<>();
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
          ResultSet rs = searchDB.executeQuery("SELECT filename, caption, datetaken FROM photos WHERE UserID = " + session.getAttribute("id") + "");
          while(rs.next()) {
                  photos.add(rs.getString("filename"));
                  captions.add(rs.getString("caption"));
                  dates.add(rs.getString("datetaken"));
          }
//          for (String photo : photos) {
//              System.out.println(photo);
//          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
    int position = (Integer) session.getAttribute("position");
      if(position >= photos.size()) {
            position = 0;
      } else if(position < 0) {
          position = photos.size() - 1;
      }
    String alt_text = "SOME IMAGE";
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<meta charset='UTF-8'>");
    out.println("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\"></head>");
    out.println("<body>");
    out.println("<p id=\"sessionId\"> Current User: " + session.getAttribute("USER_ID"));
    out.println("<div>");
    out.println("<div><form>");
    out.println("<img id = \"img_src\" src=./images/" + photos.get(position) + " alt=" + alt_text + " width=400 height=300>");
    out.println("<p>" + captions.get(position) + "</p>");
    out.println("<p>" + dates.get(position) + "</p>");
    out.println("<div></form>");
    out.println("<br>");
    out.println("<div class='button'>");
    out.println("<form action=\"/3940-assignment1-group5/gallerynextprev\" method=\"GET\">" +
            "<input type=\"submit\" value=\"Prev\" name=\"choice\">" +
            "<input type=\"submit\" value=\"Next\" name=\"choice\"></input>" +
            "</input></form>");
    out.println("<form action=\"/3940-assignment1-group5/gallery\" method=\"POST\"><input type=\"submit\" value=\"Delete\"></input></form>");
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

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      HttpSession session = request.getSession();
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
           ResultSet rs = searchDB.executeQuery("SELECT filename, caption, datetaken FROM photos WHERE UserID = " + session.getAttribute("id") + "");
           while(rs.next()) {
               photos.add(rs.getString("filename"));
           }
           int position = (Integer) session.getAttribute("position");
           if(position >= photos.size()) {
               position = 0;
           } else if(position < 0) {
               position = photos.size() - 1;
           }
           Statement delete = con.createStatement();
           delete.executeUpdate("DELETE FROM photos WHERE filename = '"+ photos.get(position) +"'");
       } catch (SQLException e) {
           e.printStackTrace();
       }
       response.sendRedirect("gallery");
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