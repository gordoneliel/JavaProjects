import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterUserServlet extends BaseServlet
{
	/**
	 * If user is not logged in, allow them to register
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{

		boolean loggedIn = false;
		HttpSession session = request.getSession();
		if(session.getAttribute(NAME) != null)
		{
			loggedIn = true;
		}
		
		PrintWriter out = response.getWriter();

		if (loggedIn)
		{
			response.sendRedirect("/search");
		} else
		{
			printLogin(out, "");
		}
	}

	/**
	 * Validate registration information and add user to database. Redirect to
	 * login.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{		
		boolean loggedIn = false;
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		String error = "";

		
		if(session.getAttribute(NAME) != null)
		{
			loggedIn = true;
		}
		
		if (loggedIn)
		{
			response.sendRedirect("/search");
		}
		else
		{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String confirmpass = request.getParameter("confirmpass");

			if (username == null || username.trim().equals("") && password == null || password.trim().equals("") && confirmpass == null || confirmpass.trim().equals(""))
			{
				error = "Username and/or password fields are empty";
				printLogin(out, error);
				return;
			} else if (!password.trim().equals(confirmpass.trim()))
			{
				error = "Passwords don't match";
				printLogin(out, error);
				return;
			}

			//Finally, the user is ready to go.. set username and password
			session.setAttribute(NAME, username);
			session.setAttribute(PASSWORD, password);
			session.setAttribute(CONFIRM_PASSWORD, confirmpass);
			
			UserManager.addUser(username, password);

			response.sendRedirect("/search");

		}

	}

	public void printLogin(PrintWriter out, String error)
	{

		out.println(header("Register"));
		out.println("<style>");
		out.println("a {color: #04C5FE;}");
		out.println("@import url(http://fonts.googleapis.com/css?family=Exo:100,200,400);@import url(http://fonts.googleapis.com/css?family=Source+Sans+Pro:700,400,300);");
		out.println("body{margin: 0;padding: 0;background: #535353;color: #fff;font-family: Arial;font-size: 12px;}");
//		out.println(".body{position: absolute;top: -20px;left: -20px;right: -40px;bottom: -40px;width: auto;height: auto;background-image: url(https://dl.dropboxusercontent.com/u/31921158/bg01.jpg);background-size: cover;-webkit-filter: blur(5px);z-index: 0;}");
		out.println(".Register input[type=text]{width: 250px;height: 30px;background: transparent;border: 1px solid rgba(255,255,255,0.6);border-radius: 2px;color: #fff;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 4px;}");
		out.println(".Register input[type=password]{width: 250px;height: 30px;background: transparent;border: 1px solid rgba(255,255,255,0.6);border-radius: 2px;color: #fff;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 4px;}");
		out.println(".Register input[type=submit]{width: 260px;height: 35px;background: #fff;border: 1px solid #fff;cursor: pointer;border-radius: 2px;color: #a18d6c;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 6px;margin-top: 10px;}");
		out.println(".Register input[type=text]:focus{outline: none;border: 1px solid rgba(255,255,255,0.9);}");
		out.println(".Register input[type=password]:focus{outline: none;border: 1px solid rgba(255,255,255,0.9);}");
		out.println("::-webkit-input-placeholder{color: rgba(255,255,255,0.6);}::-moz-input-placeholder{color: rgba(255,255,255,0.6);}");
		out.println("a:link {text-decoration: none; color: #fff;font-family: 'Exo', sans-serif;font-size: 20px;font-weight: 400;padding: 4px;}");
		out.println("a:visited {background-color: #00ecff;}");
		out.println("</style>");
		out.println("<div class=\"body\"></div>");
		
//		out.println(".login{position: absolute;top: calc(50% - 75px);left: calc(50% - 50px);height: 150px;width: 350px;padding: 10px;z-index: 2;}");
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Search Engine</title>"
				+ "<style lang=\"text/css\">"
				+ "body { text-align: center;}"
				+ "#content {width: 600px;margin: 0 auto;}"
				+ "#box {position: absolute;top: 25%;left: 30%;width: 40%;}"
				+ "#s {width: 90%;}"
				+ "img {border: 0;}"
				+ "</style></head>"
				+ "<body><div id=\"content\"><div id=\"box\">"
				+ "<a href=\"#\"><img src=\"https://dl.dropboxusercontent.com/u/31921158/logo.png\" "
				+ "alt=\"logo\" /></a>"
				+ "<br>"
				+ "<div class=\"Register\">"
				+ "<form action=\"/register\" method=\"post\">"
				+ "<input name=\"username\" id=\"username\" placeholder=\"Username\" type=\"text\" value=\"username\""
				+ "onclick=\"if (this.value=='username') this.value=''\""
				+ "onblur=\"if (this.value=='') this.value='username')\"/><br>"
				+ "<input name=\"password\" id=\"password\" placeholder=\"Password\" type=\"password\"/> <br>"
				+ "<input name=\"confirmpass\" placeholder=\"Confirm Password\" id=\"confirmpass\" type=\"password\"/><br>"
				+ "<input type=\"submit\" value=\"Register\"></form>"
				+ "</div>"

				+ error
				+ "<br />Already a member? <a href=\"/login\">Login Here.</a> "
				+ "</div></div></body></html>");
		
		out.println(footer());	
	}
	
}