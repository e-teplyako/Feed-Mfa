package misc.teplyakova.rssmfa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;

public class RssModel {
	private static RssModel instance;
	private ArrayList<RssItem> items;

	private RssModel() {

	}

	public static RssModel getInstance() {
		if (instance == null)
			instance = new RssModel();
		return instance;
	}

	public void loadFeed(Context context, LoadFeedCallback callback){
		new LoadFeedTask(context, callback).execute();
	}

	public ArrayList<RssItem> getItems() {
		return items;
	}

	class LoadFeedTask extends AsyncTask<Void, Void, ArrayList<RssItem>> {
		private final LoadFeedCallback callback;
		private Context context;

		LoadFeedTask(Context context, LoadFeedCallback callback) {
			this.callback = callback;
			this.context = context;
		}

		@Override
		protected ArrayList<RssItem> doInBackground(Void... voids) {
			SharedPreferences prefsUrls = context.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_URLS, 0);
			SharedPreferences prefsSubs = context.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_SUBS, 0);ArrayList<RssItem> rssItems = new ArrayList<>();
			SyndFeed aggregatedFeed = new SyndFeedImpl();
			aggregatedFeed.setTitle("Aggregated feed");
			List<SyndEntry> entries = new ArrayList();
			aggregatedFeed.setEntries(entries);

			for (Map.Entry<String, ?> entry : prefsUrls.getAll().entrySet()) {
				if (prefsSubs.getBoolean(entry.getKey(), false)) {
					SyndFeed feed = loadFeedForUrl(prefsUrls.getString(entry.getKey(), ""));
					for (SyndEntry feedEntry : feed.getEntries()) {
						RssItem item = new RssItem(entry.getKey(), feedEntry.getTitle(), feedEntry.getDescription().getValue(), feedEntry.getPublishedDate(), feedEntry.getLink());
						rssItems.add(item);
					}
				}
			}
			items = rssItems;
			return rssItems;
		}

		SyndFeed loadFeedForUrl(String stringUrl) {
			URL url = null;
			try {
				url = new URL(stringUrl);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			if (url == null)
				return new SyndFeedImpl();

			CloseableHttpClient client = HttpClients.createMinimal();
			InputStream is = null;
			try {
				HttpUriRequest request = new HttpGet(url.toURI());
				CloseableHttpResponse response = client.execute(request);
				is = response.getEntity().getContent();
			}
			catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = new SyndFeedImpl();
			try {
				feed = input.build(new XmlReader(is));
			}
			catch (IOException | FeedException e) {
				e.printStackTrace();
			}
			return feed;
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
