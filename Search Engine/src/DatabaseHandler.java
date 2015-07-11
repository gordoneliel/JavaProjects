import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHandler 
{

	/**
	 * Connects to database and returns connection
	 * 
	 * @return connection to database
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException
	{

		String host = "localhost"; // no slash
		String db = "user12";
		String port = "3306";
		String dbuser = "user12";
		String dbpass = "user12";

		try 
		{
			// load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e)
		{
			System.err.println("Can't find driver");
			System.exit(1);
		}

		// format "jdbc:mysql://[hostname][:port]/[dbname]"
//		String urlString = "jdbc:mysql://" + host + ":" + port + "/" + db;
		String urlString = "jdbc:mysql://127.0.0.1/"+db;
		return DriverManager.getConnection(urlString, dbuser, dbpass);
	}

}