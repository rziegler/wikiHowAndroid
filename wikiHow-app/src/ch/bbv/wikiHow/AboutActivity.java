package ch.bbv.wikiHow;

import android.app.Activity;
import android.os.Bundle;

/**
 * The about view shown in the settings.
 * 
 * @author ruthziegler
 * 
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);
	}
}
