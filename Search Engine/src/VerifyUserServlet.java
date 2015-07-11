import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Servlet invoked at login.
 * Creates cookie and redirects to main ListServlet.
 */
public class VerifyUserServlet extends BaseServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//VerifyUser does not accept GET requests. Just redirect to login with error status.
		response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + ERROR));
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//map id to name and userinfo
		DataSingleton data = DataSingleton.getInstance();

		String username = request.getParameter(NAME);
		String password = request.getParameter(PASSWORD);
		String confirmPassword = request.getParameter(CONFIRM_PASSWORD);
		
		if(username == null || username.trim().equals("") && password == null || password.trim().equals("")) 
		{
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + ERROR));
			return;
		}
		
		if(!data.userExists(username, password))
		{
			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + ERROR));
			return;
		}


				
		HttpSession session = request.getSession();
		session.setAttribute(NAME, username);
		session.setAttribute(PASSWORD, password);
		session.setAttribute(CONFIRM_PASSWORD, confirmPassword);
		

		
		//we assume no username conflicts and provide no ability to register for our service!
		
		PrintWriter out = response.getWriter();
		
		if (username != null || username.trim().equals("") && password != null || password.trim().equals("") && data.userExists(username, password))
		{
	
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
					+ "<html><head><title></title>"
					+ "<meta http-equiv=\"REFRESH\" content=\"0;url=/\">"
					+ "</head><body></body></html>");
			response.sendRedirect(response.encodeRedirectURL("/search"));
		} 
//		else //if(username == null || username.equals("") || !data.userExists(username, password)) 
//		{
////			out.println(header("Login Page"));
////			out.println("<h3><font color=\"red\">Invalid Request to Login</font></h3>");
////			out.println(footer());
////			response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + ERROR));
////			return;
//		}
		
	}
	
}
