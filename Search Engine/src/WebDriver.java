import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class WebDriver
{
	public static void main(String[] args)
	{
//		if (args.length != 3)
//		{
//			System.out.println("Incorrect number of parameters\n");
//			usage();
//			return;
//		}
		
		System.out.println("Building index... ");

		int threads = Integer.parseInt(args[1]);
		String urlString = args[0];
		Boolean delimeter = Boolean.parseBoolean(args[2]);
		
		Crawler crawler = null;
		URL url = null;
		try 
		{	 
			url = new URL(urlString);
			crawler = new Crawler(url, delimeter, threads);
			crawler.searchForLinks(url.toString());
//			System.out.println("Search Index: " + crawler.getInvertedIndex());
			
//			Path outputPath = FileSystems.getDefault().getPath("/Users/gordoneliel/Desktop/search.txt");
//			crawler.getInvertedIndex().printToFile(outputPath);
		
			
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		InvertedIndexBuilder theBuilder = null;
		if(url == null)
		{
			theBuilder = new InvertedIndexBuilder(urlString, delimeter, threads);
			theBuilder.searchForFiles();
		}
		
//		QueryReader reader = new QueryReader(urlString, crawler.getInvertedIndex(), threads);
//		reader.readQueries();
//		reader.writeQueryResultsToHTML(outHtml);
		
		try
		{
			System.out.println("Starting Web Server");
			new SearchEngineServer().startServer(crawler.getInvertedIndex());
		} catch (Exception e)
		{
		}

	}

	public static void usage()
	{
		System.out.println("\nUSAGE: -w url -q /path/to/query/file");
	}
}
