package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import java.net.URL;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdaptor.ListItemClickListener{

    int poster_path = 0;
    int movie_title = 1;
    int movie_id = 2;
    int movie_overview = 3;
    int movie_release_date = 4;
    int movie_popularity = 5;
    int movie_vote = 6;

    SharedPreferences prefs;

    private int numberofItems ;
    private RecyclerView mMoviesGridView ;
    private RecyclerViewAdaptor mMoviesAdaptor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        mMoviesGridView = (RecyclerView) findViewById(R.id.rv_movies_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(this , 2 );
        mMoviesGridView.setLayoutManager(layoutManager);
        mMoviesGridView.setHasFixedSize(true);

        //loadData();

        numberofItems = 20 ;
        mMoviesAdaptor = new RecyclerViewAdaptor( this ,numberofItems, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {

        String sort_type = prefs.getString(getString(R.string.pref_order_key)
                ,getString(R.string.pref_order_popular));
        new FetchMoviesTask().execute(sort_type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.order) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ContentValues clickedItem) {

        String [] movieDataString = new String[clickedItem.size()];
        movieDataString[poster_path] = clickedItem.getAsString("COLUMN_POSTER_PATH");
        movieDataString[movie_title] = clickedItem.getAsString("COLUMN_TITLE");
        movieDataString[movie_id] = clickedItem.getAsString("COLUMN_MOVIE_ID");
        movieDataString[movie_overview] = clickedItem.getAsString("COLUMN_OVERVIEW");
        movieDataString[movie_release_date] = clickedItem.getAsString("COLUMN_RELEASE_DATE");
        movieDataString[movie_popularity] = clickedItem.getAsString("COLUMN_POPULARITY");
        movieDataString[movie_vote] = clickedItem.getAsString("COLUMN_VOTE");

        Intent intent = new Intent(MainActivity.this , DetailActivity.class );
        intent.putExtra(Intent.EXTRA_TEXT, movieDataString);
        startActivity(intent);

    }

    public class FetchMoviesTask extends AsyncTask<String ,Void, ContentValues []> {

        @Override
        protected ContentValues [] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String sortType = params[0];
            URL RequestUrl = NetworkUtils.buildUrl(sortType);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(RequestUrl);

                ContentValues [] fetchedMoviesData = FetchMovieJsonElement.getMovieDataFromJson(jsonResponse);

                mMoviesAdaptor.setMoviesData(fetchedMoviesData);

                return fetchedMoviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ContentValues [] moviesData) {

            if (moviesData != null) {
                numberofItems = moviesData.length ;
                mMoviesGridView.setAdapter(mMoviesAdaptor);

            } else {

            }
        }
    }
}
