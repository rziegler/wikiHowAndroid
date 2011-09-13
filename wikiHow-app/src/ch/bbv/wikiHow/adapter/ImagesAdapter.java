package ch.bbv.wikiHow.adapter;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import ch.bbv.wikiHow.R;

public class ImagesAdapter extends SimpleAdapter {

	private final Context context;

	public ImagesAdapter(Context context, List<String> data) {
		this(context, data, null);
	}

	public ImagesAdapter(Context context, List<String> data, String joiner) {
		super(context, buildCategoriesData(data, joiner), R.layout.category_item, //
				new String[] { "title", "img" }, //
				new int[] { R.id.category_title, R.id.category_image });
		this.context = context;
	}

	@Override
	public void setViewImage(ImageView v, String value) {
		Log.d(TAG, "setting view image for " + value);
		AssetManager assets = context.getResources().getAssets();

		InputStream is = null;
		try {
			is = assets.open("images/" + value);
			Bitmap image = BitmapFactory.decodeStream(is);
			v.setImageBitmap(image);
		} catch (IOException e) {
			Log.e(TAG, "Error loading the image", e);
			v.setImageResource(R.drawable.icon);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<Map<String, Object>> buildCategoriesData(List<String> data, String joiner) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (String d : data) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", d);
			map.put("img", getImageName(d, joiner));
			result.add(map);
		}
		return result;
	}

	private static String getImageName(String value, String joiner) {
		Log.d(TAG, "getting image name for " + value + ", joiner is" + joiner);
		String result;
		if (joiner != null && !joiner.isEmpty()) {
			result = value.replaceAll("\\s", joiner) + ".jpg";
		} else {
			result = value + ".jpg";
		}
		return result;
	}
}