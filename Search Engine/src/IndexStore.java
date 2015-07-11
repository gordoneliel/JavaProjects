/**
 * Temporary datastructure for each thead to store for main inverted index
 * 
 * @author gordoneliel
 *
 */
public class IndexStore
{
	private String word;
	private String fileName;
	private int location;

	/**
	 * Constructore for temporary datastructure
	 * 
	 * @param word
	 *            - the word to be added
	 * @param fileName
	 *            - the name of the document where the word is found
	 * @param location
	 *            - the position in the document where the word is found.
	 *            
	 */
	public IndexStore(String word, String fileName, int location)
	{
		this.word = word;
		this.fileName = fileName;
		this.location = location;
	}
	
	/**
	 * @return the word
	 */
	public String getWord()
	{
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word)
	{
		this.word = word;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the location
	 */
	public int getLocation()
	{
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(int location)
	{
		this.location = location;
	}

}
