/**
 * Custom locking mechanism for reading and writing
 * 
 * @author gordoneliel
 *
 */
public class LockSmith
{
	private int readers;
	private int writers;

	public LockSmith()
	{
		readers = 0;
		writers = 0;
	}

	/**
	 * Acquires a read lock for a thread
	 */
	public synchronized void acquireReadLock()
	{
		while (writers > 0)
		{
			try
			{
				wait();
			} catch (InterruptedException ie)
			{
				System.out.println(ie.getMessage());
			}
		}
		readers++;
	}

	/**
	 * Acquires a write lock for a thread
	 */
	public synchronized void acquireWriteLock()
	{
		while (readers > 0 || writers > 0)
		{
			try
			{
				wait();
			} catch (InterruptedException ie)
			{
				System.out.println(ie.getMessage());
			}
		}
		writers++;
	}

	/**
	 * Releases a read lock held by a thread
	 */
	public synchronized void releaseReadLock()
	{
		assert readers > 0;
		readers--;
		notifyAll();

	}

	/**
	 * Releases a write lock held by a thread
	 */
	public synchronized void releaseWriteLock()
	{
		assert writers > 0;
		writers--;
		notifyAll();

	}

}