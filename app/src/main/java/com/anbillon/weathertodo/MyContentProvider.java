package com.anbillon.weathertodo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.HashMap;

public class MyContentProvider extends ContentProvider {

    ThingTodoSqlHelper helper;
    public static String URL = "content://com.anbillon.weathertodo.MyContentProvider/thingstodos";
    static final Uri CONTENT_URI = Uri.parse(URL);


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI("com.anbillon.weathertodo.MyContentProvider", ThingTodo.TABLE_NAME, 1);
        sUriMatcher.addURI("com.anbillon.weathertodo.MyContentProvider", ThingTodo.TABLE_NAME+"/#", 2);
    }


    public MyContentProvider() {
    }


    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                count = helper.getReadableDatabase().delete(ThingTodo.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                count = helper.getReadableDatabase().delete(ThingTodo.TABLE_NAME,  ThingTodo._ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override public String getType(Uri uri) {
        // at the given URI.
        switch (sUriMatcher.match(uri)){
            case 1:
                return "vnd.android.cursor.dir/vnd.example.thingtodos";

            case 2:
                return "vnd.android.cursor.item/vnd.example.thingtodos";


            default:

                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override public Uri insert(Uri uri, ContentValues values) {
        long rowid = helper.getWritableDatabase().insert(ThingTodo.TABLE_NAME,null,values);
        if (rowid > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowid);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }else return null;

    }

    @Override public boolean onCreate() {
        helper = new ThingTodoSqlHelper(getContext());
        return true;
    }

    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ThingTodo.TABLE_NAME);
        switch (sUriMatcher.match(uri)) {
            case 1:
                qb.setProjectionMap(new HashMap<String, String>());
                break;
            case 2:
                qb.appendWhere(ThingTodo._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                break;
        }
        return qb.query(helper.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                count = helper.getReadableDatabase().update(ThingTodo.TABLE_NAME, values, selection, selectionArgs);
                break;
            case 2:
                count = helper.getReadableDatabase().update(ThingTodo.TABLE_NAME, values,  ThingTodo._ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
