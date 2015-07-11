import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class HTTPFetcher
{

	public static int PORT = 80;

	/**
	 * Downloads an html page for use in the inverted index
	 * @param host
	 * 			- the name of the host url
	 * @param path
	 * 			- the path of the url
	 * @return
	 * 			- returns the html page requested from the provided url
	 */
	public static String download(String host, String path) 
	{
		StringBuffer buf = new StringBuffer();

		try (Socket sock = new Socket(host, PORT);
													
		OutputStream out = sock.getOutputStream(); 
															
															
		InputStream instream = sock.getInputStream(); 
														

		BufferedReader reader = new BufferedReader(new InputStreamReader(instream)))
		{
			// send request
			String request = getRequest(host, path);
			out.write(request.getBytes());
			out.flush();
			
			String line = reader.readLine();
			
			while (line != null)
			{
				buf.append(line + "\n");
											
				line = reader.readLine();
			}

		} 
		catch (IOException e)
		{
			System.out.println("HTTPFetcher::download " + e.getMessage());
		}
		
		return buf.toString();
	}

	/**
	 * Generates a get request for the host url
	 * @param host
	 * @param path
	 * @return
	 */
	private static String getRequest(String host, String path) 
	{
		String request = "GET " + path + " HTTP/1.1" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ "\r\n";								
		return request;
	}
	

}
