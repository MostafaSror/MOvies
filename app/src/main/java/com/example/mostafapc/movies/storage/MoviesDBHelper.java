package com.example.mostafapc.movies.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mostafapc.movies.storage.MoviesDBContract.*;
/**
 * Created by mostafa-pc on 5/13/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " + popularMoviesEntries.TABLE_NAME + " (" +
                popularMoviesEntries._ID + " INTEGER PRIMARY KEY," +
                popularMoviesEntries.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                popularMoviesEntries.COLUMN_TITLE + " TEXT NOT NULL, " +
                popularMoviesEntries.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                popularMoviesEntries.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                popularMoviesEntries.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                popularMoviesEntries.COLUMN_POPULARITY + " FLOAT NOT NULL, " +
                popularMoviesEntries.COLUMN_VOTE + " FLOAT NOT NULL " +
                " );";
        final String SQL_CREATE_TOP_RATED_MOVIES_TABLE = "CREATE TABLE " + topRatedMoviesEntries.TABLE_NAME + " (" +
                topRatedMoviesEntries._ID + " INTEGER PRIMARY KEY," +
                topRatedMoviesEntries.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                topRatedMoviesEntries.COLUMN_TITLE + " TEXT NOT NULL, " +
                topRatedMoviesEntries.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                topRatedMoviesEntries.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                topRatedMoviesEntries.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                topRatedMoviesEntries.COLUMN_POPULARITY + " FLOAT NOT NULL, " +
                topRatedMoviesEntries.COLUMN_VOTE + " FLOAT NOT NULL " +
                " );";
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " + favouriteMoviesEntries.TABLE_NAME + " (" +
                favouriteMoviesEntries._ID + " INTEGER PRIMARY KEY," +
                favouriteMoviesEntries.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                favouriteMoviesEntries.COLUMN_TITLE + " TEXT NOT NULL, " +
                favouriteMoviesEntries.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                favouriteMoviesEntries.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                favouriteMoviesEntries.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                favouriteMoviesEntries.COLUMN_POPULARITY + " FLOAT NOT NULL, " +
                favouriteMoviesEntries.COLUMN_VOTE + " FLOAT NOT NULL " +
                " );";
        // Create a table to hold trailers.
        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + trailersEntry.TABLE_NAME + " (" +
                trailersEntry._ID + " INTEGER PRIMARY KEY," +
                trailersEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                trailersEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                trailersEntry.COLUMN_KEY + " TEXT NOT NULL " +
                " );";
        // Create a table to hold reviews.
        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + reviewsEntry.TABLE_NAME + " (" +
                reviewsEntry._ID + " INTEGER PRIMARY KEY," +
                reviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                reviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                reviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + popularMoviesEntries.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + topRatedMoviesEntries.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + favouriteMoviesEntries.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + trailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + reviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
