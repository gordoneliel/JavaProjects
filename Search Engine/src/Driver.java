import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Driver
{

	public static void main(String[] args) throws Exception
	{
		Configuration indexSettings = new Configuration(FileSystems
				.getDefault().getPath("config.json"));
		try
		{
			indexSettings.init();
		} 
		catch (InitializationException ie)
		{
			ie.getMessage();
		}
		
		String inputPath = indexSettings.getInputPath();
		int numberOfThreads = indexSettings.getNumberOfThreads();
		boolean useDelimiter = indexSettings.useDigitDelimiter();

		Crawler crawler = null;
		if (inputPath != null)
		{
			URL url = null;
			try 
			{
				url = new URL(inputPath);
				String ouputPath = indexSettings.getOutputPath();
				Path outputPath = FileSystems.getDefault().getPath(ouputPath);
				
				crawler = new Crawler(url, useDelimiter, numberOfThreads);
				crawler.searchForLinks(url.toString());
				crawler.getInvertedIndex().printToFile(outputPath);
				
			} 
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
			}
			
			InvertedIndexBuilder theBuilder = null;
			if(url == null)
			{
				theBuilder = new InvertedIndexBuilder(inputPath, useDelimiter, numberOfThreads);
				theBuilder.searchForFiles();
				
				if (indexSettings.getOutputPath() != null)
				{
					String ouputPath = indexSettings.getOutputPath();
					Path outputPath = FileSystems.getDefault().getPath(ouputPath);
					theBuilder.getInvertedIndex().printToFile(outputPath);
				}
			}
			
			if(indexSettings.getSearchPath() != null)
			{
				String searchPath = indexSettings.getSearchPath();
				String searchOutputPath = indexSettings.getSearchOutputPath();
				QueryReader reader = new QueryReader(searchOutputPath, searchPath, url != null ? crawler.getInvertedIndex() : theBuilder.getInvertedIndex(), numberOfThreads);
				reader.readQueries();
				reader.writeQueryResultsToFile();
			}
			
		}
	}

}
