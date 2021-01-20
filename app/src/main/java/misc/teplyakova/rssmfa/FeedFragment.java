package misc.teplyakova.rssmfa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
	private RecyclerView rv;
	private FeedAdapter adapter;
	private ArrayList<RssItem> items;
	private FeedAdapter.ItemClickListener itemClickListener;
	private SwipeRefreshLayout.OnRefreshListener refreshListener;
	private SwipeRefreshLayout swipeRefreshLayout;

	public FeedFragment() {

	}

	public FeedFragment(ArrayList<RssItem> items, FeedAdapter.ItemClickListener itemClickListener, SwipeRefreshLayout.OnRefreshListener refreshListener) {
		this.items = items;
		this.itemClickListener = itemClickListener;
		this.refreshListener = refreshListener;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		setRetainInstance(true);
		View view = inflater.inflate(R.layout.feed, container, false);
		rv = view.findViewById(R.id.recycler_view);
		swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		adapter = new FeedAdapter(itemClickListener, items);
		rv.setAdapter(adapter);
		RecyclerView.LayoutManager layoutManager =
				new LinearLayoutManager(getActivity());
		rv.setLayoutManager(layoutManager);
		rv.setHasFixedSize(true);
		swipeRefreshLayout.setOnRefreshListener(refreshListener);
	}
}
