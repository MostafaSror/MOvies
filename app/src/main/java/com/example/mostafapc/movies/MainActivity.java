package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdaptor.ListItemClickListener{

    static int poster_path = 0;
    static int movie_title = 1;
    static int movie_id = 2;
    static int movie_overview = 3;
    static int movie_release_date = 4;
    static int movie_popularity = 5;
    static int movie_vote = 6;
    private String def_sort_type = "popular";

    private RecyclerView mMoviesGridView ;
    private RecyclerViewAdaptor mMoviesAdaptor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMoviesGridView = (RecyclerView) findViewById(R.id.rv_movies_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(this , 2 );
        mMoviesGridView.setLayoutManager(layoutManager);
        mMoviesGridView.setHasFixedSize(true);

        mMoviesAdaptor = new RecyclerViewAdaptor( this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(def_sort_type);
    }

    private void loadData(String sort_type) {
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

        switch (id) {
            case R.id.action_popular:{
                def_sort_type = "popular";
                loadData(def_sort_type);
                return true;
            }
            case R.id.action_rated:{
                def_sort_type = "top_rated";
                loadData(def_sort_type);
                return true;
            }
            default:{
                Toast.makeText(this, R.string.error_choose_sort, Toast.LENGTH_SHORT).show();
            }
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
                mMoviesGridView.setAdapter(mMoviesAdaptor);
            }
        }
    }
}
