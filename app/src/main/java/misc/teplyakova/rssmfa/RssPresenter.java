package misc.teplyakova.rssmfa;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RssPresenter {
	String feedUrl = "https://www.diplomatie.gouv.fr/spip.php?page=backend-fd&lang=en";
			//"https://www.diplomatie.gouv.fr/spip.php?page=backend-fd&lang=en";
			//"https://www.state.gov/rss-feed/africa/feed/";
			//"http://www.mfa.gov.kp/en/feed/";
	private MainActivity view;
	private final RssModel model;

	public RssPresenter(RssModel model) {
		this.model = model;
	}

	public void attachView(MainActivity view) {
		this.view = view;
	}

	public void detachView() {
		view = null;
	}

	public void viewIsReady() {
		loadFeed();
	}

	private void loadFeed() {
		Set<URL> urls = new HashSet<>();
		try {
			URL url = new URL("https://www.diplomatie.gouv.fr/spip.php?page=backend-fd&lang=en");
			urls.add(url);
			url = new URL("https://www.state.gov/rss-feed/africa/feed/");
			urls.add(url);
			url = new URL("http://www.mfa.gov.kp/en/feed/");
			urls.add(url);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		model.loadFeed(urls, new RssModel.LoadFeedCallback() {
			@Override
			public void onLoad(ArrayList<RssItem> items) {
				view.showFeed(items);
			}
		});
	}
}
