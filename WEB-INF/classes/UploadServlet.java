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
public class UploadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dirList = getListing("C:\\tomcat\\webapps\\3940-assignment1-group5\\images");
        response.setContentType("text/plain");
        response.setContentLength(dirList.length());
        PrintWriter out = response.getWriter();
        out.println(dirList);	
        out.flush();  
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
            addToDB.execute("INSERT INTO photos (ID, UserID, filename , caption, datetaken) VALUES (0, \'"
                    + username + "\', \'" + fileName + "\',\'" + captionName + "\',\'" + formDate + "\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        filePart.write(System.getProperty("catalina.base") + "/webapps/3940-assignment1-group5/images/" + fileName);
        try {
            GetListing.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    private String getListing(String path) {
     System.out.println("??????? in getListing??????\n");
     String dirList =  null;
      File dir = new File(path);
      String[] chld = dir.list();
      for(int i = 0; i < chld.length; i++){
         dirList += "," + chld[i];      
      }
      System.out.println(dirList);
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
}



