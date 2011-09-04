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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

	public static final String KEY_ROWID = "pk";
	public static final String KEY_IDENTIFIER = "identifier";
	public static final String KEY_TITLE = "title";
	public static final String KEY_HTML = "html";
	public static final String KEY_CATEGORY = "category";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table articles (pk integer primary key autoincrement, "
			+ "identifier text not null, title text not null, html text not null, category text not null);";

	// private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "articles";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;
	private final String dbName;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context, String databaseName) {
			super(context, databaseName, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Creating a new database instance.");
			db.execSQL(DATABASE_CREATE);

			db.execSQL("INSERT INTO articles (identifier, title, html, category) VALUES ('Article:Avoid-Rattlesnake-Attacks', 'Avoid Rattlesnake Attacks', '<div>Attack</div>', 'Animal Attacks')");
			db.execSQL("INSERT INTO articles (identifier, title, html, category) VALUES ('Article:Escape-a-Minefield', 'Escape a Minefield', '<div>Attack</div>', 'Extreme Scenarios')");
			db.execSQL("INSERT INTO articles (identifier, title, html, category) VALUES ('Article:Escape-from-a-Bear', 'Escape from a Bear', '<div>Attack</div>', 'Animal Attacks')");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public ArticleDbAdapter(Context ctx, String databaseName) {
		this.mCtx = ctx;
		this.dbName = databaseName;
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
	 */
	public ArticleDbAdapter open() throws SQLException {
		Log.d(TAG, "Opening database connection");
		mDbHelper = new DatabaseHelper(mCtx, dbName);
		mDb = mDbHelper.getWritableDatabase();
		Log.d(TAG, "Writable database " + mDb.toString());
		return this;
	}

	public void close() {
		mDbHelper.close();
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
	public long createArticle(Article article) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_IDENTIFIER, article.getIdentifier());
		initialValues.put(KEY_TITLE, article.getTitle());
		initialValues.put(KEY_HTML, article.getHtml());
		initialValues.put(KEY_CATEGORY, article.getCategory());

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteArticle(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllArticles() {
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE, KEY_HTML, KEY_CATEGORY },
				null, null, null, null, null);
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
		Log.d(TAG, "Fetching all categories on " + mDb);
		return mDb.query(true, DATABASE_TABLE, new String[] { KEY_CATEGORY }, null, null, null, null, null, null);
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
	public Cursor fetchArticle(String identifier) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IDENTIFIER, KEY_TITLE,
				KEY_CATEGORY }, KEY_IDENTIFIER + "=" + identifier, null, null, null, null, null);
		if (mCursor != null) {
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
	public Cursor fetchArticleHtml(String identifier) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_HTML }, KEY_IDENTIFIER + "="
				+ identifier, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
