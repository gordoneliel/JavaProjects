import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchServlet extends BaseServlet
{
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		DataSingleton data = DataSingleton.getInstance(); // valid for Singleton

		HttpSession session = request.getSession();
		String  searchHistory = request.getParameter(SEARCH_HISTORY); 
		String name = (String) session.getAttribute(NAME);
		try
		{
			String query = request.getParameter("searchquery"); 
			query = query.toLowerCase();
			if(query.equals("")) 
			{
				response.sendRedirect("/search");
				return;
			}

			BaseServlet.serverIndex.readQueriesFromHtml(query);
			
			// Save search history if the user checks the checkbox

			if (searchHistory != null)
			{
				data.addSearchQuery(name, query);
			}
			
			response.sendRedirect("/search");
		} catch (Exception e)
		{

		}
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{

		DataSingleton data = DataSingleton.getInstance(); // valid for Singleton
		 
		
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(NAME);
		String password = (String) session.getAttribute(PASSWORD);
		String query = (String) session.getAttribute(QUERY);
		String lastLogin = data.getLastLoginTime(name);

		// user is not logged in, redirect to login page
		 if(name == null || !data.userExists(name, password))
		 {
			 response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS +
					 "=" + NOT_LOGGED_IN));
		 return;
		 }

		PrintWriter out = prepareResponse(response);

		out.println(header("Search:"));

		// Logging out, settings and history

		out.println("<style>");
		out.println("a {color: #04C5FE;}");
		out.println("@import url(http://fonts.googleapis.com/css?family=Exo:100,200,400);@import url(http://fonts.googleapis.com/css?family=Source+Sans+Pro:700,400,300);");
		out.println("body{margin: 0;padding: 0;background: #535353;color: #fff;font-family: Arial;font-size: 12px;}");
//		out.println(".body{position: absolute;top: -20px;left: -20px;right: -40px;bottom: -40px;width: auto;height: 400;background-image: url(http://ginva.com/wp-content/uploads/2012/07/city-skyline-wallpapers-008.jpg);background-size: cover;-webkit-filter: blur(5px);z-index: 0;}");

//		out.println("table, th, td {border-collapse: collapse;}");
		out.println("th {border-top-left-radius: 5px;border-bottom-left-radius: 5px;}");
		out.println("table {border-top-left-radius: 5px; border-top-right-radius: 5px;}");
		out.println("table#t01 tr:nth-child(even) {background-color: #eee;}table#t01 tr:nth-child(odd) {background-color: #fff;}");
		out.println("th, td {padding: 15px 10px; text-align: left; color: #555;}");
		out.println("table#t01 {width: 80%; background-color: #ffffff;}");
		out.println("</style>");
		
//		out.println("<div class=\"body\"></div>");
		
		out.println("<form name = \"logout\" form action=\"logout\" method=\"get\">");
		out.println("<p style=\"text-align:right;\"> Hello " + name + " |"
				+ " Your last login was: " + lastLogin
				+ " | <a href=\"/visitedResults\">View Visited Pages</a>"
				+ " | <a href=\"/search_history\">View Search History</a>"
				+ " | <a href=\"/settings\">Settings</a>");
		out.println("<input type=\"submit\" name = \"logout\" value=\"Logout\">");
		out.println("</form>");
		
		ArrayList<String> lastUsers = data.getLastUsers();
		out.print("<em style=\"text-align:left;\"> Last logged in Users: ");
		for (String user : lastUsers)
		{
			out.print("<b>" + user + ",  ");
		}
		out.println("</b></em>");

		out.println("<center>");
		out.println("<h1> Umbrella Search</h1>");
		out.println("<a href=\"#\"><img src=\"https://dl.dropboxusercontent.com/u/31921158/logo.png\" " + "alt=\"logo\" /></a>");

		out.println("<form action=\"/search\" method=\"post\">");
		out.println("<p>Search for: <input type=\"text\" name=\"searchquery\" id= \"search_box\" size=\"30\"></p>");
		out.println("\t\t<input type=\"checkbox\" name=\"searchHistory\" checked=\"yes\"> Check this box to switch on Search History");
		out.println("<p><input type=\"submit\" value=\"Search\"></p>");
		out.println("<div id=\"box_cont\"><form action=\"/results\" method=\"get\">");
		out.println("</div></form></div>");
		out.println("</form>");

		
		out.println("<center>");
		out.println("<table id= \"t01\">");
		BaseServlet.serverIndex.writeQueryResultsToHTML(out, query);
		
		out.println("</table>");
		out.println("</center>");
		out.println("<script>");
		out.println("function linkClicked() {data.storeVisitedPages(user, url); <p> HELLLLOEOEOEO</p>;return true;}");
		out.println("</script>");
		out.println(footer());

	}
}
