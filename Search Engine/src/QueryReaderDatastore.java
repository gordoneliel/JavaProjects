import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.LinkedHashMap;

/**
 *  Stores the results of the search in a threadsafe data structure
 *  
 * @author gordoneliel
 *
 */
public class QueryReaderDatastore
{
	private LinkedHashMap<String, DocumentResultList> searchResults;
	private LockSmith lockMechanic;
	
	/**
	 * Constructor to make a threadsafe search result data structure
	 */
	public QueryReaderDatastore()
	{
		this.searchResults = new LinkedHashMap<>();
		this.lockMechanic = new LockSmith();
	}
	
	/**
	 * Adds the result of each thread into a global search database
	 * 
	 * @param query
	 * 				- The query to be stored
	 * @param result
	 * 				- The result of a query from a thread
	 */
	public void addResults(String query, DocumentResultList result)
	{
		lockMechanic.acquireWriteLock();
		this.searchResults.put(query, result);
		lockMechanic.releaseWriteLock();
	}
	
	/**
	 * Writes the search results to an output file
	 * 
	 */
	public void writeQueryResultsToFile(Path outputPath)
	{
		lockMechanic.acquireReadLock();
		try (BufferedWriter output = new BufferedWriter(new FileWriter(outputPath.toFile().getAbsoluteFile())))
		{
			for (String wordName : searchResults.keySet())
			{
				output.write(wordName + "\n");
				for (DocumentResult file : searchResults.get(wordName))
				{
					output.write(file + "\n");
				}
				output.write("\n");
			}
			output.flush();
		} 
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		lockMechanic.releaseReadLock();
	}
	
	/**
	 * Writes the search results to html
	 * @throws FileNotFoundException 
	 */
	public void writeQueryResultsToHTML(PrintWriter output, String query)
	{
		lockMechanic.acquireReadLock();
		
		if (searchResults.size() == 0) 
		{
			output.write("<p>No results found</p>");
		}
		
		for (String wordName : searchResults.keySet())
		{
			output.write("<p>" + searchResults.get(wordName).size() + "  Results for:  " + wordName + "</p>");
			if (searchResults.size() == 0) 
			{
				output.write("<p>No results found</p>");
			}
			else
			{
				for (DocumentResult file : searchResults.get(wordName))
				{
					output.write("<tr><td><a href=" + file.toString() + " onclick= return linkClicked();" + ">"
							+ file.toString() + "</a></tr></td>");
				}
			}
			
		}
		output.flush();
		
		
		searchResults.clear();
		lockMechanic.releaseReadLock();
	}
	
}
