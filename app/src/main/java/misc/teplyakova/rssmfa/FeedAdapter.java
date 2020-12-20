package misc.teplyakova.rssmfa;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.RowHolder> {
	private ArrayList<RssItem> items;
	private MainActivity activity;

	public FeedAdapter(MainActivity activity, ArrayList<RssItem> items) {
		this.items = items;
		this.activity = activity;
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

	class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView headline;
		TextView date;
		ImageView flag;

		RowHolder(View row) {
			super(row);
			headline = row.findViewById(R.id.headline);
			date = row.findViewById(R.id.date);
			flag = row.findViewById(R.id.flag);
			row.setOnClickListener(this);
		}

		void bindModel(RssItem item) {
			headline.setText(item.getTitle());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			date.setText(dateFormat.format(item.getPubDate()));
		}

		@Override
		public void onClick(View v) {
			if (activity != null)
				activity.itemClicked(items.get(getAdapterPosition()));
		}
	}
}
