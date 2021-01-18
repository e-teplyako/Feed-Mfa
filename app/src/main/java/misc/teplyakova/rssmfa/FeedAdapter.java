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
import java.util.HashMap;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.RowHolder> {
	private ArrayList<RssItem> items;
	private ItemClickListener listener;
	private HashMap<String, Integer> imageResources;

	public FeedAdapter(ItemClickListener listener, ArrayList<RssItem> items) {
		this.items = items;
		this.listener = listener;
		initImageResources();
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
		return (items == null) ? 0 : items.size();
	}

	private void initImageResources() {
		imageResources = new HashMap<>();
		imageResources.put("BEL", R.drawable.bel);
		imageResources.put("BGR", R.drawable.bgr);
		imageResources.put("CAN", R.drawable.can);
		imageResources.put("COL", R.drawable.col);
		imageResources.put("DEU", R.drawable.deu);
		imageResources.put("DNK", R.drawable.dnk);
		imageResources.put("EST", R.drawable.est);
		imageResources.put("FIN", R.drawable.fin);
		imageResources.put("FRA", R.drawable.fra);
		imageResources.put("IND", R.drawable.ind);
		imageResources.put("ISL", R.drawable.isl);
		imageResources.put("ISR", R.drawable.isr);
		imageResources.put("ITA", R.drawable.ita);
		imageResources.put("KAZ", R.drawable.kaz);
		imageResources.put("PRK", R.drawable.ic_settings_applications_24dp);
	}

	public interface ItemClickListener {
		void itemClicked(RssItem item);
	}

	class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
			String code = item.getCountryCode();
			if (imageResources.containsKey(code))
				flag.setImageResource(imageResources.get(code));
		}

		@Override
		public void onClick(View v) {
			if (listener != null)
				listener.itemClicked(items.get(getAdapterPosition()));
		}
	}
}
