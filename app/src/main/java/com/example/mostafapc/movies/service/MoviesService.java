package com.example.mostafapc.movies.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.mostafapc.movies.FetchMovieJsonElement;
import com.example.mostafapc.movies.NetworkUtils;
import com.example.mostafapc.movies.storage.MoviesDBContract;

import java.net.URL;

/**
 * Created by mostafa-pc on 5/21/2017.
 */

public class MoviesService extends IntentService {

    public static final String ACTION_MyIntentService = "com.example.mostafapc.movies.service.RESPONSE";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";

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

            Intent intentResponse = new Intent();
            intentResponse.setAction(ACTION_MyIntentService);
            intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
            intentResponse.putExtra(EXTRA_KEY_OUT, jsonResponse);
            sendBroadcast(intentResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
