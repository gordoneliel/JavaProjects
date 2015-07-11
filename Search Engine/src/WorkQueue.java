import java.util.LinkedList;

/**
 * Work queue implementation derived from http://www.ibm.com/developerworks/library/j-jtp0730.html
 * Added and implementation of shutdown and awaitingTermination methods for terminating the queue 
 * 
 * @author gordoneliel
 *
 */
public class WorkQueue
{
	private final PoolWorker[] threads;
	private final LinkedList<Runnable> queue;
	
	private volatile boolean running;

	public WorkQueue(int nThreads)
	{
		queue = new LinkedList<Runnable>();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++)
		{
			threads[i] = new PoolWorker();
			threads[i].start();
		}
		
		this.running = true;
	}

	public void execute(Runnable r)
	{
		if(running)
		{
			synchronized (queue)
			{
				queue.addLast(r);
				queue.notifyAll();
			}
		}
		
	}
	
	/**
	 * Shuts down the work queue
	 */
	public void shutdown()
	{
		running = false;
		synchronized (queue)
		{
			queue.notifyAll();
		}
		// Then notify all waiters
	}
	
	/**
	 * Waits for all threads to complete their tasks
	 */

	public void awaitTermination()
	{
		// stop here until all threads are finished
		// for each thread do a join
		for (PoolWorker poolWorker : threads)
		{
			try
			{
				poolWorker.join();
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private class PoolWorker extends Thread
	{
		public void run()
		{
			Runnable r;

			while (true)
			{
				synchronized (queue)
				{
					while (queue.isEmpty() && running)
					{
						try
						{
							queue.wait();
						} 
						catch (InterruptedException ignored)
						{
							
						}
					}

					//if not runninng break out of while loop, we are done
					if(!running && queue.isEmpty())
					{
						break;
					}
					else
					{
						assert !queue.isEmpty();
						r = (Runnable) queue.removeFirst();
					}
					
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				try
				{
					r.run();
				} catch (RuntimeException e)
				{
					// You might want to log something here
				}
			}
		}
	}
}