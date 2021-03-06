package misc.teplyakova.rssmfa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, FeedAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
	ProgressBar progressBar;
	private final String FEED_FRAGMENT_ID = "feed_fragment_id";
	private final String DETAIL_FRAGMENT_ID = "detail_fragment_id";
	private final String SS_FEED_MODE = "ss_feed_mode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressBar = findViewById(R.id.progressBar);
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		shouldDisplayHomeUp();
		RssPresenter.getInstance().attachView(this);
		if (savedInstanceState == null)
			RssPresenter.getInstance().viewIsReady();
		else {
			RssPresenter.getInstance().viewHasBeenRecreated(savedInstanceState.getBoolean(SS_FEED_MODE, true));
		}
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_ID) == null)
			outState.putBoolean(SS_FEED_MODE, true);
		else
			outState.putBoolean(SS_FEED_MODE, false);
	}

	public void showFeed(ArrayList<RssItem> items) {
		FeedFragment fragment = new FeedFragment(items, this, this);
		getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content,
						fragment, FEED_FRAGMENT_ID).commit();
	}

	private void showDetail(RssItem item) {
		DetailFragment fragment = new DetailFragment(item);
		getSupportFragmentManager().beginTransaction().addToBackStack(null)
				.replace(android.R.id.content,
						fragment, DETAIL_FRAGMENT_ID).commit();
	}

	@Override
	public void itemClicked(RssItem item) {
		showDetail(item);
	}

	public void shouldDisplayHomeUp(){
		boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
		ActionBar ab = getSupportActionBar();
		if (ab != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				Intent intent = new Intent(this, PreferenceActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RssPresenter.getInstance().detachView();
	}

	@Override
	public void onBackStackChanged() {
		shouldDisplayHomeUp();
	}

	@Override
	public boolean onSupportNavigateUp() {
		getSupportFragmentManager().popBackStack();
		return true;
	}

	public void showProgressBar() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void hideProgressBar() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onRefresh() {
		RssPresenter.getInstance().viewIsReady();
	}
}
