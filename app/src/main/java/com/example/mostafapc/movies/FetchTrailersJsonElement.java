package com.example.mostafapc.movies;

import android.content.ContentValues;

import com.example.mostafapc.movies.storage.MoviesDBContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by mostafa-pc on 5/13/2017.
 */

public class FetchTrailersJsonElement {

    public static ContentValues[] getTrailersDataFromJson(String trailerJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "results";
        final String OWM_KEY = "key";
        final String OWM_NAME = "name";
        final String OWM_ID = "id";
        final String OWM_SITE = "site";

        try {
            JSONObject trailerJson = new JSONObject(trailerJsonStr);
            JSONArray trailersArray = trailerJson.getJSONArray(OWM_LIST);

            int movieID = trailerJson.getInt(OWM_ID);

            // Insert the new trailers information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(trailersArray.length());


            for(int i = 0; i < trailersArray.length(); i++) {

                JSONObject trailerObject = trailersArray.getJSONObject(i);
                ContentValues trailerValues = new ContentValues();

                String key = trailerObject.getString(OWM_KEY);
                String name = trailerObject.getString(OWM_NAME);
                String site = trailerObject.getString(OWM_SITE);

                trailerValues.put(MoviesDBContract.trailersEntry.COLUMN_MOVIE_ID, movieID);
                trailerValues.put(MoviesDBContract.trailersEntry.COLUMN_KEY, key);
                trailerValues.put(MoviesDBContract.trailersEntry.COLUMN_TRAILER_NAME, name + "(" + site + ")");

                cVVector.add(trailerValues);
            }

            ContentValues[] cvArray = new ContentValues[cVVector.size()];

            // add to database
            if ( cVVector.size() > 0 ) {
                cVVector.toArray(cvArray);
                //inserted = geContext.getContentResolver().bulkInsert(trailersEntry.CONTENT_URI, cvArray);
            }
            return cvArray;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
