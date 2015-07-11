import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.naming.LimitExceededException;

public class Crawler
{
	public static int PORT = 80;
	private WorkQueue workers;
	private boolean digitDelimiter;
	private InvertedIndex invertedIndex;
	private HashMap<String, Boolean> linkMap;
	private LockSmith lockMechanic;
	private int pending = 0;

	public Crawler(URL seedURL, boolean digitDelimiter, int numThreads)
	{
		this.linkMap = new HashMap<>();
		this.workers = new WorkQueue(numThreads);
		this.lockMechanic = new LockSmith();
		this.invertedIndex = new InvertedIndex();

		// searchForLinks(seedURL.toString());
	}
	
	public Crawler(URL seed, int numThreads)
	{
		this.linkMap = new HashMap<>();
		this.workers = new WorkQueue(numThreads);
		this.lockMechanic = new LockSmith();
		this.invertedIndex = new InvertedIndex();
	}

	/**
	 * Searches through all the html links found on the seed url
	 */
	public void searchForLinks(String seed)
	{
		addTocrawledLinkMap(seed, true);
		workers.execute(new CrawlWorker(seed, invertedIndex, digitDelimiter));

		closeWorkers();
		workers.shutdown();
		workers.awaitTermination();
	}

	/**
	 * CrawlWorker creates runnable objects and adds them to our inverted index
	 * 
	 * @author gordoneliel
	 *
	 */
	private class CrawlWorker implements Runnable
	{
		private URL threadURL;
		private ArrayList<IndexStore> tempDatastore;
		private InvertedIndex invertedIndex;
		private String pageURL;
		private Boolean digitDelimiter;

		/**
		 * Constructor that creates runnable objects to read and add words to
		 * our inverted index
		 * 
		 * @param url
		 *            - The url for building the index
		 * @param invertedIndex
		 *            - Indicates whether to use digits as a delimiter or not
		 */
		public CrawlWorker(String url, InvertedIndex invertedIndex,
				boolean digitDelimeter)
		{
			this.tempDatastore = new ArrayList<>();
			this.invertedIndex = invertedIndex;
			this.pageURL = url;
			this.digitDelimiter = digitDelimeter;

			incrementPending();

			try
			{
				threadURL = new URL(url);
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void run()
		{
			String page = null;
			try
			{
				// get page
				page = HTTPFetcher.download(threadURL.getHost(),
						threadURL.getPath());

				// Get links for my page
				ArrayList<String> links = HTMLLinkParser.listLinks(page);

				for (String link : links)
				{
					addLink(link);
				}

				// Cleaned up page for indexing
				page = HTMLCleaner.cleanHTML(page);

				// build ii
				addToInvertedIndex(page, pageURL);

				invertedIndex.batchAdd(tempDatastore);

			} catch (Exception e)
			{
				System.out.println(e.getMessage());
			}

			decrementPending();

		}

		/**
		 * Adds a url link to the work queue for execution
		 * 
		 * @param link
		 *            - The url to be crawled
		 * @throws MalformedURLException
		 */
		private void addLink(String link) throws MalformedURLException
		{
			int indexOf = link.lastIndexOf('#');
			link = (indexOf == -1) ? link : link.substring(0, indexOf);
			// get the absolute url
			URL base = new URL(this.pageURL);
			URL absolute = link.startsWith("http://") ? new URL(link)
					: new URL(base, link);
			String validLink = absolute.toString();

			crawlLinks(validLink);
		}

		/**
		 * Executes threads to crawl the links found on a seed webpage
		 * 
		 * @param validLink
		 *            - The link to be crawled
		 */
		private synchronized void crawlLinks(String validLink)
		{
			if (linkMap.size() < 50 && !linkMap.containsKey(validLink))
			{
				addTocrawledLinkMap(validLink, true);
				workers.execute(new CrawlWorker(validLink, invertedIndex,
						digitDelimiter));
			}
		}

		/**
		 * Adds a page to our inverted index
		 * 
		 * @param page
		 *            - the page to be indexed
		 * @param urlLocation
		 *            - the url location of the page
		 */
		private void addToInvertedIndex(String page, String urlLocation)
		{
			Pattern pattern = null;
			int wordCount = 0;
			String urlPage = page;

			if (digitDelimiter)
			{
				pattern = Pattern.compile("[^a-zA-Z]+");
			} else
			{
				pattern = Pattern.compile("[^a-zA-Z0-9]+");
			}

			try (Scanner fileScanner = new Scanner(urlPage)
					.useDelimiter(pattern))
			{
				while (fileScanner.hasNext())
				{
					String nextToken = fileScanner.next();
					tempDatastore.add(new IndexStore(nextToken, urlLocation,
							++wordCount));
				}
			}
		}
	}

	/**
	 * Checks if there are still jobs to be done, waits for termination
	 */
	private synchronized void closeWorkers()
	{
		while (pending > 0)
		{
			try
			{
				this.wait();

			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Thread safe structure for storing crawled links
	 * 
	 * @param link
	 *            - A link that is about to be crawled
	 * @param state
	 *            - the crawled state of the link
	 */
	private synchronized void addTocrawledLinkMap(String link, boolean state)
	{
		linkMap.put(link, state);
	}

	/**
	 * Increments the pending value
	 */
	private synchronized void incrementPending()
	{
		pending++;
	}

	/**
	 * Notifies all threads for shutdown
	 */
	private synchronized void decrementPending()
	{
		pending--;
		if (pending <= 0)
		{
			this.notifyAll();
		}
	}

	/**
	 * @return the invertedIndex
	 */
	public InvertedIndex getInvertedIndex()
	{
		return invertedIndex;
	}

	public static void main(String arg[])
	{
		URL url = null;
		try
		{
			url = new URL("http://www.cs.usfca.edu/~cs212/birds/birds.html");
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		Crawler crawl = new Crawler(url, true, 10);
	}
}
