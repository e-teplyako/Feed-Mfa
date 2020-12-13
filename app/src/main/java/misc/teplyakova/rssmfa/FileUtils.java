package misc.teplyakova.rssmfa;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;

public class FileUtils {
	public static String fileName = "mfa_url_short.json";

	public static String readFileToString(Context context, String fileName) {
		AssetManager assetManager = context.getAssets();
		try {
			InputStream inputStream = assetManager.open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String data;
			while ((data = reader.readLine()) != null) {
				sb.append(data);
			}
			reader.close();
			inputStream.close();
			return sb.toString();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static TreeMap<String, String> parseJson(String jsonString) {
		TreeMap<String, String> result = new TreeMap<>();
		try {
			JSONObject jsonRootObject = new JSONObject(jsonString);
			Iterator<String> iterator = jsonRootObject.keys();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = jsonRootObject.optString(key);
				result.put(key, value);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
