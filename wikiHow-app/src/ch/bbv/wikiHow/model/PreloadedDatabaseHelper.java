package ch.bbv.wikiHow.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class PreloadedDatabaseHelper extends SQLiteOpenHelper implements DatabaseHelper {
	private static final String DATABASE_PATH = "/data/data/ch.bbv.wikiHow/databases/";
	private static final int DATABASE_VERSION = 1;

	private final String databaseName;
	private final Context context;
	private SQLiteDatabase database;

	PreloadedDatabaseHelper(final Context context, final String databaseName) {
		super(context, databaseName, null, DATABASE_VERSION);
		this.databaseName = databaseName;
		this.context = context;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
	}

	public void createDatabase() throws IOException {
		final boolean dbExists = checkDatabase();
		if (!dbExists) {
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (final IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDatabase() {
		final String dbPath = getDatabasePath();
		final SQLiteDatabase checkDb;
		try {
			checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (final Exception e) {
			return false;
		}

		if (checkDb != null) {
			checkDb.close();
			return true;
		}

		return false;
	}

	private String getDatabasePath() {
		return DATABASE_PATH + databaseName;
	}

	private void copyDatabase() throws IOException {
		final InputStream inStream = context.getAssets().open(databaseName);
		final OutputStream outStream = new FileOutputStream(getDatabasePath());

		final byte[] buffer = new byte[1024];
		int length;
		while ((length = inStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, length);
		}

		outStream.flush();
		outStream.close();
		inStream.close();
	}

	public void openDatabase() throws SQLException {
		final String dbPath = getDatabasePath();
		this.database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (this.database != null) {
			this.database.close();
		}
		super.close();
	}

	public long insert(final String databaseTable, final String nullColumnHack, final ContentValues initialValues) {
		return this.database.insert(databaseTable, nullColumnHack, initialValues);
	}

	public int delete(final String databaseTable, final String whereClause, final String[] whereArgs) {
		return this.database.delete(databaseTable, whereClause, whereArgs);
	}

	public Cursor query(final String databaseTable, final String[] columns, final String selection,
			final String[] selectionArgs, final String groupBy, final String having, final String orderBy) {
		return this.database.query(databaseTable, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	public Cursor query(final boolean distinct, final String databaseTable, final String[] columns,
			final String selection, final String[] selectionArgs, final String groupBy, final String having,
			final String orderBy, final String limit) {
		return this.database.query(distinct, databaseTable, columns, selection, selectionArgs, groupBy, having,
				orderBy, limit);
	}

	@Override
	public String toString() {
		return this.database.toString();
	}
}