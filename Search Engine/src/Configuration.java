import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.core.IsInstanceOf;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The Configuration class parses a JSON file containing configuration
 * information and provides wrapper methods to access the configuration data in
 * the JSON object.
 * 
 * @author srollins
 *
 */
public class Configuration
{

	/**
	 * Constants that store the keys used in the configuration file.
	 */
	public static final String URL_PATH = "urlPath";
	public static final String INPUT_PATH = "inputPath";
	public static final String OUTPUT_PATH = "outputPath";
	public static final String DIGIT_DELIMITER = "digitDelimiter";
	
	public static final String SEARCH_PATH = "searchPath";
	public static final String SEARCH_OUTPUT_PATH = "searchOutputPath";
	public static final String NUMBER_THREADS = "numberThreads";
	
	private Path path;
	private JSONParser parser;
	
	private String inputPath;
	private String outputPath;
	private String searchPath;
	private String searchOutputPath;
	private int numberOfThreads;
	private boolean useDigitDelimiter;
	/**
	 * TODO: Instance variables to store shared information.
	 */

	/**
	 * Instantiates a Configuration object.
	 * 
	 * @param path
	 *            - the location of the file
	 */
	public Configuration(Path path)
	{
		this.path = path;
		this.parser = new JSONParser();
		this.numberOfThreads = 0;
	}

	/**
	 * Initializes a Configuration object. Uses a JSONParser to parse the
	 * contents of the file. Hint: I used a helper method to validate the
	 * contents of the file once it was parsed. Note: you will need to implement
	 * your own exception class called InitializationException.
	 * 
	 * @throws InitializationException
	 *             - thrown in the following cases: (1) an IOException is
	 *             generated when accessing the file; (2) a ParseException is
	 *             thrown when parsing the JSON contents of the file; (3) the
	 *             file does not contain the inputPath key; (4) the file does
	 *             not contain the digitDelimiter key; (5) the digitDelimiter
	 *             value is not a boolean.
	 */
	public void init() throws InitializationException
	{	
		JSONObject configurationObj;
		
		try(BufferedReader in = Files.newBufferedReader(path, Charset.forName("UTF-8")))
		{
			configurationObj = (JSONObject) parser.parse(in);			
		} 
		
		catch (IOException ioe)
		{
			throw new InitializationException("Unable to open file");
		}
		
		catch ( ParseException iope)
		{
			throw new InitializationException("Unable to parse file");
		}
		
		// Get input from Json
		checkInput(configurationObj);
	}

	/**
	 * Checks the input, output, searchPath and useDigitDelimiter values
	 * @param configurationObj 
	 * @throws InitializationException
	 */
	public void checkInput(JSONObject configurationObj) throws InitializationException
	{	
		if(!configurationObj.containsKey(INPUT_PATH))
		{
			throw new InitializationException("inputPath not specified");
		}
		
		inputPath = (String)configurationObj.get(INPUT_PATH);
		outputPath = (String)configurationObj.get(OUTPUT_PATH);
		searchPath = (String)configurationObj.get(SEARCH_PATH);
		searchOutputPath = (String)configurationObj.get(SEARCH_OUTPUT_PATH);
		
		Object threadCountObj = configurationObj.get(NUMBER_THREADS);
		if(threadCountObj != null)
		{
			Double comparator = Double.parseDouble(threadCountObj.toString());
			if(threadCountObj instanceof String || threadCountObj instanceof Double ||
					comparator > 1000 || comparator < 1)
			{
				throw new InitializationException("numberThreads is not a valid integer value");
			}
			else
			{
				String configObj = threadCountObj.toString();
				numberOfThreads = Integer.parseInt(configObj);
			}
		}
		else
		{
			if(numberOfThreads < 1)
			{
				numberOfThreads = 5;
			}
		}
		

//		else
//		{
//			throw new InitializationException("numberThreads is not a valid integer value");
//		}
		
		if(searchOutputPath == null)
		{
			searchOutputPath = "results/default.txt";
		}
		
		if(configurationObj.get(DIGIT_DELIMITER) == null )
		{
			throw new InitializationException("digitDelimiter not specified");
		}

		if(!(configurationObj.get(DIGIT_DELIMITER) instanceof Boolean))
		{
			throw new InitializationException("digitDelimiter not a boolean");
		}
		
		useDigitDelimiter = (boolean) configurationObj.get(DIGIT_DELIMITER);
		

	}
	
	/**
	 * Returns the value of associated with the searchPath key in the JSON
	 * configuration file.
	 * 
	 * @return the searchPath
	 */
	public String getSearchPath()
	{
		return searchPath;
	}

	/**
	 * Returns the value of associated with the searchOutputPath key in the JSON
	 * configuration file.
	 * 
	 * @return the searchOutputPath
	 */
	public String getSearchOutputPath()
	{
		return searchOutputPath;
	}

	/**
	 * Returns the value of associated with the inputPath key in the JSON
	 * configuration file.
	 * 
	 * @return - value associated with key inputPath
	 */
	public String getInputPath()
	{
		return inputPath;
	}

	/**
	 * Returns the value of associated with the outputPath key in the JSON
	 * configuration file.
	 * 
	 * @return - value associated with key outputPath - null if no outputPath
	 *         specified
	 */
	public String getOutputPath()
	{
		return outputPath;
	}

	/**
	 * Returns the value of associated with the digitDelimiter key in the JSON
	 * configuration file.
	 * 
	 * @return - value associated with key digitDelimiter
	 */
	public boolean useDigitDelimiter()
	{
		return useDigitDelimiter;
	}

	
	/**
	 * @return the numberOfThreads
	 */
	public int getNumberOfThreads()
	{
		return numberOfThreads;
	}

	/**
	 * @param numberOfThreads the numberOfThreads to set
	 */
	public void setNumberOfThreads(int numberOfThreads)
	{
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * Simple main method used for in-progress testing of Configuration class
	 * only.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		(new Configuration(FileSystems.getDefault().getPath("config.json")))
				.init();
	}

}
