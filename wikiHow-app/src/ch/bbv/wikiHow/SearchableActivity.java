package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import ch.bbv.wikiHow.adapter.XmlSearchResultAdapter;

/**
 * Performs searches and presents results.
 * 
 * @author ruthziegler
 * 
 */
public class SearchableActivity extends ListActivity {

	private TextView textView;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "SearchableActivity onCreate...");
		setContentView(R.layout.search);

		textView = (TextView) findViewById(R.id.searchTextView);
		listView = (ListView) findViewById(android.R.id.list);

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
		try {
			textView.setText(getString(R.string.search_results, query));
			String result = executeHttpGet(query);
			XmlSearchResultAdapter adapter = new XmlSearchResultAdapter(result, this);
			this.setListAdapter(adapter);
		} catch (Exception e) {
			Log.e(TAG, "Error during search", e);
		}
	}

	private String executeHttpGet(String query) throws Exception {
		BufferedReader in = null;
		String xml = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(getWikiHowUrl(query)));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			xml = sb.toString();
			// Log.d(TAG, "Printing the result:");
			// Log.d(TAG, xml);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return xml;
	}

	private String getWikiHowUrl(String query) {
		StringBuilder builder = new StringBuilder();
		builder.append("http://www.wikihow.com/Special:LSearch?fulltext=Search&search=");
		builder.append(query.replaceAll("\\s", "%20"));
		builder.append("&rss=1");
		return builder.toString();
	}
}
