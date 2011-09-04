/**
 * 
 */
package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * The preferences view for the settings.
 * 
 * @author ruthziegler
 */
public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private static final String KEY_FONT_SIZE_PREFERENCE = "fontSize";
	private static final String KEY_CACHE_LIFE_PREFERENCE = "cacheLife";

	private Preference fontSizePref;
	private Preference cacheLifePref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating settings view");
		addPreferencesFromResource(R.xml.wikihow_preferences);

		PreferenceManager.setDefaultValues(this, R.xml.wikihow_preferences, false);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		fontSizePref = findPreference(KEY_FONT_SIZE_PREFERENCE);
		cacheLifePref = findPreference(KEY_CACHE_LIFE_PREFERENCE);

		// set the default values
		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), KEY_FONT_SIZE_PREFERENCE);
		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), KEY_CACHE_LIFE_PREFERENCE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		fontSizePref = findPreference(KEY_FONT_SIZE_PREFERENCE);
		cacheLifePref = findPreference(KEY_CACHE_LIFE_PREFERENCE);

		// set the default values
		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), KEY_FONT_SIZE_PREFERENCE);
		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), KEY_CACHE_LIFE_PREFERENCE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "Preferences changed for key " + key);
		if (KEY_FONT_SIZE_PREFERENCE.equals(key)) {
			// Resources r = getResources();
			// int[] sizes = r.getIntArray(R.array.fontSize);
			//
			String value = sharedPreferences.getString(key, null);
			// fontSizePref.setSummary(sizes[Integer.valueOf(value).intValue()]);
			fontSizePref.setSummary(value);
		} else if (KEY_CACHE_LIFE_PREFERENCE.equals(key)) {
			cacheLifePref.setSummary(sharedPreferences.getString(key, null) + " days.");
		}
	}
}
