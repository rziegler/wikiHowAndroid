package ch.bbv.wikiHow.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class ArticlePreloadedCache {

	private static final String PRELOADED_DATABASE_NAME = "preloaded";
	private ArticleDbAdapter dbAdapter;

	public ArticlePreloadedCache(Context ctx) {
		dbAdapter = new ArticleDbAdapter(ctx, PRELOADED_DATABASE_NAME);
	}

	public void open() {
		dbAdapter.open();
	}

	public void close() {
		dbAdapter.close();
	}

	public Article getCachedArticle(String identifier) {
		Cursor c = dbAdapter.fetchArticle(identifier);
		Article result = null;

		if (c != null) {
			result = new Article();
			result.setIdentifier(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_IDENTIFIER)));
			result.setTitle(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_TITLE)));
			result.setCategory(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
			result.setCached(true);

			c.close();
		}
		return result;
	}

	public void hydrateArticle(Article article) {
		Cursor c = dbAdapter.fetchArticleHtml(article.getIdentifier());

		if (c != null) {
			article.setHtml(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_HTML)));
			c.close();
		}
	}

	public List<Article> getPreloadedArticles(String category) {
		return null;
	}

	public List<String> getPreloadedCategories() {
		Cursor c = dbAdapter.fetchAllCategories();
		List<String> result = new ArrayList<String>();

		if (c != null) {
			while (c.moveToNext()) {
				result.add(c.getString(c.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
			}
		}
		return result;
	}

	// title kann auch * oder % enthalten -> DB ruft like auf...
	public List<Article> getPreloadedArticlesWithTitle(String title) {
		return null;
	}
}
