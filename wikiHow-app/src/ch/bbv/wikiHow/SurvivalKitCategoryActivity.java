package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import ch.bbv.wikiHow.adapter.ImagesAdapter;
import ch.bbv.wikiHow.dal.ArticlePreloadedCache;
import ch.bbv.wikiHow.model.Article;

public class SurvivalKitCategoryActivity extends ListActivity {
    static final String KEY_CATEGORY = "category";

    private ArticlePreloadedCache preloadedCache;
    private List<Article> articles;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SurvivalKitArticlesActivity onCreate...");
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
        Log.d(TAG, "SurvivalKitArticlesActivity onResume...");
        this.preloadedCache.open();

        final Bundle bundle = this.getIntent().getExtras();
        final String category = bundle.getString(KEY_CATEGORY);
        if(this.articles == null) {
            if(category.equals("All Survival Kit")) {
                this.articles = this.preloadedCache.getArticles();
            }
            else {
                this.articles = this.preloadedCache.getArticlesByCategory(category);
            }
        }
        final List<String> titles = new ArrayList<String>();
        for(final Article article : this.articles) {
            titles.add(article.getTitle());
        }

        Log.d(TAG, "Fetched articles " + this.articles.size());
        this.setTitle(category);
        this.setListAdapter(new ImagesAdapter(this, titles, "-"));
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        Log.d(TAG, "SurvivalKitArticlesActivity onListItemClick...");
        super.onListItemClick(l, v, position, id);
        // Get the item that was clicked
        final HashMap<String, Object> map = (HashMap<String, Object>) this.getListAdapter().getItem(position);
        final String title = (String) map.get("title");
        final String identifier = this.getIdentifier(title);
        if(identifier == null) {
            final Toast toast = Toast.makeText(this.getApplicationContext(), "Article not found", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        final Intent intent = new Intent(this, ArticleActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(ArticleActivity.KEY_IDENTIFIER, identifier);
        intent.putExtras(bundle);

        Log.d(TAG, "SurvivalKitCategoryActivity starting ArticlesActivity for '" + title + "'");
        this.startActivity(intent);
    }

    // TODO: Hack => better display key/value pair (title/identifier) in list
    private String getIdentifier(final String title) {
        for(final Article article : this.articles) {
            if(article.getTitle().equals(title))
                return article.getIdentifier();
        }

        return null;
    }
}
