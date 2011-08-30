/**
 * 
 */
package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * The preferences view for the settings.
 * 
 * @author ruthziegler
 */
public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating settings view");
		addPreferencesFromResource(R.xml.wikihow_preferences);
	}
}
