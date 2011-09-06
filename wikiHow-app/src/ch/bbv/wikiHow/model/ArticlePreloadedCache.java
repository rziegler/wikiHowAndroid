package ch.bbv.wikiHow.model;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ArticlePreloadedCache {

    private static final String PRELOADED_DATABASE_NAME = "preloaded.db";
    private ArticleDbAdapter dbAdapter;

    public ArticlePreloadedCache(final Context ctx) {
        dbAdapter = new ArticleDbAdapter(ctx, PRELOADED_DATABASE_NAME);
    }

    public void open() {
        try {
            dbAdapter.open();
        }
        catch(final IOException e) {
            Log.e(TAG, "could not open DatabaseAdapter", e);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        dbAdapter.close();
    }

    public Article getCachedArticle(final String identifier) {
        final Cursor c = dbAdapter.fetchArticle(identifier);
        Article result = null;

        if(c != null) {
            result = new Article();
            result.setIdentifier(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_IDENTIFIER)));
            result.setTitle(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_TITLE)));
            result.setCategory(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
            result.setCached(true);

            c.close();
        }
        return result;
    }

    public void hydrateArticle(final Article article) {
        final Cursor c = dbAdapter.fetchArticleHtml(article.getIdentifier());

        if(c != null) {
            article.setHtml(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_HTML)));
            c.close();
        }
    }

    public List<Article> getPreloadedArticles(final String category) {
        return null;
    }

    public List<String> getPreloadedCategories() {
        final Cursor c = dbAdapter.fetchAllCategories();
        final List<String> result = new ArrayList<String>();

        if(c != null) {
            while(c.moveToNext()) {
                result.add(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
            }
        }
        return result;
    }

    // title kann auch * oder % enthalten -> DB ruft like auf...
    public List<Article> getPreloadedArticlesWithTitle(final String title) {
        return null;
    }
}
