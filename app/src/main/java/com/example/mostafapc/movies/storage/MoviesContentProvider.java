package com.example.mostafapc.movies.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import static com.example.mostafapc.movies.storage.MoviesDBContract.*;

/**
 * Created by mostafa-pc on 5/13/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesDBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesDBContract.PATH_POPULAR_MOVIES , POPULAR_MOVIES_URI);
        matcher.addURI(authority, MoviesDBContract.PATH_TOP_RATED_MOVIES , TOP_RATED_MOVIES_URI);
        matcher.addURI(authority, MoviesDBContract.PATH_FAVOURITE_MOVIES , FAVOURITE_MOVIES_URI);

        matcher.addURI(authority, MoviesDBContract.PATH_TRAILERS , TRAILERS);
        matcher.addURI(authority, MoviesDBContract.PATH_REVIEWS , REVIEWS);

        return matcher;
    }

    MoviesDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case POPULAR_MOVIES_URI:
                retCursor = db.query(
                        MoviesDBContract.popularMoviesEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TOP_RATED_MOVIES_URI:

                retCursor = db.query(
                        MoviesDBContract.topRatedMoviesEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FAVOURITE_MOVIES_URI:

                retCursor = db.query(
                        MoviesDBContract.favouriteMoviesEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TRAILERS:
                retCursor = db.query(
                        MoviesDBContract.trailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REVIEWS:
                retCursor = db.query(
                        MoviesDBContract.reviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR_MOVIES_URI:
                return MoviesDBContract.popularMoviesEntries.CONTENT_TYPE;
            case TOP_RATED_MOVIES_URI:
                return MoviesDBContract.topRatedMoviesEntries.CONTENT_TYPE;
            case FAVOURITE_MOVIES_URI:
                return MoviesDBContract.favouriteMoviesEntries.CONTENT_TYPE;
            case POPULAR_MOVIE_ID:
                return MoviesDBContract.popularMoviesEntries.CONTENT_ITEM_TYPE;
            case TOP_RATED_MOVIE_ID:
                return MoviesDBContract.topRatedMoviesEntries.CONTENT_ITEM_TYPE;
            case FAVOURITE_MOVIE_ID:
                return MoviesDBContract.favouriteMoviesEntries.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return MoviesDBContract.trailersEntry.CONTENT_TYPE;
            case REVIEWS:
                return MoviesDBContract.reviewsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVOURITE_MOVIES_URI: {
                long _id = db.insert(MoviesDBContract.favouriteMoviesEntries.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesDBContract.favouriteMoviesEntries.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVOURITE_MOVIES_URI: {
                long _id = db.delete(MoviesDBContract.favouriteMoviesEntries.TABLE_NAME,
                        favouriteMoviesEntries.COLUMN_MOVIE_ID + "=?",selectionArgs );
                if ( _id > 0 )
                    return 1;
                else
                    return 0;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {

            case POPULAR_MOVIES_URI:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        Cursor retCursor = db.query(
                                MoviesDBContract.popularMoviesEntries.TABLE_NAME,
                                null,
                                popularMoviesEntries.COLUMN_MOVIE_ID + "=?",
                                new String[]{value.getAsString(popularMoviesEntries.COLUMN_MOVIE_ID)},
                                null,
                                null,
                                null
                        );
                        if(retCursor.moveToFirst() == false){
                            long _id = db.insert(MoviesDBContract.popularMoviesEntries.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return returnCount;


            case TOP_RATED_MOVIES_URI:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        Cursor retCursor = db.query(
                                MoviesDBContract.topRatedMoviesEntries.TABLE_NAME,
                                null,
                                popularMoviesEntries.COLUMN_MOVIE_ID + "=?",
                                new String[]{value.getAsString(popularMoviesEntries.COLUMN_MOVIE_ID)},
                                null,
                                null,
                                null
                        );
                        if(retCursor.moveToFirst() == false){
                            long _id = db.insert(MoviesDBContract.topRatedMoviesEntries.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return returnCount;


            case FAVOURITE_MOVIES_URI:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.favouriteMoviesEntries.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            case TRAILERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.trailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            case REVIEWS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.reviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            default:
                return 0;
//                        super.bulkInsert(uri, values);
        }
    }

}
