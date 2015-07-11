import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VisitedServlet extends BaseServlet
{
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		DataSingleton data = DataSingleton.getInstance();
		HttpSession session = request.getSession();
		String user = (String)session.getAttribute(NAME);
		PrintWriter out = prepareResponse(response);
		
		 if(user == null)
		 {
			 response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS +
					 "=" + NOT_LOGGED_IN));
			 return;
		 }
		 
		if (session.getAttribute(NAME) != null)
		{
			String url = request.getParameter("url");

//			url.trim().equals("");
			if (false)
			{
				response.sendRedirect("/visitedResults");
//				redirectTo(response, "/");
			} else
			{
				data.storeVisitedPages(user, url);
				
				out.println(header("Visited Pages"));
				out.println("<style>");
				out.println("a {color: #04C5FE;}");
				out.println("@import url(http://fonts.googleapis.com/css?family=Exo:100,200,400);@import url(http://fonts.googleapis.com/css?family=Source+Sans+Pro:700,400,300);");
				out.println("body{margin: 0;padding: 0;background: #535353;color: #fff;font-family: Arial;font-size: 12px;}");
//				out.println(".body{position: absolute;top: -20px;left: -20px;right: -40px;bottom: -40px;width: auto;height: auto;background-image: url(http://ginva.com/wp-content/uploads/2012/07/city-skyline-wallpapers-008.jpg);background-size: cover;-webkit-filter: blur(5px);z-index: 0;}");
			
				//Buttons
//				out.println(".Logout input[type=submit]{width: 260px;height: 30px;background: #1abc9c;border: 1px solid #fff;cursor: pointer;border-radius: 2px;color: #fff; corner-radius: 2; font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 6px;margin-top: 10px;}");
				
//				out.println("table, th, td {border-collapse: collapse;}");
				out.println("th {border-top-left-radius: 5px;border-bottom-left-radius: 5px;}");
				out.println("table {border-radius: 5px;}");
				out.println("table#t01 tr:nth-child(even) {background-color: #eee;}table#t01 tr:nth-child(odd) {background-color: #fff;}");
				out.println("th, td {padding: 15px 10px; color: #555;}");
				out.println("table#t01 {width: 80%; background-color: #ffffff;}");
				out.println("</style>");
//				out.println("<div class=\"body\"></div>");
//				out.println(header("Query History"));
				out.println("<center>");
				out.println("<div class=\"Logout\">");
				out.println("<form name = \"logout\" form action=\"logout\" method=\"get\">");
				out.println("<p style=\"text-align:right;\"> Hello " + user
						+ " | <a href=\"/search\"> Back to Search</a>"
						+ " | <a href=\"/visitedResults\">View Visited Pages</a>"
						+ " | <a href=\"/settings\">Settings</a>");
				out.println("<input type=\"submit\" name = \"logout\" value=\"Logout\">");
				out.println("</form>");
				out.println("</div>");
				out.println("<h4>Your recent search history:</h4>");
				
				out.println("<table id= \"t01\">");
				HashMap< String, String> queryHistory = data.getUserQueryHistory(user);
				for (String query : queryHistory.keySet())
				{
					out.write("<tr><td>" + "Searched for: " + query + " </td>" + "<td align=\"right\" >" + "Time:    " + queryHistory.get(query) + "</td></tr>");
				}
				
//				out.println("<center>");
				out.println("</table>");
				out.println("<form action=\"/search_history\" method=\"post\">");
				out.println("<p><input type=\"submit\" value=\"Clear History\"></p>");
				out.println("</form>");
				out.println("</center>");
				
				out.println(footer());
//				db.storeVisitedPage(getCurrentUser(), url);
//				response.sendRedirect("/visitedResults");
//				redirectTo(response, url);
			}
		} else
		{
//			logger.debug("need login first");
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + ERROR));
			return;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{

	}
}
