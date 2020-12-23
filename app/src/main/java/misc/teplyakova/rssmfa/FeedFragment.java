package misc.teplyakova.rssmfa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
	private RecyclerView rv;
	private FeedAdapter adapter;
	private ArrayList<RssItem> items;
	private FeedAdapter.ItemClickListener listener;

	public FeedFragment(ArrayList<RssItem> items, FeedAdapter.ItemClickListener listener) {
		this.items = items;
		this.listener = listener;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.feed, container, false);
		rv = view.findViewById(R.id.recycler_view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		adapter = new FeedAdapter(listener, items);
		rv.setAdapter(adapter);
		RecyclerView.LayoutManager layoutManager =
				new LinearLayoutManager(getActivity());
		rv.setLayoutManager(layoutManager);
		rv.setHasFixedSize(true);
		//showFeed(items);
	}

	private void showFeed(ArrayList<RssItem> items) {
		adapter.notifyDataSetChanged();
	}
}
