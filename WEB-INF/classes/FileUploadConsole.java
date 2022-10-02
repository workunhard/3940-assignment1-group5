import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUploadConsole extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dirList = getListing("C:\\tomcat\\webapps\\3940-assignment1-group5\\images");
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
            writer.append("<form method=\"POST\" action=\"uploadConsole\" ")
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

        PrintWriter out = response.getWriter();
        out.println(dirList);
        out.flush();
    }


    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null || !req.isRequestedSessionIdValid()) {
            return false;
        }else{
            return true;
        }

    }

    private String getListing(String path){
        String dirList = null;
        File dir = new File(path);
        String[] child = dir.list();
        for(int i=0; i < child.length; i++){
            dirList += "," + child[i];
        }
        System.out.println(dirList);
        return dirList;
    }
}
