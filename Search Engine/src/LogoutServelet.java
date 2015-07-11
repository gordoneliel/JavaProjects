import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LogoutServelet extends BaseServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Data data = (Data) getServletConfig().getServletContext().getAttribute(DATA);
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		response.sendRedirect(response.encodeRedirectURL("/login"));
		//return;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		super.doPost(request, response);
		
		Data data = (Data) getServletConfig().getServletContext().getAttribute(DATA);
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		response.sendRedirect(response.encodeRedirectURL("/login?" + STATUS + "=" + NOT_LOGGED_IN));
		return;
	}
}
