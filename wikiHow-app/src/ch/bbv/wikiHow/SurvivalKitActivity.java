package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ch.bbv.wikiHow.model.ArticlePreloadedCache;

public class SurvivalKitActivity extends ListActivity {

    private ArticlePreloadedCache preloadedCache;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SurvivalKitActivity onCreate...");
        preloadedCache = new ArticlePreloadedCache(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        preloadedCache.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "SurvivalKitActivity onResume...");
        preloadedCache.open();
        final List<String> categories = preloadedCache.getPreloadedCategories();
        Log.d(TAG, "Fetched categories " + categories.size());
        setListAdapter(new ArrayAdapter<String>(this, R.layout.rowlayout, R.id.label, categories));
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        Log.d(TAG, "SurvivalKitActivity onListItemClick...");
        super.onListItemClick(l, v, position, id);
        // Get the item that was clicked
        final Object o = this.getListAdapter().getItem(position);
        final String keyword = o.toString();
        Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
    }
}
