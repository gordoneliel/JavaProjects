import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Maintains a mapping from a word to a list of documents and positions in those
 * documents where the word was found. See the project description for more
 * information:
 * https://github.com/CS212-S15/projects/blob/master/specifications/project1.md
 * 
 * @author gordoneliel
 *
 */
public class InvertedIndex
{

	/**
	 * It is up to you to decide what the data members for this class will look
	 * like, but make sure to think about efficiency!
	 * 
	 * Hint, think about how to use objects of type DocumentLocationMap.
	 */
	private TreeMap<String, DocumentLocationMap> wordMap;
	private HashMap<String, Integer> docsToCount;
	private LockSmith lockMechanic;

	/**
	 * Constructor to instantiate a new InvertedIndex
	 */
	public InvertedIndex()
	{
		this.setWordMap(new TreeMap<String, DocumentLocationMap>());
		this.docsToCount = new HashMap<String, Integer>();
		this.lockMechanic = new LockSmith();
	}

	/**
	 * Adds a new word to the index. If the word is already in the index, the
	 * method simply adds a new document/position.
	 * 
	 * @param word
	 *            - the word to be added
	 * @param fileName
	 *            - the name of the document where the word is found
	 * @param location
	 *            - the position in the document where the word is found.
	 */
	public void add(String word, String fileName, int location)
	{
		lockMechanic.acquireWriteLock();
		word = word.toLowerCase();
		//Only count documents that have words in them
		if(location > 0)
		{
			docsToCount.put(fileName, location);
		}
		// Check tolowercase to avoid duplicate entries for capital letters
		if (getWordMap().containsKey(word))
		{
			getWordMap().get(word).addLocation(fileName, location);
		} else
		{
			DocumentLocationMap locationMap = new DocumentLocationMap(word);
			locationMap.addLocation(fileName, location);
			getWordMap().put(word, locationMap);
		}
		lockMechanic.releaseWriteLock();
	}

	/**
	 * Adds each thread's inverted index data to the main inverted index
	 * 
	 * @param tempData
	 * 				- An arraylist that stores a thread's inverted index
	 */
	public void batchAdd(ArrayList<IndexStore> tempData)
	{
		for (IndexStore indexStore : tempData)
		{
			this.add(indexStore.getWord(), indexStore.getFileName(), indexStore.getLocation());
		}	
	}
		


	/**
	 * Returns a string representation of the index. See the project
	 * specification for the required format of the String representation of the
	 * index. Your output must match exactly to pass all tests.
	 */
	public String toString()
	{
		StringBuffer docMapString = new StringBuffer();
		for (String wordName : this.getWordMap().keySet())
		{
			docMapString.append(wordName + "\n" + getWordMap().get(wordName) + "\n");
		}
		return docMapString.toString().trim();
	}

	/**
	 * Optional method. I used this method to save the string representation of
	 * the index to a file.
	 * 
	 * @param fileName
	 */
	public void printToFile(Path fileName)
	{
		try (BufferedWriter output = new BufferedWriter(new FileWriter(fileName
				.toFile().getAbsolutePath())))
		{
			for (String wordName : this.getWordMap().keySet())
			{
				output.write(wordName + "\n" + getWordMap().get(wordName) + "\n");
			}
		} catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Searches the invered index for the query and computes the tfidf of each
	 * line of words in the query
	 * 
	 * @param query
	 *            - The query to be made on the inverted index
	 * 
	 * @return DocumentResult - A query mapping to a list of tf-idf scoring
	 *         results
	 */
	public DocumentResultList search(String query)
	{
		return computeTFIDF(query);
	}

	/**
	 * Computes the TF-IDF of a line of query
	 * 
	 * @param query
	 *            - A single line of query
	 * @return DocumentResultList - A query mapping to a list of tf-idf scoring
	 *         results
	 */
	public DocumentResultList computeTFIDF(String query)
	{	
		DocumentResultList resultList = new DocumentResultList(query);
		String[] queries = query.split("\\s+");
		
		for (int i = 0; i < queries.length; i++)
		{
			System.out.println("Queries " + queries[i]);
		}
		//lockMechanic.acquireWriteLock();
		lockMechanic.acquireReadLock();
		// The total number of documents
		double totalDocs = docsToCount.size();

		TreeMap<String, Double> tempScore = new TreeMap<>();
		
		for (String oneQuery : queries)
		{
			boolean contains = getWordMap().containsKey(oneQuery);
			
			if (contains)
			{
				DocumentLocationMap queryDocuments = getWordMap().get(oneQuery);

				/* each doc where the word appears */
				for (String doc : queryDocuments.getIndexMap().keySet())
				{
					// calc tfidf
					double tfidf = 0;
					double wordFreq = getWordMap().get(oneQuery).locationNumber(doc);
					double totalWords = docsToCount.get(doc);
					double tf = tfCalculator(wordFreq, totalWords);
					double idf = idfCalc(totalDocs, getWordMap().get(oneQuery).getIndexMap().size());
					tfidf += tf * idf;
					if (tempScore.containsKey(doc))
					{
						// update the tfidf
						double tmp = tempScore.get(doc) + tfidf;
						tempScore.put(doc, tmp);
					} else
					{
						tempScore.put(doc, tfidf);
					}
				}
			}
		}
		
		for (String doc : tempScore.keySet())
		{
			resultList.add(new DocumentResult(doc, tempScore.get(doc)));
		}
		
		
		lockMechanic.releaseReadLock();
		//lockMechanic.releaseWriteLock();
		return resultList;
	}

	/**
	 * Calculates the term frequency of one word in one document
	 * 
	 * @param termFreq
	 *            - The number of times a word appears in a document
	 * 
	 * @param totalCount
	 *            - The total number of words in a document
	 * 
	 * @return double - Returns frequency of words / total words in document
	 */
	public double tfCalculator(double termFreq, double totalCount)
	{
		if (totalCount == 0)
		{
			return 0.0;
		}
		return termFreq / totalCount;
	}

	/**
	 * Calculates the inverse document frequency of a word in all documents
	 * 
	 * @param totalDocs
	 *            - The total number of documents in our inverted index
	 * 
	 * @param totalWordAppearance
	 *            - The total number of documents a word appears in
	 * 
	 * @return double - Returns the log10 of total documents / total number of
	 *         documents a word appears in
	 * 
	 */
	public double idfCalc(double totalDocs, double totalWordAppearance)
	{
		double inverseDocumentFreq = 0;

		if (totalWordAppearance == 0 || totalDocs == 0)
		{
			return 0;
		}

		inverseDocumentFreq = Math.log10(totalDocs / totalWordAppearance);
		return inverseDocumentFreq;
	}

	public static void main(String[] args)
	{
		InvertedIndex ii = new InvertedIndex();
		ii.add("hello", "test1", 1);
		ii.add("hello", "test1", 10);
		ii.add("animal", "test1", 4);
		ii.add("hello", "test2", 8);
		ii.add("animal", "test2", 1);
		ii.add("zip", "test5", 14);
		ii.add("time", "test23", 21);
		ii.add("home", "test23", 47);
		// System.out.println(ii);

	}

	public TreeMap<String, DocumentLocationMap> getWordMap()
	{
		return wordMap;
	}

	public void setWordMap(TreeMap<String, DocumentLocationMap> wordMap)
	{
		this.wordMap = wordMap;
	}

}
