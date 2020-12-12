package misc.teplyakova.rssmfa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.RowHolder> {
	private ArrayList<RssItem> items;

	public FeedAdapter(ArrayList<RssItem> items) {
		this.items = items;
	}

	@NonNull
	@Override
	public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new RowHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull RowHolder holder, int position) {
		holder.bindModel(items.get(position));
	}

	@Override
	public int getItemCount() {
		return(items.size());
	}

	static class RowHolder extends RecyclerView.ViewHolder {
		TextView headline;
		TextView date;
		ImageView flag;

		RowHolder(View row) {
			super(row);
			headline = row.findViewById(R.id.headline);
			date = row.findViewById(R.id.date);
			flag = row.findViewById(R.id.flag);
		}

		void bindModel(RssItem item) {
			headline.setText(item.getTitle());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			date.setText(dateFormat.format(item.getPubDate()));
		}
	}
}
