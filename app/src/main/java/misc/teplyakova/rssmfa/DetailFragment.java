package misc.teplyakova.rssmfa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {
	private RssItem item;
	private TextView headline;
	private WebView webView;
	private Button openLink;
	private TextView description;

	public DetailFragment(RssItem item) {
		this.item = item;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.activity_detail, container, false);
		headline = result.findViewById(R.id.headline);
		webView = result.findViewById(R.id.webview);
		openLink = result.findViewById(R.id.open_link);
		description = result.findViewById(R.id.description);
		return result;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headline.setText(item.getTitle());
		webView.loadData(item.getDescription(), "text/html", null);
		description.setText(item.getDescription());
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
