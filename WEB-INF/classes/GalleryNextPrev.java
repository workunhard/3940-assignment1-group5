import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class GalleryNextPrev  extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        int pos = (Integer) session.getAttribute("position");
        if(request.getParameter("choice").equals("Next")) {
            pos += 1;
        } else {
            pos -= 1;
        }
        session.setAttribute("position", pos);
        response.sendRedirect("gallery");
    }
}
