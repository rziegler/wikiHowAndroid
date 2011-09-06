package ch.bbv.wikiHow.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

interface DatabaseHelper {

    public abstract void openDatabase() throws SQLException;

    public abstract void close();

    public abstract long insert(final String databaseTable, final String nullColumnHack, final ContentValues initialValues);

    public abstract int delete(final String databaseTable, final String whereClause, final String[] whereArgs);

    public abstract Cursor query(final String databaseTable, final String[] columns, final String selection, final String[] selectionArgs,
            final String groupBy, final String having, final String orderBy);

    public abstract Cursor query(final boolean distinct, final String databaseTable, final String[] columns, final String selection,
            final String[] selectionArgs, final String groupBy, final String having, final String orderBy, final String limit);

}