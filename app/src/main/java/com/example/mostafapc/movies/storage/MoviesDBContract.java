package com.example.mostafapc.movies.storage;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mostafa-pc on 5/13/2017.
 */

public class MoviesDBContract {

    // The "Content authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.example.mostafapc.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths
    public static final String PATH_POPULAR_MOVIES = "popular_Movies";
    public static final String PATH_TOP_RATED_MOVIES = "top_Rated_Movies";
    public static final String PATH_FAVOURITE_MOVIES = "favourite_Movies";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";

    static final int POPULAR_MOVIES_URI = 100;
    static final int TOP_RATED_MOVIES_URI = 101;
    static final int FAVOURITE_MOVIES_URI = 102;

    static final int POPULAR_MOVIE_ID = 103;
    static final int TOP_RATED_MOVIE_ID = 104;
    static final int FAVOURITE_MOVIE_ID = 105;

    static final int TRAILERS = 201;
    static final int REVIEWS = 301;

    //Inner class that defines the movies table
    public static final class popularMoviesEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIES;

        public static final String TABLE_NAME = "popularMovies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE = "vote";

        public static Uri buildMovieUri(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath( Long.toString(id)).build();
        }

        public static String[] getMovieIDFromUri(Uri uri) {
            String[] temp = {uri.getPathSegments().get(1)};
            return temp;
        }

    }

    public static final class topRatedMoviesEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED_MOVIES;

        public static final String TABLE_NAME = "topRatedMovies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE = "vote";

        public static Uri buildMovieUri(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath( Long.toString(id)).build();
        }

        public static String[] getMovieIDFromUri(Uri uri) {
            String[] temp = {uri.getPathSegments().get(1)};
            return temp;
        }
    }

    public static final class favouriteMoviesEntries implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIES;

        public static final String TABLE_NAME = "favouriteMovies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE = "vote";

        public static Uri buildMovieUri(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath( Long.toString(id)).build();
        }

        public static String[] getMovieIDFromUri(Uri uri) {
            String[] temp = {uri.getPathSegments().get(1)};
            return temp;
        }
    }

    /* Inner class that defines the contents of the movies trailers table */
    public static final class trailersEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        public static final String COLUMN_KEY = "key";

        public static Uri buildTrailerWithMovieIDUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static String[] getMovieIDFromUri(Uri uri) {
            String[] temp = {uri.getPathSegments().get(1)};
            return temp;
        }
    }

    /* Inner class that defines the contents of the movies reviews table */
    public static final class reviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildReviewsWithMovieIDUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
