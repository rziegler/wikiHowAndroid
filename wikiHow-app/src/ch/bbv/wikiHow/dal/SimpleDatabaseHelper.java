package ch.bbv.wikiHow.dal;

import static ch.bbv.wikiHow.WikiHowAppActivity.TAG;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class SimpleDatabaseHelper extends SQLiteOpenHelper implements DatabaseHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table articles (_id integer primary key autoincrement, "
			+ "identifier text not null, title text not null, html text not null, category text not null);";

	SimpleDatabaseHelper(final Context context, final String databaseName) {
		super(context, databaseName, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS notes");
		this.onCreate(db);
	}

	public void openDatabase() throws SQLException {
		this.getWritableDatabase();
	}

	public long insert(final String databaseTable, final String nullColumnHack, final ContentValues initialValues) {
		return this.getWritableDatabase().insert(databaseTable, nullColumnHack, initialValues);
	}

	public int delete(final String databaseTable, final String whereClause, final String[] whereArgs) {
		return this.getWritableDatabase().delete(databaseTable, whereClause, whereArgs);
	}

	public Cursor query(final String databaseTable, final String[] columns, final String selection, final String[] selectionArgs, final String groupBy,
			final String having, final String orderBy) {
		return this.getWritableDatabase().query(databaseTable, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	public Cursor query(final boolean distinct, final String databaseTable, final String[] columns, final String selection, final String[] selectionArgs,
			final String groupBy, final String having, final String orderBy, final String limit) {
		return this.getWritableDatabase().query(distinct, databaseTable, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
}
