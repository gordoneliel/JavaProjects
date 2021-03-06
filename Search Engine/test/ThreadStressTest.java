
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

/**
 * Adapted from sjengle.
 * @author srollins
 *
 */
public class ThreadStressTest {

	private static final int WARM_RUNS = 3;
	private static final int TIME_RUNS = 5;
	private static final int THREADS = 5;


	@Test(timeout = ThreadTest.TIMEOUT * (WARM_RUNS + TIME_RUNS))
	public void testRuntime() {
		double singleAverage = benchmark(String.valueOf(1)) / 1000000000.0;
		double threadAverage = benchmark(String.valueOf(THREADS)) / 1000000000.0;

		System.out.printf("%d Threads: %.2f s%n", 1, singleAverage);
		System.out.printf("%d Threads: %.2f s%n", THREADS, threadAverage);
		System.out.printf("  Speedup: %.2f %n", singleAverage / threadAverage);

		Assert.assertTrue(singleAverage >= threadAverage);
	}

	private double benchmark(String numThreads) {
		long total = 0;
		long start = 0;

		try {
			ProjectTest.copyConfigFile("test/configurations/thread-complex-" + numThreads + ".json", ProjectTest.DEFAULT_CONFIG_DESTINATION);
		} catch(IOException ioe) {
			ioe.printStackTrace();
			fail("Failed to copy configuration to correct location.");        		
		}        


		try {
			for (int i = 0; i < WARM_RUNS; i++) {
				Driver.main(ProjectTest.DEFAULT_ARGS);
			}

			for (int i = 0; i < TIME_RUNS; i++) {
				start = System.nanoTime();
				Driver.main(ProjectTest.DEFAULT_ARGS);
				total += System.nanoTime() - start;
			}
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			Assert.fail(String.format(
					"%n" + "Test Case: %s%n" + "Exception: %s%n",
					"Benchmark: " + numThreads, writer.toString()));
		}

		return (double) total / TIME_RUNS;
	}

}    