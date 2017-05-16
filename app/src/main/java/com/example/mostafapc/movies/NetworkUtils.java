package com.example.mostafapc.movies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static private String BASE_URL =
            "http://api.themoviedb.org/3/movie";

    private static String PARAM_KEY = "api_key";

    public static URL buildMoviesUrl(String SearchQuery) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath( SearchQuery)
                .appendQueryParameter(PARAM_KEY, "6aa29c0d00989285cdf8bc920b76985c")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewsUrl(String SearchQuery) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath( SearchQuery)
                .appendPath("reviews")
                .appendQueryParameter(PARAM_KEY, "6aa29c0d00989285cdf8bc920b76985c")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailersUrl(String SearchQuery) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath( SearchQuery)
                .appendPath("videos")
                .appendQueryParameter(PARAM_KEY, Integer.toString(R.string.THE_MOVIE_DB_API_TOKEN))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}