import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class UserSettingsServlet extends BaseServlet
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		DataSingleton data = DataSingleton.getInstance(); 
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(NAME);
		
		 if(user == null)
		 {
			 response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS +
					 "=" + NOT_LOGGED_IN));
			 return;
		 }
		 
		PrintWriter out = response.getWriter();
		out.println("<style>");
		out.println("a {color: #04C5FE;}");
		out.println("@import url(http://fonts.googleapis.com/css?family=Exo:100,200,400);@import url(http://fonts.googleapis.com/css?family=Source+Sans+Pro:700,400,300);");
		out.println("body{margin: 0;padding: 0;background: #535353;color: #fff;font-family: Arial;font-size: 12px;}");
//		out.println(".body{position: absolute;top: -20px;left: -20px;right: -40px;bottom: -40px;width: auto;height: auto;background-image: url(https://dl.dropboxusercontent.com/u/31921158/bg03.jpg);background-size: cover;-webkit-filter: blur(5px);z-index: 0;}");
		out.println(".changePass input[type=text]{width: 250px;height: 30px;background: transparent;border: 1px solid rgba(255,255,255,0.6);border-radius: 2px;color: #fff;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 4px;}");
		out.println(".changePass input[type=password]{width: 250px;height: 30px;background: transparent;border: 1px solid rgba(255,255,255,0.6);border-radius: 2px;color: #fff;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 4px;}");
		out.println(".changePass input[type=submit]{width: 260px;height: 35px;background: #fff;border: 1px solid #fff;cursor: pointer;border-radius: 2px;color: #a18d6c;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 6px;margin-top: 10px;}");
		out.println(".changePass input[type=text]:focus{outline: none;border: 1px solid rgba(255,255,255,0.9);}");
		out.println(".changePass input[type=password]:focus{outline: none;border: 1px solid rgba(255,255,255,0.9);}");
		out.println("::-webkit-input-placeholder{color: rgba(255,255,255,0.6);}::-moz-input-placeholder{color: rgba(255,255,255,0.6);}");
//		out.println("a:link {text-decoration: none; color: #fff;font-family: Arial;font-size: 12px;font-weight: 400;padding: 4px;}");
//		out.println("a:visited {background-color: #00ecff;}");
		out.println("</style>");
		out.println("<div class=\"body\"></div>");
		
		out.println(header("Settings"));
		out.println("<center>");
//		out.println("<div class=\"Logout\">");
		out.println("<form name = \"logout\" form action=\"logout\" method=\"get\">");
		out.println("<p style=\"text-align:right;\"> Hello " + user
				+ " | <a href=\"/search\"> Back to Search</a>"
				+ " | <a href=\"/visitedResults\">View Visited Pages</a>"
				+ " | <a href=\"/settings\">Settings</a>");
		out.println("<input type=\"submit\" name = \"logout\" value=\"Logout\">");
		out.println("</form>");
//		out.println("</div>");
		
		out.println("<h1>Change Password</h1>");
		out.println("<div class=\"changePass\">");
		out.println("<form name=\"change_password\" action=\"settings\" method=\"post\">");
		out.println("<input name=\"old_password\" id=\"old_password\" placeholder=\"Old Password\" type=\"password\" /><br>");
		out.println("<input name=\"new_password\" id=\"new_password\" placeholder=\"New Password\" type=\"password\" /><br>");
		out.println("<input type=\"submit\" value=\"Change Password\"></form>");
		out.println("</div>");
		out.println("</center>");
		out.println(footer());
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		DataSingleton data = DataSingleton.getInstance();

		HttpSession session = request.getSession();
		String user = (String) session.getAttribute(NAME);
		String oldpass = (String) request.getParameter(OLD_PASS);
		String newpass = (String) request.getParameter(NEW_PASS);

		try
		{
			data.changePassword(user, oldpass, newpass);
			session.setAttribute(PASSWORD, newpass);
			doGet(request, response);
		} 
		catch (Exception e)
		{
			
		}
	}
}
