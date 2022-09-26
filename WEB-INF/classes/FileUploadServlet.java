import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;

@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                   .append("<body>\r\n")
                   .append("<p id=\"sessionId\"> Current User: " + session.getAttribute("USER_ID") + "\r\n");
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
        out.println(topPart+getListing("c:\\tomcat\\webapps\\3940-assignment1-group5\\images")+bottomPart);
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
            addToDB.execute("INSERT INTO photos (UserID, filename , caption, datetaken) VALUES (0, " + username + ", " + fileName + ","+captionName+","+formDate+")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String getListing(String path) {
     String dirList =  null;
      File dir = new File(path);
      String[] chld = dir.list();
      for(int i = 0; i < chld.length; i++){
         if ((new File(path+chld[i])).isDirectory())
            dirList += "<li><button type=\"button\">"+chld[i]+"</button></li>";
         else
            dirList += "<li>"+chld[i]+"</li>";      
      }
      return dirList;
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

		if (session == null || !req.isRequestedSessionIdValid()) {
			return false;
		}else{
			return true;
		}

	}   
    
}


