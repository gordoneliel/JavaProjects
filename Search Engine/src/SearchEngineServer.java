import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;


public class SearchEngineServer 
{
	private Crawler webCrawler = new Crawler(null, true, 10);
	/**
	 * @param args
	 */
	protected static InvertedIndex serverIndex;
	
	public void startServer(InvertedIndex invertedIndex) throws Exception
	{
		Server server = new Server(8080);
	    serverIndex = invertedIndex;
        
        //create a ServletHander to attach servlets
        ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
        server.setHandler(servhandler);

        servhandler.addServlet(LoginServlet.class, "/login");
        servhandler.addServlet(VerifyUserServlet.class, "/verifyuser");
        servhandler.addServlet(LogoutServelet.class, "/logout");
        servhandler.addServlet(SearchServlet.class, "/search");
        servhandler.addServlet(QueryHistoryServlet.class, "/search_history");
        servhandler.addServlet(UserSettingsServlet.class, "/settings");
        servhandler.addServlet(RegisterUserServlet.class, "/register");
        servhandler.addServlet(VisitedServlet.class, "/visitedResults");

        //set the list of handlers for the server
        server.setHandler(servhandler);
        
        //start the server
        server.start();
        server.join();
	}
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
        
        //create a ServletHander to attach servlets
        ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
        server.setHandler(servhandler);
        servhandler.addServlet(LoginServlet.class, "/login");
        servhandler.addServlet(VerifyUserServlet.class, "/verifyuser");
        servhandler.addServlet(LogoutServelet.class, "/logout");
        servhandler.addServlet(SearchServlet.class, "/search");
        servhandler.addServlet(QueryHistoryServlet.class, "/search_history");
        servhandler.addServlet(UserSettingsServlet.class, "/settings");
        servhandler.addServlet(RegisterUserServlet.class, "/register");
        servhandler.addServlet(VisitedServlet.class, "/visitedResults");

        //set the list of handlers for the server
        server.setHandler(servhandler);
        
        //start the server
        server.start();
        server.join();

	}

}
