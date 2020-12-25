package misc.teplyakova.rssmfa;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

public class DetailFragment extends Fragment {
	private RssItem item;
	private TextView headline;
	private TextView date;
	private TextView description;
	private Button openLink;

	public DetailFragment(RssItem item) {
		this.item = item;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.activity_detail, container, false);
		headline = result.findViewById(R.id.headline);
		date = result.findViewById(R.id.date);
		description = result.findViewById(R.id.description);
		openLink = result.findViewById(R.id.open_link);
		return result;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headline.setText(item.getTitle());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
		date.setText(dateFormat.format(item.getPubDate()));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
		description.setText(Html.fromHtml(item.getDescription()));
		openLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri link = Uri.parse(item.getLink());
				Intent intent = new Intent(Intent.ACTION_VIEW, link);
				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivity(intent);
				}
			}
		});
	}
}
