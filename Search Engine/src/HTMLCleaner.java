import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class does not take a particularly efficient approach, but this
 * simplifies the process of cleaning HTML code for your web crawler 
 * project later.
 */

/**
 * A helper class with several static methods that will help strip out all of
 * the HTML. Meant to be used for the web crawler project.
 *
 * @see HTMLCleaner
 * @see HTMLCleanerTest
 */
public class HTMLCleaner
{

	
	
	/**
	 * Removes everything between the element tags, and the element tags
	 * themselves. For example, consider the html code:
	 *
	 * <pre>
	 * &lt;style type="text/css"&gt;body { font-size: 10pt; }&lt;/style&gt;
	 * </pre>
	 *
	 * If removing the "style" element, all of the above code will be removed,
	 * and replaced with the empty string.
	 *
	 * @param name
	 *            - name of the element to strip, like style or script
	 * @param html
	 *            - html code to parse
	 * @return html code without the element specified
	 */
	public static String stripElement(String name, String html)
	{
		String pattern = "(?si)<\\s*" + name + ".*?<\\s*/\\s*" + name + "\\s*>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String result = m.replaceAll("");
		return result;
	}

	/**
	 * Removes all HTML tags, which is essentially anything between the < and >
	 * symbols. The tag will be replaced by the empty string.
	 *
	 * @param html
	 *            - html code to parse
	 * @return text without any html tags
	 */
	public static String stripTags(String html)
	{
		String pattern = "(?is)<.*?>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String result = m.replaceAll("");
		return result;
	}

	/**
	 * Replaces all HTML entities in the text with a space. For example,
	 * "2010&ndash;2012" will become "2010 2012".
	 *
	 * @param html
	 *            - the text with html code being checked
	 * @return text with HTML entities replaced by a space
	 */
	public static String stripEntities(String html)
	{
		String pattern = "(&\\S+?;)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String result = m.replaceAll(" ");
		return result;
	}

	
	/**
	 * Clean the headers of the html page
	 * @param html
	 * 				- the text with html code being checked
	 */
	public static String stripHeaders(String html)
	{ 
		String pattern = "(?sm)HTTP//?" + "(.+?:.+?)^\\s*$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(html);
		String result = m.replaceAll("");
		return result;
	}
	
	/**
	 * Removes all style and script tags (and any text in between those tags),
	 * all HTML tags, and all special characters/entities.
	 *
	 * THIS METHOD IS PROVIDED FOR YOU. DO NOT MODIFY.
	 *
	 * @param html
	 *            - html code to parse
	 * @return plain text
	 */
	public static String cleanHTML(String html)
	{
		String text = html;
		text = stripHeaders(text);
		text = stripElement("script", text);
		text = stripElement("style", text);
		text = stripTags(text);
		text = stripEntities(text);
		return text;
	}
}
