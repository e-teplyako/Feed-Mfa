package misc.teplyakova.rssmfa;

import android.os.AsyncTask;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;

public class RssModel {
	public RssModel() {

	}

	public void loadFeed(HashMap<URL, String> urlToCountryCode, LoadFeedCallback callback){
		new LoadFeedTask(callback).execute(urlToCountryCode);
	}

	class LoadFeedTask extends AsyncTask<HashMap<URL, String>, Void, ArrayList<RssItem>> {
		private final LoadFeedCallback callback;

		LoadFeedTask(LoadFeedCallback callback) {
			this.callback = callback;
		}

		@Override
		protected ArrayList<RssItem> doInBackground(HashMap<URL, String>... lists) {
			ArrayList<RssItem> rssItems = new ArrayList<>();
			SyndFeed aggregatedFeed = new SyndFeedImpl();
			aggregatedFeed.setTitle("Aggregated feed");
			List<SyndEntry> entries = new ArrayList();
			aggregatedFeed.setEntries(entries);

			try {
				for (URL url : lists[0].keySet()) {
					CloseableHttpClient client = HttpClients.createMinimal();
					HttpUriRequest request = new HttpGet(url.toURI());
					CloseableHttpResponse response = client.execute(request);
					InputStream is = response.getEntity().getContent();
					SyndFeedInput input = new SyndFeedInput();
					SyndFeed feed = input.build(new XmlReader(is));
					for (SyndEntry entry : feed.getEntries()) {
						RssItem item = new RssItem(lists[0].get(url), entry.getTitle(), entry.getDescription().getValue(), entry.getPublishedDate(), entry.getLink());
						rssItems.add(item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rssItems;
		}

		@Override
		protected void onPostExecute(ArrayList<RssItem> rssItems) {
			if (callback != null)
				callback.onLoad(rssItems);
		}
	}

	interface LoadFeedCallback {
		void onLoad(ArrayList<RssItem> items);
	}
}
