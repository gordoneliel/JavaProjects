import java.util.ArrayList;
import java.util.UUID;


public class UserInfo extends UserManager{

	private String name;
	private String password;
	private ArrayList<String> searchHistory;
	
	public UserInfo(String name, String password)
	{
		super();
		this.name = name;
		this.password = password;
		this.searchHistory = new ArrayList<String>();
	}
	
	
	public synchronized void addSearchItem(String search) 
	{
		this.searchHistory.add(search);
	}

	public synchronized void delete(int index)
	{
		if(this.searchHistory.size() > index) {
			this.searchHistory.remove(index);
		}
	}


	public synchronized String getName() 
	{
		return name;
	}
	
	public synchronized String listToHtml() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("<table border=1 border-spacing=3px>");
		builder.append("<tr><td colspan=2><b>" + name + "'s ToDo List!</b></td></tr>");
		int i = 0;
		for(String todo: searchHistory) {
			builder.append("<b><tr><td>" + todo + "</td><td><form action=\"list?delete=" + i++ + "\" method=\"post\"><input type=\"submit\" value=\"Done!\"></form></td></tr></b>");
		}
		builder.append("</table>");
		return builder.toString();
	}
	
	
}
