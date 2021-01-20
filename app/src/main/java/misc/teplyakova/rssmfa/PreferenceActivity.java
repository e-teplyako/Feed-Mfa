package misc.teplyakova.rssmfa;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenceActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.settings, new SettingsFragment())
				.commit();
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public static class SettingsFragment extends PreferenceFragmentCompat {
		public final static String PREFERENCES_COUNTRIES_URLS = "pref_countries_urls";
		public final static String PREFERENCES_COUNTRIES_SUBS = "pref_countries_subs";

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			getPreferenceManager().setSharedPreferencesName(PREFERENCES_COUNTRIES_SUBS);
			setPreferencesFromResource(R.xml.preferences, rootKey);
			populateList((PreferenceCategory) findPreference("countries"));
		}

		private void populateList(PreferenceCategory parent) {
			SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCES_COUNTRIES_SUBS, 0);
			List<String> countries = new ArrayList<>(prefs.getAll().keySet());
			Collections.sort(countries);
			for (String country : countries) {
				SwitchPreference switchPreference = new SwitchPreference(this.getContext());
				switchPreference.setKey(country);
				switchPreference.setChecked(prefs.getBoolean(country, false));
				int stringId = getResources().getIdentifier(country, "string", getActivity().getPackageName());
				switchPreference.setTitle(getResources().getString(stringId));
				parent.addPreference(switchPreference);
			}
		}
	}
}