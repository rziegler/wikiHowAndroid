package ch.bbv.wikiHow;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import ch.bbv.wikiHow.adapter.ImagesAdapter;
import ch.bbv.wikiHow.dal.ArticlePreloadedCache;
import ch.bbv.wikiHow.model.Article;

public class SurvivalKitArticlesActivity extends ListActivity {
	static final String CATEGORY_KEY = "category";

	private ArticlePreloadedCache preloadedCache;

	private List<Article> articles;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "SurvivalKitArticlesActivity onCreate...");
		try {
			this.preloadedCache = new ArticlePreloadedCache(this);
		} catch (final IOException e) {
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

		if (this.articles == null) {
			final Bundle bundle = this.getIntent().getExtras();
			final String category = bundle.getString(CATEGORY_KEY);
			if (category.equals("All Survival Kit")) {
				this.articles = this.preloadedCache.getArticles();
			} else {
				this.articles = this.preloadedCache.getArticlesByCategory(category);
			}
		}
		final List<String> titles = new ArrayList<String>();
		for (final Article article : this.articles) {
			titles.add(article.getTitle());
		}

		Log.d(TAG, "Fetched articles " + this.articles.size());
		// this.setListAdapter(new ArrayAdapter<String>(this,
		// R.layout.category_list, R.id.label, titles));
		this.setListAdapter(new ImagesAdapter(this, titles, "-"));
	}

	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
		Log.d(TAG, "SurvivalKitArticlesActivity onListItemClick...");
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		final Object o = this.getListAdapter().getItem(position);
		final String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
	}
}
