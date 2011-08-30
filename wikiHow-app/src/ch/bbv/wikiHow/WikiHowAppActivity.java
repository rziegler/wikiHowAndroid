package ch.bbv.wikiHow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class WikiHowAppActivity extends Activity {

	public static final String TAG = "wikiHow";

	private static final int ACTIVITY_SETTINGS = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate...");
		setContentView(R.layout.main);

		// initialize the preferences with default values once
		PreferenceManager.setDefaultValues(this, R.xml.wikihow_preferences, false);

		// access the prefs
		// SharedPreferences sp =
		// PreferenceManager.getDefaultSharedPreferences(this);
		// String value = sp.getString("key", null);
		// value contains the default value defined in xml instead of null
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.wikihow_menu, menu);
		Log.d(TAG, "onCreateOptionsMenu");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.survivalkit:
			Log.d(TAG, "SURVIVAL KIT MENU selected");
			return true;
		case R.id.featured:
			Log.d(TAG, "FEATURED MENU selected");
			return true;
		case R.id.bookmarks:
			Log.d(TAG, "BOOKMARKS MENU selected");
			return true;
		case R.id.search:
			Log.d(TAG, "SEARCH MENU selected");
			return true;
		case R.id.settings:
			Log.d(TAG, "SETTINGS MENU selected");
			Intent intent = new Intent(getBaseContext(), Preferences.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}