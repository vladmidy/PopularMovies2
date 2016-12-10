package com.ciscoedge.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ciscoedge.popularmovies.data.MovieContract.MovieEntry;
import com.ciscoedge.popularmovies.data.MovieContract.VideoEntry;
import com.ciscoedge.popularmovies.data.MovieContract.ReviewEntry;


public class  MovieDbHelper extends SQLiteOpenHelper {


    private static final String LOGTAG = "MOVIE_APP";


    private static final int DATABASE_VERSION = 15;


    static final String DATABASE_NAME = "popularmovies.db";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold movies.
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_ORIGINAL_TILE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TOP_RATED + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULAR + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_FAVORITE + " TEXT DEFAULT 'false' " +
                " );";

        // Create a table to hold video youtube links.
        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                VideoEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_VIDEO_TYPE + " TEXT NOT NULL " +
                " );";


        // Create a table to hold reviews.
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);

        Log.i(LOGTAG, "Table: " + MovieEntry.TABLE_NAME + " has been created.");
        Log.i(LOGTAG, "Table: " + VideoEntry.TABLE_NAME + " has been created.");
        Log.i(LOGTAG, "Table: " + ReviewEntry.TABLE_NAME + " has been created.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // This code block only runs if there was a change to the version number of the database.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);

        Log.i(LOGTAG, "Table: " + MovieEntry.TABLE_NAME + " has been dropped.");
        Log.i(LOGTAG, "Table: " + VideoEntry.TABLE_NAME + " has been dropped.");
        Log.i(LOGTAG, "Table: " + ReviewEntry.TABLE_NAME + " has been dropped.");
    }
}
