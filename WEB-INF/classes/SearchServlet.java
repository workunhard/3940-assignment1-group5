import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
public class SearchServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
System.out.println("?????doGet Called???????????????");
		if (!isLoggedIn(request)) { 
			response.setStatus(302);
			response.sendRedirect("login");
		}
		HttpSession session = request.getSession();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String html = "<!DOCTYPE html>" +
				"<html>" +
				"<head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\"></head>" +
				"<body>" +
				"<p id=\"sessionId\"> Current User: " + session.getAttribute("USER_ID") +
				"<h2> Search Filter </h2> " +
				"<form action='search' method = 'post' id = 'searchForm'>" +
				"<label for='caption'>Caption</label>" +
				"<input type='text' id = 'caption' name = 'caption'>" +
				"<label for='date'>Date</label>" +
				"<input type='date' placeholder='yyyy-mm-dd' id = 'date' name = 'date'>" +
				"<button type='submit' form='searchForm' value='Submit'>Search</button>" +
				"</form>" +
				"</body>" +
                "</html>";
		PrintWriter out = response.getWriter();
		out.println(html);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		if (!(isLoggedIn(request))) {
			response.sendRedirect("login");
		} else {

			HttpSession session = request.getSession(false);

			PrintWriter out = response.getWriter();
			String caption = request.getParameter("caption");
			String date = request.getParameter("date");
			if (date.equals("yyyy-mm-dd"))
				date = "";
			session.setAttribute("caption", caption);
			session.setAttribute("date", date);
			session.setAttribute("search", "true");
			response.sendRedirect("gallery");
			
		}
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
