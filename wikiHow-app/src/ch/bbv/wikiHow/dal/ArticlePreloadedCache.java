package ch.bbv.wikiHow.dal;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import ch.bbv.wikiHow.model.Article;

public class ArticlePreloadedCache {

    private static final String PRELOADED_DATABASE_NAME = "preloaded.db";
    private final ArticleDbAdapter dbAdapter;

    public ArticlePreloadedCache(final Context context) throws IOException {
        final PreloadedDatabaseHelper databaseHelper = new PreloadedDatabaseHelper(context, PRELOADED_DATABASE_NAME);
        databaseHelper.createDatabase();
        this.dbAdapter = new ArticleDbAdapter(databaseHelper);
    }

    public void open() {
        try {
            this.dbAdapter.open();
        }
        catch(final IOException e) {
            Log.e(TAG, "could not open DatabaseAdapter", e);
            throw new RuntimeException(e);
        }
    }

    public void close() {
        this.dbAdapter.close();
    }

    public Article getCachedArticle(final String identifier) {
        return this.dbAdapter.fetchArticle(identifier);
    }

    public void hydrateArticle(final Article article) {
        final String html = this.dbAdapter.fetchArticleHtml(article.getIdentifier());

        if(html != null) {
            article.setHtml(html);
        }
    }

    public List<Article> getArticles() {
        return this.dbAdapter.queryArticle(null);
    }

    public List<Article> getArticlesByCategory(final String category) {
        return this.dbAdapter.queryArticle(ArticleDbAdapter.KEY_CATEGORY + " = '" + category + "'");
    }

    public List<String> getAllCategories() {
        return this.dbAdapter.fetchAllCategories();
    }

    // title kann auch * oder % enthalten -> DB ruft like auf...
    public List<Article> queryArticlesByTitle(final String title) {
        return this.dbAdapter.queryArticle(ArticleDbAdapter.KEY_TITLE + " like '" + title + "'");
    }
}
