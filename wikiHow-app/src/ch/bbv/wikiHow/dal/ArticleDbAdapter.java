/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ch.bbv.wikiHow.dal;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import ch.bbv.wikiHow.model.Article;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
class ArticleDbAdapter {

    private static final String DATABASE_TABLE = "articles";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_IDENTIFIER = "identifier";
    public static final String KEY_TITLE = "title";
    public static final String KEY_HTML = "html";
    public static final String KEY_CATEGORY = "category";

    private DatabaseHelper databaseHelper;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    public ArticleDbAdapter(final DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     * @throws IOException
     */
    public ArticleDbAdapter open() throws IOException {
        Log.d(TAG, "Opening database connection");
        this.databaseHelper.openDatabase();
        Log.d(TAG, "Writable database " + this.databaseHelper.toString());
        return this;
    }

    public void close() {
        this.databaseHelper.close();
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param article
     *            the article to create.
     * @return rowId or -1 if failed
     */
    public long insertArticle(final Article article) {
        final ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IDENTIFIER, article.getIdentifier());
        initialValues.put(KEY_TITLE, article.getTitle());
        initialValues.put(KEY_HTML, article.getHtml());
        initialValues.put(KEY_CATEGORY, article.getCategory());

        return this.databaseHelper.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * 
     * @param rowId
     *            id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteArticle(final long rowId) {
        return this.databaseHelper.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

//    public Cursor fetchAllArticles() {
//        return this.databaseHelper.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE, KEY_HTML, KEY_CATEGORY }, null, null, null, null,
//                null);
//    }

    /**
     * Returns all categories for which articles are stored.
     * 
     * @return List of categories - never null
     */
    public List<String> fetchAllCategories() throws SQLException {
        Log.d(TAG, "Fetching all categories on " + this.databaseHelper);
        final Cursor cursor = this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_CATEGORY }, null, null, null, null, null, null);

        final List<String> result = new ArrayList<String>();
        if(cursor != null) {
            while(cursor.moveToNext()) {
                result.add(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
            }
        }

        return result;
    }

    /**
     * Returns the article by the {@code identifier}.
     * 
     * @param identifier
     *            of the article
     * @return the article or {@code null} if not found
     */
    public Article fetchArticle(final String identifier) throws SQLException {
        final Cursor cursor = this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE, KEY_CATEGORY },
                KEY_IDENTIFIER + "=" + identifier, null, null, null, null, null);

        Article result = null;
        if(cursor != null) {
            cursor.moveToFirst();

            result = new Article();
            result.setIdentifier(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_IDENTIFIER)));
            result.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_TITLE)));
            result.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
            result.setCached(true);

            cursor.close();
        }

        return result;
    }

    /**
     * Returns the HTML content of the article by the {@code identifier}.
     * 
     * @return the HTML content or {@code null} if not found
     */
    public String fetchArticleHtml(final String identifier) throws SQLException {
        final Cursor cursor = this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_HTML }, KEY_IDENTIFIER + "=" + identifier, null,
                null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
            final String html = cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_HTML));
            cursor.close();
            return html;
        }

        return null;
    }

    /**
     * Returns a list of articles matching the {@code selection}.
     * 
     * @param selection
     *            a condition of type {@code identifier=test}
     * @return list of articles - never null
     */
    public List<Article> queryArticle(final String selection) throws SQLException {
        final Cursor cursor = this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE, KEY_CATEGORY }, selection,
                null, null, null, null, null);

        final ArrayList<Article> articles = new ArrayList<Article>();
        if(cursor != null) {
            while(cursor.moveToNext()) {
                final Article result = new Article();
                result.setIdentifier(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_IDENTIFIER)));
                result.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_TITLE)));
                result.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(ArticleDbAdapter.KEY_CATEGORY)));
                result.setCached(true);
                articles.add(result);
            }
        }

        return articles;
    }
}
