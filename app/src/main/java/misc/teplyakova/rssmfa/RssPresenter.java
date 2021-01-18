package misc.teplyakova.rssmfa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RssPresenter {
	private static RssPresenter instance;
	private final String PREFERENCES_MISC = "pref_miscellaneous";
	private final String PREF_VERSION_CODE_KEY = "version_code";
	private MainActivity view;

	private RssPresenter() {

	}

	public static RssPresenter getInstance() {
		if (instance == null)
			instance = new RssPresenter();
		return instance;
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

	public void viewHasBeenRecreated(boolean isInFeedMode) {
		if (isInFeedMode)
			view.showFeed(RssModel.getInstance().getItems());
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

	class RetreivePreferencesTask extends AsyncTask<Void, Void, HashMap<URL, String>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			view.showProgressBar();
		}

		@Override
		protected HashMap<URL, String> doInBackground(Void... voids) {
			HashMap urls = new HashMap();
			SharedPreferences prefsUrls = view.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_URLS, 0);
			SharedPreferences prefsSubs = view.getSharedPreferences(PreferenceActivity.SettingsFragment.PREFERENCES_COUNTRIES_SUBS, 0);
			try {
				for (Map.Entry<String, ?> entry : prefsUrls.getAll().entrySet()) {
					if (prefsSubs.getBoolean(entry.getKey(), false))
						urls.put(new URL(prefsUrls.getString(entry.getKey(), "")), entry.getKey());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return urls;
		}

		@Override
		protected void onPostExecute(HashMap<URL, String> urls) {
			RssModel.getInstance().loadFeed(urls, new RssModel.LoadFeedCallback() {
				@Override
				public void onLoad(ArrayList<RssItem> items) {
					Collections.sort(items);
					view.hideProgressBar();
					view.showFeed(items);
				}
			});
		}
	}
}
