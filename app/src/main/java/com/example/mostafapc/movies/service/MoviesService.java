package com.example.mostafapc.movies.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.mostafapc.movies.FetchMovieJsonElement;
import com.example.mostafapc.movies.NetworkUtils;
import com.example.mostafapc.movies.storage.MoviesDBContract;

import org.json.JSONException;

import java.net.URL;

/**
 * Created by mostafa-pc on 5/21/2017.
 */

public class MoviesService extends IntentService {

    private final static String SEARCH_QUERY_SORT_EXTRA = "query_sort_type";

    public MoviesService() {
        super("MoviesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String SortType = intent.getStringExtra(SEARCH_QUERY_SORT_EXTRA);
        if(SortType == null || SortType.isEmpty()){
            return;
        }

        URL url = NetworkUtils.buildMoviesUrl(SortType);

        try {

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

            ContentValues[] fetchedMoviesData = FetchMovieJsonElement.getMovieDataFromJson(jsonResponse);

            if ( fetchedMoviesData.length > 0 ) {
                switch (SortType){
                    case "popular":
                        getContentResolver().
                                bulkInsert(MoviesDBContract.popularMoviesEntries.CONTENT_URI, fetchedMoviesData);
                        return;
                    case "top_rated":
                        getContentResolver().
                                bulkInsert(MoviesDBContract.topRatedMoviesEntries.CONTENT_URI, fetchedMoviesData);
                        return;
                    default:
                        Toast.makeText( this, "Favourites", Toast.LENGTH_LONG ).show();
                        return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
