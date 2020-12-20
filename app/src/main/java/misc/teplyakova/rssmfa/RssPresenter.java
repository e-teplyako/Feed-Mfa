package misc.teplyakova.rssmfa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RssPresenter {
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
		SharedPreferences prefsUrls = view.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_URLS, 0);
		SharedPreferences prefsSubs = view.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_SUBS, 0);
		for (Map.Entry<String, String> entry : countriesMap.entrySet()) {
			prefsUrls.edit().putString(entry.getKey(), entry.getValue()).apply();
			prefsSubs.edit().putBoolean(entry.getKey(), false).apply();
		}

		prefsUrls = view.getSharedPreferences(PREFERENCES_MISC, 0);
		int currentVersionCode = BuildConfig.VERSION_CODE;
		prefsUrls.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
	}

	class RetreivePreferencesTask extends AsyncTask<Void, Void, Set<URL>> {

		@Override
		protected Set<URL> doInBackground(Void... voids) {
			Set<URL> urls = new HashSet<>();
			SharedPreferences prefsUrls = view.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_URLS, 0);
			SharedPreferences prefsSubs = view.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_SUBS, 0);
			try {
				for (Map.Entry<String, ?> entry : prefsUrls.getAll().entrySet()) {
					if (prefsSubs.getBoolean(entry.getKey(), false))
						urls.add(new URL(prefsUrls.getString(entry.getKey(), "")));
				}
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
