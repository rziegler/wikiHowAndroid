package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.bbv.wikiHow.dal.ArticlePreloadedCache;

public class SurvivalKitActivity extends ListActivity {

    private ArticlePreloadedCache preloadedCache;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SurvivalKitActivity onCreate...");
        try {
            this.preloadedCache = new ArticlePreloadedCache(this);
        }
        catch(final IOException e) {
            Log.d(TAG, "Could not create ArticlePreloadedChache");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.preloadedCache.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "SurvivalKitActivity onResume...");
        this.preloadedCache.open();
        final List<String> categories = this.preloadedCache.getAllCategories();
        Log.d(TAG, "Fetched categories " + categories.size());
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.rowlayout, R.id.label, categories));
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        Log.d(TAG, "SurvivalKitActivity onListItemClick...");
        super.onListItemClick(l, v, position, id);
        // Get the item that was clicked
        final Object o = this.getListAdapter().getItem(position);

        final Intent intent = new Intent(this, SurvivalKitArticlesActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(SurvivalKitArticlesActivity.CATEGORY_KEY, o.toString());
        intent.putExtras(bundle);

        Log.d(TAG, "SurvivalKitActivity starting SurvialKitArticles Activity for " + o.toString());
        this.startActivity(intent);
    }
}
