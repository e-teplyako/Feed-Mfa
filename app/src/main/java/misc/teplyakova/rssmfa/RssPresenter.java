package misc.teplyakova.rssmfa;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

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
		new RetreivePreferencesTask().execute();
	}

	class RetreivePreferencesTask extends AsyncTask<Void, Void, Set<URL>> {

		@Override
		protected Set<URL> doInBackground(Void... voids) {
			Set<URL> urls = new HashSet<>();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getApplicationContext());
			try {
				if (preferences.getBoolean("DPRK", false))
					urls.add(new URL("http://www.mfa.gov.kp/en/feed/"));
				if (preferences.getBoolean("France", false))
					urls.add(new URL("https://www.diplomatie.gouv.fr/spip.php?page=backend-fd&lang=en"));
				if (preferences.getBoolean("USA", false))
					urls.add(new URL("https://www.state.gov/rss-feed/africa/feed/"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return urls;
		}

		@Override
		protected void onPostExecute(Set<URL> urls) {
			model.loadFeed(urls, new RssModel.LoadFeedCallback() {
				@Override
				public void onLoad(ArrayList<RssItem> items) {
					view.showFeed(items);
				}
			});
		}
	}
}
