/**
 * 
 */
package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * The preferences view for the settings.
 * 
 * @author ruthziegler
 */
public class WikiHowPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private class CloseDialogListener implements OnClickListener {

		public void onClick(DialogInterface dialog, int id) {
			dismissDialog(0);
		}
	}

	private class AboutPreferenceClickListener implements OnPreferenceClickListener {

		public boolean onPreferenceClick(Preference preference) {
			Intent intent = new Intent(getBaseContext(), AboutActivity.class);
			startActivity(intent);
			return true;
		}
	}

	private class TipsPreferenceClickListener implements OnPreferenceClickListener {

		public boolean onPreferenceClick(Preference preference) {
			showDialog(0);
			return true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Preferences onCreate");
		addPreferencesFromResource(R.xml.wikihow_preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Preferences onResume...");
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), this.getString(R.string.fontsize_key));
		onSharedPreferenceChanged(getPreferenceScreen().getSharedPreferences(), this.getString(R.string.cachelife_key));

		Preference aboutPref = getPreferenceScreen().findPreference(this.getString(R.string.about_key));
		aboutPref.setOnPreferenceClickListener(new AboutPreferenceClickListener());

		Preference tipsPref = getPreferenceScreen().findPreference(this.getString(R.string.tips_key));
		tipsPref.setOnPreferenceClickListener(new TipsPreferenceClickListener());
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "Preferences changed for key " + key);
		Preference pref = findPreference(key);

		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry()); // to get the value use
													// listPref.getValue()
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// id not used since there's only one dialog
		Dialog dialog = new AlertDialog.Builder(this).setMessage(R.string.about_dialog)
				.setNeutralButton(R.string.done, new CloseDialogListener()).create();
		return dialog;
	}
}
