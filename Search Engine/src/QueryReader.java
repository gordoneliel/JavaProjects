import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Reads in a list of queries and executes them on the inverted index
 * It also stores a LinkedHashMap of queries to a sorted list of search results in 
 * decending order of relevance
 * 
 * @author gordoneliel
 *
 */
public class QueryReader
{
	private Path filePath;
	private InvertedIndex index;
	private Path outputPath;
	private WorkQueue workers;
	private QueryReaderDatastore searchResult;
	
	/**
	 * 
	 * @param outputPath
	 * 					- The path for writing the output of the search results
	 * 
	 * @param fileName
	 * 					- The name of the input file for searching
	 * 
	 * @param index
	 * 					- The inverted index to execute the search
	 * 
	 * @param numThreads
	 * 					- The number of threads to run the search
	 * 
	 */
	public QueryReader(String outputPath, String fileName, InvertedIndex index, int numThreads)
	{
		this.filePath = FileSystems.getDefault().getPath(fileName);
		this.index = index;
		this.outputPath = FileSystems.getDefault().getPath(outputPath);
		this.workers = new WorkQueue(numThreads);
		this.searchResult = new QueryReaderDatastore();
		
	}
	
	public QueryReader(InvertedIndex index, int numThreads)
	{
		this.index = index;
		this.workers = new WorkQueue(numThreads);
		this.searchResult = new QueryReaderDatastore();
	}
	
	
	public QueryReader(PrintWriter writer, int numThreads)
	{
		this.workers = new WorkQueue(numThreads);
		this.searchResult = new QueryReaderDatastore();
	}
	/**
	 * Reads queries from a file and searches the inverted index
	 * Gets queries line by line and computes tfidf
	 * 
	 */
	public void readQueries()
	{
		try(Scanner queryReader = new Scanner(filePath))
		{
			while(queryReader.hasNextLine())
			{
				String queryLine = queryReader.nextLine().toLowerCase();
				//TODO: this line should be removed
				searchResult.addResults(queryLine, new DocumentResultList(null));
				workers.execute(new QueryWorker(queryLine, index, searchResult));
			}
		}
		catch(IOException ioe)
		{
			ioe.getMessage();
		}
	
		workers.shutdown();
		workers.awaitTermination();
	}
	
	/**
	 * Reads queries from an html and searches the inverted index
	 * Gets queries line by line and computes tfidf
	 * 
	 */
	public void readQueriesFromHtml(String query)
	{
		String queryLine = query.toLowerCase();
		searchResult.addResults(queryLine, new DocumentResultList(null));
		
		DocumentResultList tmp = index.search(queryLine);
		searchResult.addResults(queryLine, tmp);
		//workers.execute(new QueryWorker(queryLine, index, searchResult));
			
		//workers.shutdown();
		//workers.awaitTermination();
	}
	
	/**
	 * Nested class to create runnables for parsing queries
	 * 
	 */
	private class QueryWorker implements Runnable
	{
		private String word;
		private InvertedIndex threadsafeIndex;
		private QueryReaderDatastore searchResult;
		
		/**
		 * Constructor for creating runnables for searching our inverted index 
		 * @param word
		 * 				- The word to be searched
		 * @param threadsafeIndex
		 * 				- The inverted index that is going to be searched
		 * @param searchResult
		 * 				- Stores the results of the search result from each thread into a global search database
		 * 		
		 */
		public QueryWorker(String word, InvertedIndex threadsafeIndex, QueryReaderDatastore searchResult)
		{
			this.word = word;
			this.threadsafeIndex = threadsafeIndex;	
			this.searchResult = searchResult;
		}

		@Override
		public void run()
		{
			DocumentResultList tmp = threadsafeIndex.search(word);
			searchResult.addResults(word, tmp);
		}
	}
	
	public void writeQueryResultsToFile()
	{
		searchResult.writeQueryResultsToFile(outputPath);
	}
	
	public void writeQueryResultsToHTML(PrintWriter outHtml, String query)
	{
		searchResult.writeQueryResultsToHTML(outHtml, query);
	}


}
