import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Uses Singleton pattern to maintain a HashMap of all user data.
 */
public class DataSingleton {

	//only 1 instance of this object will ever be created.
	private static final DataSingleton INSTANCE = new DataSingleton();

	//maintain a map of name to UserInfo object
//	protected HashMap<String, UserInfo> userInfo;

	//constructor
	private DataSingleton()
	{
//		userInfo = new HashMap<String, UserInfo>();
	}
	
	/**
	 * Checks the database for an existing user
	 * @param name
	 * @param password
	 * @return
	 */
	public synchronized boolean userExists(String name, String password) 
	{
		return UserManager.authUser(name, password);
	}
	
	/**
	 * Adds a user to the database
	 * @param name
	 * @param password
	 */
	public synchronized void addUser(String name, String password)
	{

		UserManager.addUser(name, password);
	}

	/**
	 * Adds a query to the user's search historyv\
	 * @param username
	 * @param query
	 * @return
	 */
	public synchronized boolean addSearchQuery(String username, String query) 
	{
		return UserManager.saveUserQueries(username, query);
	}

	/**
	 * Deletes the user's searh history
	 * 
	 * @param username
	 */
	public synchronized void deleteSearchHistory(String username)
	{
		UserManager.deleteSearchHistory(username);
	}

	/**
	 * Gets the specified user's search history
	 * @param username
	 */
	public synchronized HashMap<String, String> getUserQueryHistory(String username)
	{
		return UserManager.getSavedQueries(username);
	}
	
	/**
	 * Allows the user to change their password
	 * @param username
	 * @param oldpass
	 * @param newpass
	 * @return
	 */
	public synchronized boolean changePassword(String username, String oldpass, String newpass)
	{
		return UserManager.changePassword(username, oldpass, newpass);
	}
	
	/**
	 * Returns the last time a user logged in
	 * @param username
	 * @return
	 */
	public synchronized String getLastLoginTime(String username)
	{
		return UserManager.getLoginTime(username);
	}

	/**
	 * Returns the last users logged into the search engine
	 * @return
	 */
	public synchronized ArrayList<String> getLastUsers()
	{
		return UserManager.getLastLoggedUsers();
	}
	
	/**
	 * Stores a page that has been visited by the user
	 */
	public synchronized boolean storeVisitedPages(String username, String pageURL)
	{
		return UserManager.storeVisitedPages(username, pageURL);
	}
	/*
	 * Return the instance of this object.
	 */
	public static DataSingleton getInstance()
	{
		return INSTANCE;
	}


}
