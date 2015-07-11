import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Stores a mapping from a word to a list of DocumentResult objects 
 *  sorted by tf-idf scores in decending order
 * 
 * @author gordoneliel
 *
 */
public class DocumentResultList extends TreeSet<DocumentResult>
{
	private String word;

	/**
	 * Constructor to initialize a DocumentResultList to map
	 * a query to a list of DocumentResult objects
	 *  
	 * @param word
	 * 				- The Query word to by added
	 */
	public DocumentResultList(String word)
	{
		super();
		this.word = word;
	}

	/**
	 * Adds a DocumentResult to a query
	 */
	public boolean add(DocumentResult e)
	{
		return super.add(e);
	}

//	public void setTreeMap(TreeMap<String, Double> temp)
//	{
//		super() = temp;
//	}
	/**
	 * @return the word
	 */
	public String getWord()
	{
		return word;
	}
	
	@Override
	public String toString()
	{
		return super.toString();
	}

	//TODO
	public void print() {
		System.out.println(super.toString());
	}

}
