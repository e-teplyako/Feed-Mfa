package misc.teplyakova.rssmfa;

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
	RssPresenter presenter;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressBar = findViewById(R.id.progressBar);
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		shouldDisplayHomeUp();
		init();
	}

	public void showFeed(ArrayList<RssItem> items) {
		FeedFragment fragment = new FeedFragment(items, this, this);
		getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content,
						fragment).commit();
	}

	private void showDetail(RssItem item) {
		DetailFragment fragment = new DetailFragment(item);
		getSupportFragmentManager().beginTransaction().addToBackStack(null)
				.replace(android.R.id.content,
						fragment).commit();
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


	private void init() {
		RssModel model = new RssModel();
		presenter = new RssPresenter(model);
		presenter.attachView(this);
		presenter.viewIsReady();
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
		presenter.detachView();
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
		presenter.viewIsReady();
	}
}
