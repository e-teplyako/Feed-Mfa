package misc.teplyakova.rssmfa;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

public class FeedFragment extends ListFragment {
	private ArrayAdapter aa = null;
	private ArrayList<RssItem> items;

	public FeedFragment(ArrayList<RssItem> items) {
		this.items = items;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		aa = new RssAdapter(getActivity(), new ArrayList<RssItem>());
		setListAdapter(aa);
		showFeed(items);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		MainActivity activity = (MainActivity) getActivity();
		if (activity != null)
			activity.itemClicked(items.get(position));
	}

	private void showFeed(ArrayList<RssItem> items) {
		aa.clear();
		aa.addAll(items);
		aa.notifyDataSetChanged();
	}
}
