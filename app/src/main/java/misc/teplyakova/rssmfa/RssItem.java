package misc.teplyakova.rssmfa;
import java.util.Date;

public class RssItem implements Comparable<RssItem> {
	private String countryCode;
	private String title;
	private String description;
	private Date pubDate;
	private String link;

	public RssItem(String countryCode, String title, String description, Date pubDate, String link) {
		this.countryCode = countryCode;
		this.title = title;
		this.description = description;
		this.pubDate = pubDate;
		this.link = link;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public String getTitle() {
		return this.title;
	}

	public String getLink()	{
		return this.link;
	}

	public String getDescription() {
		return this.description;
	}

	public Date getPubDate() {
		if (pubDate == null)
			return new Date();
		return this.pubDate;
	}

	public int compareTo(RssItem o) {
		return this.getPubDate().compareTo(o.getPubDate()) * -1;
	}
}
