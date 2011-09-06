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

package ch.bbv.wikiHow.model;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

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
public class ArticleDbAdapter {

    private static final String DATABASE_TABLE = "articles";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_IDENTIFIER = "identifier";
    public static final String KEY_TITLE = "title";
    public static final String KEY_HTML = "html";
    public static final String KEY_CATEGORY = "category";

    private DatabaseHelper databaseHelper;
    private final Context context;
    private final String databaseName;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    public ArticleDbAdapter(final Context ctx, final String databaseName) {
        this.context = ctx;
        this.databaseName = databaseName;
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
        // TODO: put the following two lines in a factory and create a
        // CachedDatabaseHelper
        final PreloadedDatabaseHelper databaseHelper = new PreloadedDatabaseHelper(context, databaseName);
        databaseHelper.createDatabase();
        //
        this.databaseHelper = databaseHelper;
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
    public long createArticle(final Article article) {
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
     * @param rowId
     *            id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteArticle(final long rowId) {
        return this.databaseHelper.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllArticles() {
        return this.databaseHelper.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE, KEY_HTML, KEY_CATEGORY }, null, null, null, null,
                null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId
     *            id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException
     *             if note could not be found/retrieved
     */
    public Cursor fetchAllCategories() throws SQLException {
        Log.d(TAG, "Fetching all categories on " + this.databaseHelper);
        return this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_CATEGORY }, null, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId
     *            id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException
     *             if note could not be found/retrieved
     */
    public Cursor fetchArticle(final String identifier) throws SQLException {
        final Cursor mCursor = this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE, KEY_CATEGORY },
                KEY_IDENTIFIER + "=" + identifier, null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId
     *            id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException
     *             if note could not be found/retrieved
     */
    public Cursor fetchArticleHtml(final String identifier) throws SQLException {
        final Cursor mCursor = this.databaseHelper.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_HTML }, KEY_IDENTIFIER + "=" + identifier, null,
                null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
