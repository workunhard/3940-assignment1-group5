import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dirList = getListing();
        response.setContentLength(dirList.length());
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
       boolean isLoggedIn = isLoggedIn(request);
       if (!isLoggedIn) {
           response.setStatus(302);
           response.sendRedirect("login");

       }else {
           PrintWriter writer = response.getWriter();
                  writer.append("<!DOCTYPE html>\r\n")
                          .append("<html>\r\n")
                          .append("<head>\r\n")
                          .append("<title>File Upload Form</title>\r\n")
                          .append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\">")
                          .append("</head>\r\n")
                          .append("<body>\r\n").append("<p id=\"sessionId\"> Current User: ").append(String.valueOf(session.getAttribute("USER_ID"))).append("\r\n");
           writer.append("<h1>Upload file</h1>\r\n");
           writer.append("<form method=\"POST\" action=\"upload\" ")
                   .append("enctype=\"multipart/form-data\">\r\n");
           writer.append("<input type=\"file\" name=\"fileName\"/><br/><br/>\r\n");
           writer.append("Caption: <input type=\"text\" name=\"caption\"<br/><br/>\r\n");
           writer.append("<br />\n");
           writer.append("Date: <input type=\"date\" name=\"date\"<br/><br/>\r\n");
           writer.append("<br />\n");
           writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
           writer.append("</form>\r\n");
           writer.append("</body>\r\n").append("</html>\r\n");
	   }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("fileName");
        String captionName = request.getParameter("caption");
        String formDate = request.getParameter("date");
        String fileName = getSubmittedFileName(filePart);

        assert fileName != null;
        if(fileName.equals("")){
            response.setStatus(302);
            response.sendRedirect("upload");
            return;
        }

        if(formDate.equals("")) formDate = "2020-10-10";
        if(captionName.equals("")) captionName = "No caption";
        filePart.write(System.getProperty("catalina.base") + "/webapps/3940-assignment1-group5/images/" + fileName);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String topPart = "<!DOCTYPE html><html><body><ul>";
        String bottomPart = "</ul></body></html>";        
        out.println(topPart+getListing() + bottomPart);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Driver exception: " + ex.getMessage());
            return;
        }
        try {
            // Connect to DB and insert new user
            final String URL = "jdbc:mysql://localhost:3306/test";
            HttpSession session = request.getSession(true);
            String username = session.getAttribute("id").toString();
            Connection con = DriverManager.getConnection(URL, "root", "Popcorn");
            Statement addToDB = con.createStatement();
            addToDB.execute("INSERT INTO photos (ID, UserID, filename , caption, datetaken) VALUES (0, '"
                    + username + "', '" + fileName + "','" + captionName + "','" + formDate + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect("main");
    }
    private String getListing() {
        System.out.println("c:\\tomcat\\webapps\\3940-assignment1-group5\\images");
      StringBuilder dirList = new StringBuilder();
      File dir = new File("c:\\tomcat\\webapps\\3940-assignment1-group5\\images");
      String[] chld = dir.list();
        assert chld != null;
        for (String s : chld) {
            if ((new File("c:\\tomcat\\webapps\\3940-assignment1-group5\\images" + s)).isDirectory())
                dirList.append("<li><button type=\"button\">").append(s).append("</button></li>");
            else
                dirList.append("<li>").append(s).append("</li>");
        }
      return dirList.toString();
    }
    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
    
	private boolean isLoggedIn(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

        return session != null && req.isRequestedSessionIdValid();

	}   
    
}


