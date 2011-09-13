package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Performs searches and presents results.
 * 
 * @author ruthziegler
 * 
 */
public class SearchableActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "SearchableActivity onCreate...");
		setContentView(R.layout.search);

		// get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			searchArticles(query);
		}

	}

	private void searchArticles(String query) {
		// TODO Search in database, search online
		Log.i(TAG, "Start searching... " + query);

	}

}
