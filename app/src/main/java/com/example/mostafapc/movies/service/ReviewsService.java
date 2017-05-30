package com.example.mostafapc.movies.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.mostafapc.movies.NetworkUtils;

import java.net.URL;

/**
 * Created by Moustafa.Mamdouh on 5/27/2017.
 */

public class ReviewsService extends IntentService {
    public static final String ACTION_MyIntentService = "com.example.mostafapc.movies.service.RESPONSE2";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";

    private static final String SEARCH_QUERY_MOVIE_ID_EXTRA = "search_by_id";


    public ReviewsService() {
        super("ReviewsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String movieIDNo = intent.getStringExtra(SEARCH_QUERY_MOVIE_ID_EXTRA);
        if(movieIDNo == null || movieIDNo.isEmpty()){
            return;
        }

        URL url = NetworkUtils.buildReviewsUrl(movieIDNo);

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
