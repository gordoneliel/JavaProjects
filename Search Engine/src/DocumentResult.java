/**
 * Stores a mapping of a document to a tf-idf score
 * Works with DocumentResultList to map a query to a list of DocumentResults
 * 
 * @author gordoneliel
 *
 */
public class DocumentResult implements Comparable<DocumentResult>
{
	private Double score;
	private String doc;
	
	/**
	 * 
	 * @param doc
	 * 			- The document that is going to be mapped to a tf-idf score
	 * @param score
	 * 			- The tf-idf score of a document
	 */
	public DocumentResult(String doc,  Double score)
	{
		this.score = score;
		this.doc = doc;
	}
	

	/**
	 * Compares the tf-idf scores of an input DocumentResult
	 * @param o
	 * 			- Document result to be compared with
	 * Compares two DocumentResults if their scores are equal then it compares them based on document name.
	 */
	public int compareTo(DocumentResult o)
	{
		if (this.score.compareTo(o.score) == 0)
		{
			return -this.doc.compareTo(o.doc);
		}
		else
		{
			return -Double.compare(this.score, o.getScore());
		}
	}
	
	public double getScore() 
	{
		return this.score;
	}
	
	public String getDoc()
	{
		return this.doc;
	}
	
	public String toString() 
	{
		return doc;
	}
	
}
