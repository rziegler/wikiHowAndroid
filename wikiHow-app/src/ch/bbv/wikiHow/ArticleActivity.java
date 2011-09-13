package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import ch.bbv.wikiHow.dal.ArticleCache;
import ch.bbv.wikiHow.dal.ArticlePreloadedCache;
import ch.bbv.wikiHow.model.Article;

public class ArticleActivity extends Activity {

    static final String KEY_IDENTIFIER = "articleIdentifier";

    WebView webview;
    private ArticlePreloadedCache preloadedCache;
    private ArticleCache cache;

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "ArticleActivity onPause...");

        this.preloadedCache.close();
        this.cache.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ArticleActivity onResume...");

        this.preloadedCache.open();
        this.cache.open();

        final Bundle bundle = this.getIntent().getExtras();
        final String identifier = bundle.getString(KEY_IDENTIFIER);
        final Article article = this.loadArticle(identifier);

        if(article == null) {
            final Toast toast = Toast.makeText(this.getApplicationContext(), "Article could not be loaded", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        this.webview = (WebView) this.findViewById(R.id.webview);
        // this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.loadData(article.getHtml(), "text/html", "utf-8");
        this.setTitle(article.getTitle());
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ArticleActivity onCreate...");
        this.setContentView(R.layout.article);

        this.initializePreloadedCache();
        this.initializeCache();
    }

    private Article loadArticle(final String identifier) {
        Article article = this.preloadedCache.getCachedArticle(identifier);
        if(article != null) {
            this.preloadedCache.hydrateArticle(article);
            return article;
        }

        article = this.cache.getCachedArticle(identifier);
        if(article != null) {
            this.cache.hydrateArticle(article);
            return article;
        }

        return null; // TODO: load from web
    }

    private void initializePreloadedCache() {
        try {
            this.preloadedCache = new ArticlePreloadedCache(this);
        }
        catch(final IOException e) {
            Log.d(TAG, "Could not create ArticlePreloadedChache");
            throw new RuntimeException(e);
        }
    }

    private void initializeCache() {
        try {
            this.cache = new ArticleCache(this);
        }
        catch(final IOException e) {
            Log.d(TAG, "Could not create ArticleCache");
            throw new RuntimeException(e);
        }
    }
}
