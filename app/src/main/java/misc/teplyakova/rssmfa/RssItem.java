package misc.teplyakova.rssmfa;
import java.util.Date;

public class RssItem implements Comparable<RssItem> {
	private String title;
	private String description;
	private Date pubDate;
	private String link;

	public RssItem(String title, String description, Date pubDate, String link) {
		this.title = title;
		this.description = description;
		this.pubDate = pubDate;
		this.link = link;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getLink()
	{
		return this.link;
	}

	public String getDescription()
	{
		return this.description;
	}

	public Date getPubDate()
	{
		return this.pubDate;
	}

	public int compareTo(RssItem o) {
		return this.pubDate.compareTo(o.pubDate) * -1;
	}
}
