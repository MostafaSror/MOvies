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

public class FetchReviewsJsonElement {

    public static ContentValues[] getTrailersDataFromJson(String reviewsJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "results";
        final String OWM_KEY = "author";
        final String OWM_NAME = "content";
        final String OWM_ID = "id";

        try {
            JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
            JSONArray reviewsArray = reviewsJson.getJSONArray(OWM_LIST);

            int movieID = reviewsJson.getInt(OWM_ID);

            // Insert the new trailers information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(reviewsArray.length());


            for (int i = 0; i < reviewsArray.length(); i++) {

                JSONObject reviewObject = reviewsArray.getJSONObject(i);
                ContentValues trailerValues = new ContentValues();

                String authorName = reviewObject.getString(OWM_KEY);
                String review = reviewObject.getString(OWM_NAME);

                trailerValues.put(MoviesDBContract.reviewsEntry.COLUMN_MOVIE_ID, movieID);
                trailerValues.put(MoviesDBContract.reviewsEntry.COLUMN_AUTHOR, authorName);
                trailerValues.put(MoviesDBContract.reviewsEntry.COLUMN_CONTENT, review);

                cVVector.add(trailerValues);
            }
            ContentValues[] cvArray = new ContentValues[cVVector.size()];

            // add to database
            if (cVVector.size() > 0) {
                cVVector.toArray(cvArray);
                //mContext.getContentResolver().bulkInsert(reviewsEntry.CONTENT_URI, cvArray);
            }

            return cvArray;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}