import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserManager
{

	/**
	 * Saves a users's query history to an sql database
	 * 
	 * @param username
	 * @param query
	 * @return
	 */
	public static boolean deleteSearchHistory(String username)
	{
		Connection con = null;
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt
					.executeQuery("SELECT * "
							+ "FROM search_history WHERE username=\""
							+ username + "\"");
			
			if (result.next())
			{
				stmt = con.createStatement();
				stmt.execute("DELETE FROM search_history where username='" + username + "'");
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
					return true;
				}
			} catch (SQLException e)
			{
			}
		}
		return false;

	}
	
	/**
	 * Provides a list of search queries made by the user
	 * @param username
	 * @return
	 */
	public static HashMap<String, String> getSavedQueries(String username)
	{
		Connection con = null;
		String user;
		HashMap<String, String> retval = null;
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT username "
					+ "FROM user WHERE username=\"" + username + "\"");
			if (result.next())
			{
				user = result.getString("username");
				stmt = con.createStatement();
				result = stmt
						.executeQuery("SELECT query, searchDate FROM search_history WHERE username=\'"
								+ user + "';");
				retval = new HashMap<String, String>();
				while (result.next())
				{
					retval.put(result.getString("query"), result.getString("searchDate"));
				}
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
				}
			} catch (SQLException e)
			{
			}
		}
		return retval;
	}

	/**
	 * Saves a users's query history to an sql database
	 * 
	 * @param username
	 * @param query
	 * @return
	 */
	public static boolean saveUserQueries(String username, String query)
	{
		Connection con = null;
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();

				stmt.execute("INSERT INTO search_history (username, query) "
						+ "VALUES ('" + username + "','" + query + "')");
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
					return true;
				}
			} catch (SQLException e)
			{
			}
		}
		return false;

	}

	/**
	 * Returns the last login time of the user
	 * @param username
	 * @return
	 */
	public static String getLoginTime(String username)
	{
		Connection con = null;
		String loginTime = null;
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT loginTime  "
					+ "FROM user WHERE username=\"" + username + "\"");

			if (result.next())
			{
				loginTime = result.getString("loginTime");
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
				}
			} catch (SQLException e)
			{
			}
		}
		return loginTime;
	
	}
	/**
	 * Authenticate user with database. If given username exists, check to see
	 * if stored password matches given password.
	 * 
	 * @param username
	 * @param password
	 * @return results of operation
	 */
	public static boolean authUser(String username, String password)
	{
		Connection con = null;
		boolean retval = false;
		try
		{
			String storedPass;
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * "
					+ "FROM user WHERE username=\"" + username + "\"");

			if (result.next())
			{
				storedPass = result.getString("password");
				if (password.equals(storedPass))
				{
					retval = true;
					// update login time
					stmt.execute("UPDATE user SET loginTime=CURRENT_TIMESTAMP" + ";");
				}
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				con.close();
			} catch (SQLException e)
			{
			}
		}
		return retval;
	}

	/**
	 * If username exists and previous password matches stores password, replace
	 * stored password with given new password.
	 * 
	 * @param username
	 * @param oldpass
	 * @param newpass
	 * @return results of operation
	 */
	public static boolean changePassword(String username, String oldpass,
			String newpass)
	{
		Connection con = null;
		boolean retval = false;
		int userId;
		try
		{
			String storedPass;
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * "
					+ "FROM user WHERE username=\"" + username + "\"");

			if (result.next())
			{
				storedPass = result.getString("password");
				if (oldpass.equalsIgnoreCase(storedPass))
				{
					stmt = con.createStatement();
					stmt.execute("UPDATE user SET password=\""  + newpass + "\"" + "WHERE username=\""  + username  + "\";");
					retval = true;
				}
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				con.close();
			} catch (SQLException e)
			{
			}
		}
		return retval;
	}

	/**
	 * If username does not exist, add user with given username and password
	 * 
	 * @param username
	 * @param password
	 */
	public static void addUser(String username, String password)
	{
		Connection con = null;
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * "
					+ "FROM user WHERE username=\"" + username + "\"");

			if (!result.next())
			{
				//con = DatabaseHandler.getConnection();
				stmt = con.createStatement();
				stmt.execute("INSERT INTO user (username,password) "
						+ "VALUES ('" + username + "','" + password + "')");
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
				}
			} catch (SQLException e)
			{
			}
		}
	}
	
	/**
	 * Returns the last users logged into the search engine
	 * @return
	 */
	public static ArrayList<String> getLastLoggedUsers()
	{
		Connection con = null;
		ArrayList<String> lastUsers = new ArrayList<>();
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * "
					+ "FROM user;");
			
			while(result.next())
			{
				lastUsers.add(result.getString("username"));
			}
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
				}
			} catch (SQLException e)
			{
			}
		}
		return lastUsers;
	}
	
	/**
	 * Stores all the pages visited by the user
	 */
	public static boolean storeVisitedPages(String username, String pageURL)
	{
		Connection con = null;
		try
		{
			con = DatabaseHandler.getConnection();
			Statement stmt = con.createStatement();

				stmt.execute("INSERT INTO visited_pages (username, visitedPage) "
						+ "VALUES ('" + username + "','" + pageURL + "')");
		} catch (SQLException e)
		{
			System.out.println("Connection error");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (con != null)
				{
					con.close();
					return true;
				}
			} catch (SQLException e)
			{
			}
		}
		return false;

	}
}