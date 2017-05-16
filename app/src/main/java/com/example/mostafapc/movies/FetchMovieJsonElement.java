package com.example.mostafapc.movies;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import static android.app.PendingIntent.getActivity;

/**
 * Created by mostafa-pc on 4/14/2017.
 */

public class FetchMovieJsonElement {

    public static ContentValues[] getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        ContentValues[] cvArray ;

        final String COLUMN_MOVIE_ID = "movie_id";
        final String COLUMN_TITLE = "title";
        final String COLUMN_OVERVIEW = "overview";
        final String COLUMN_RELEASE_DATE = "release_date";
        final String COLUMN_POSTER_PATH = "poster_path";
        final String COLUMN_POPULARITY = "popularity";
        final String COLUMN_VOTE = "vote";

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "results";
        final String OWM_ID = "id";
        final String OWM_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_DATE = "release_date";
        final String OWM_PATH = "poster_path";
        final String OWM_POPULARITY = "popularity";
        final String OWM_VOTE = "vote_average";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray(OWM_LIST);



        Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesArray.length());

        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject movieObject = moviesArray.getJSONObject(i);
            ContentValues movieValues = new ContentValues();

            String id = movieObject.getString(OWM_ID);
            String title = movieObject.getString(OWM_TITLE);
            String overview = movieObject.getString(OWM_OVERVIEW);
            String date = movieObject.getString(OWM_DATE);
            String path = movieObject.getString(OWM_PATH);
            String url = "http://image.tmdb.org/t/p/w185/" + path;
            String popularity = movieObject.getString(OWM_POPULARITY);
            String vote = movieObject.getString(OWM_VOTE);


            movieValues.put(COLUMN_MOVIE_ID, id);
            movieValues.put(COLUMN_TITLE, title);
            movieValues.put(COLUMN_OVERVIEW, overview);
            movieValues.put(COLUMN_RELEASE_DATE, date);
            movieValues.put(COLUMN_POSTER_PATH, url);
            movieValues.put(COLUMN_POPULARITY, popularity);
            movieValues.put(COLUMN_VOTE, vote);

            cVVector.add(movieValues);
        }

        int inserted = 0;
        cvArray = new ContentValues[cVVector.size()];
        // add values of movies to database
        if ( cVVector.size() > 0 ) {
            cVVector.toArray(cvArray);
        }

        return cvArray;
    }
}