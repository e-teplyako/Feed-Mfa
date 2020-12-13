package misc.teplyakova.rssmfa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RssPresenter {
	private final String PREFERENCES_COUNTRIES = "pref_countries";
	private final String PREFERENCES_MISC = "pref_miscellaneous";
	private final String PREF_VERSION_CODE_KEY = "version_code";
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
		if (checkFirstRun())
			initFirstTime();
		SharedPreferences prefs = view.getSharedPreferences(PREFERENCES_COUNTRIES, 0);
		Log.e(getClass().getSimpleName(), prefs.getAll().toString());
		loadFeed();
	}

	private void loadFeed() {
		new RetreivePreferencesTask().execute();
	}

	private boolean checkFirstRun() {
		final int DOES_NOT_EXIST = -1;
		int currentVersionCode = BuildConfig.VERSION_CODE;
		SharedPreferences prefs = view.getSharedPreferences(PREFERENCES_MISC, 0);
		int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOES_NOT_EXIST);
		return (savedVersionCode != currentVersionCode);
	}

	private void initFirstTime() {
		TreeMap<String, String> countriesMap = FileUtils.parseJson(FileUtils.readFileToString(view, FileUtils.fileName));
		SharedPreferences prefs = view.getSharedPreferences(PREFERENCES_COUNTRIES, 0);
		for (Map.Entry<String, String> entry : countriesMap.entrySet()) {
			prefs.edit().putString(entry.getKey(), entry.getValue()).apply();
			prefs.edit().putBoolean(entry.getKey(), false).apply();
		}

		prefs = view.getSharedPreferences(PREFERENCES_MISC, 0);
		int currentVersionCode = BuildConfig.VERSION_CODE;
		prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
	}

	class RetreivePreferencesTask extends AsyncTask<Void, Void, Set<URL>> {

		@Override
		protected Set<URL> doInBackground(Void... voids) {
			Set<URL> urls = new HashSet<>();
			SharedPreferences preferences = view.getSharedPreferences(PREFERENCES_COUNTRIES, 0);
			try {
				if (preferences.getBoolean("DPRK", false))
					urls.add(new URL("http://www.mfa.gov.kp/en/feed/"));
				if (preferences.getBoolean("France", false))
					urls.add(new URL("https://www.diplomatie.gouv.fr/spip.php?page=backend-fd&lang=en"));
				if (preferences.getBoolean("USA", false))
					urls.add(new URL("https://www.state.gov/rss-feed/africa/feed/"));
				urls.add(new URL("https://www.mfa.bg/en/rss"));
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
