package misc.teplyakova.rssmfa;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

public class RssAdapter extends ArrayAdapter<RssItem> {
	public RssAdapter(Activity context, List<RssItem> list) {
		super(context, 0, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listItemView = convertView;
		if(listItemView == null) {
			listItemView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item, parent, false);
		}

		RssItem currentItem = getItem(position);

		TextView nameTextView = listItemView.findViewById(R.id.headline);
		if (currentItem != null)
			nameTextView.setText(currentItem.getTitle());

		TextView dateTextView = listItemView.findViewById(R.id.date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		if (currentItem != null && currentItem.getPubDate() != null)
			dateTextView.setText(dateFormat.format(currentItem.getPubDate()));

		return listItemView;
	}

}
