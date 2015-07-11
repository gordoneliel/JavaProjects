import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class that builds the InvertedIndex.
 * 
 * @author gordoneliel
 *
 */
public class InvertedIndexBuilder 
{

	/**
	
	It is up to you to design this class. 
	This class will need to recursively traverse an input directory and any time it finds a file
	that has the .txt extension (case insensitive!), it will process the file and add all words to 
	the InvertedIndex.
	
	
	**/
	private Path path;
	private boolean digitDelimiter = false;
	private File inputFile;
	private InvertedIndex invertedIndex;
	private LockSmith lockMechanic;
	private WorkQueue workers;
	
	public InvertedIndexBuilder(String path, boolean digitDelimiter, int numberOfThreads)
	{
		this.path = FileSystems.getDefault().getPath(path);
		this.digitDelimiter = digitDelimiter;
		this.invertedIndex = new InvertedIndex();
		this.lockMechanic = new LockSmith();
		this.workers = new WorkQueue(numberOfThreads);
	}
	
	/**
	 * Searches an input directory for a .txt files
	 */
	public void searchForFiles()
	{
		search(this.path.toString());
		workers.shutdown();
		workers.awaitTermination();
	}
	
	/**
	 * Recursively searches an input directory for all files with a .txt extension
	 * @param inputDirName
	 * 					- the diretory to be searched
	 */
	public void search(String inputDirName)
	{
		File rootDirs = new File(inputDirName);
		
		// Error checking for wrong directory path
		if(!rootDirs.exists()) return;
		
		for(File subDirs: rootDirs.listFiles())
		{
			if(subDirs.isDirectory())
			{
				search(subDirs.toString());
			}
			else
			{
				if(subDirs.isFile() && subDirs.getName().toLowerCase().endsWith("txt") )
				{
					workers.execute(new IndexWorker(subDirs.getPath(), digitDelimiter, invertedIndex));
				}
			}
		}
	}
	
	/**
	 * IndexWorker creates runnable objects and adds them to our inverted index 
	 * @author gordoneliel
	 *
	 */
	private class IndexWorker implements Runnable
	{
		private String path;
		private Boolean digitDelimiter;
		private InvertedIndex localIndex;
		private InvertedIndex invertedIndex;
		private ArrayList<IndexStore> tempDatastore;
		/**
		 * Constructor that creates runnable objects to read and add words to our inverted index
		 * @param path
		 * 				- The path of a .txt file for building the index
		 * @param digitDelimiter
		 * 				- Indicates whether to use digits as a delimiter or not
		 */
		public IndexWorker(String path, Boolean digitDelimiter, InvertedIndex invertedIndex)
		{
			this.path = path;
			this.digitDelimiter = digitDelimiter;
			this.localIndex = new InvertedIndex();
			this.invertedIndex = invertedIndex;
			this.tempDatastore = new ArrayList<>();
		}
		
		@Override
		public void run()
		{
			readTxtFiles(path, digitDelimiter);
			invertedIndex.batchAdd(tempDatastore);
		}
		
		/**
		 * Reads .txt files for invertedIndex
		 * @param txtfileName
		 * 					- the name of the file to be added to the inverted index
		 * @param digitDelimiter
		 * 					- specifies whether to use digits as a delimiter
		 */
		private void readTxtFiles(String txtfileName, boolean digitDelimiter) 
		{
			Pattern pattern = null;
			int wordCount = 0; 
			Path textFilePath = FileSystems.getDefault().getPath(txtfileName);

			if(digitDelimiter)
			{
				pattern = Pattern.compile("[^a-zA-Z]+");
			}
			else
			{
				pattern = Pattern.compile("[^a-zA-Z0-9]+");
			}
			
			try(Scanner fileScanner = new Scanner(textFilePath).useDelimiter(pattern)) 
			{
				while(fileScanner.hasNext())
				{
					String nextToken = fileScanner.next();
					//localIndex.add(nextToken, txtfileName, ++wordCount);
					tempDatastore.add(new IndexStore(nextToken, txtfileName, ++wordCount));
				}
			} 
			catch (IOException ioe) 
			{
				System.out.println("InvertedIndexBuilder :: Problem opening file");
				System.out.println(ioe.getMessage());
				wordCount = -1;
			}
		}
		
	}	
	
	public InvertedIndex getInvertedIndex()
	{
		return invertedIndex;
	}
	
	public Path getPath()
	{
		return path;
	}

	public File getInputFile()
	{
		return inputFile;
	}


}
