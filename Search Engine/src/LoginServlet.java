import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Allows a user to log in
 */
public class LoginServlet extends BaseServlet
{
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		DataSingleton data = DataSingleton.getInstance(); //valid for Singleton implementation
		HttpSession session = request.getSession();

		// if user is logged in, redirect
		if (session.getAttribute(NAME) != null)
		{
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}

		String status = getParameterValue(request, STATUS);
		
		boolean statusok = status != null && status.equals(ERROR)?false:true;
		boolean redirected = status != null && status.equals(NOT_LOGGED_IN)?true:false;

		// output text box requesting user name
		PrintWriter out = prepareResponse(response);

		out.println(header("Login Page"));

		// if the user was redirected here as a result of an error
		if (!statusok)
		{
			out.println("<h3><font color=\"red\">Username or Password Incorrect</font></h3>");
		} else if (redirected)
		{
			out.println("<h3><font color=\"red\">Log in first!</font></h3>");
		}
		printLogin(out, status);
	}

	public void printLogin(PrintWriter out, String error) 
	{

		out.println("<style>");
		out.println("a {color: #04C5FE;}");
		out.println("@import url(http://fonts.googleapis.com/css?family=Exo:100,200,400);@import url(http://fonts.googleapis.com/css?family=Source+Sans+Pro:700,400,300);");
		out.println("body{margin: 0;padding: 0;background: #535353;color: #fff;font-family: Arial;font-size: 12px;}");
//		out.println(".body{position: absolute;top: -20px;left: -20px;right: -40px;bottom: -40px;width: auto;height: auto;background-image: url(https://dl.dropboxusercontent.com/u/31921158/bg03.jpg);background-size: cover;-webkit-filter: blur(5px);z-index: 0;}");
		out.println(".login input[type=text]{width: 250px;height: 30px;background: transparent;border: 1px solid rgba(255,255,255,0.6);border-radius: 2px;color: #fff;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 4px;}");
		out.println(".login input[type=password]{width: 250px;height: 30px;background: transparent;border: 1px solid rgba(255,255,255,0.6);border-radius: 2px;color: #fff;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 4px;}");
		out.println(".login input[type=submit]{width: 260px;height: 35px;background: #fff;border: 1px solid #fff;cursor: pointer;border-radius: 2px;color: #a18d6c;font-family: 'Exo', sans-serif;font-size: 16px;font-weight: 400;padding: 6px;margin-top: 10px;}");
		out.println(".login input[type=text]:focus{outline: none;border: 1px solid rgba(255,255,255,0.9);}");
		out.println(".login input[type=password]:focus{outline: none;border: 1px solid rgba(255,255,255,0.9);}");
		out.println(".login input[type=button]:hover{opacity: 0.8;}");
		out.println("::-webkit-input-placeholder{color: rgba(255,255,255,0.6);}::-moz-input-placeholder{color: rgba(255,255,255,0.6);}");
		out.println("a:link {text-decoration: none; color: #fff;font-family: 'Exo', sans-serif;font-size: 20px;font-weight: 400;padding: 4px;}");
		out.println("a:visited {background-color: #00ecff;}");
		out.println("</style>");
		out.println("<div class=\"body\"></div>");
		
//		out.println(".login{position: absolute;top: calc(50% - 75px);left: calc(50% - 50px);height: 150px;width: 350px;padding: 10px;z-index: 2;}");
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
				+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
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
				+ "<div class=\"login\">"
				+ "<form name=\"name\" action=\"verifyuser\" method=\"post\">"
				+ "<input name=\"name\" id=\"username\" placeholder=\"Username\" type=\"text\" value=\"username\""
				+ "onclick=\"if (this.value=='username') this.value=''\""
				+ "onblur=\"if (this.value=='') this.value='username')\"/><br>"
				+ "<input name=\"password\" id=\"password\" placeholder=\"Password\" type=\"password\" /><br>"
				+ "<input type=\"submit\" value=\"Login\"></form>"
				+ "</div>"
				
				+ "Please Login to use our services!<br />"
				+ "Don't have an account? <a href=\"/register\">Register Here.</a><p>");
		
		out.println(footer());

		if (error != null)
		{
			out.println(error);
		}

	}
}
