package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.net.URL;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdaptor.ListItemClickListener{

    private int numberofItems ;

    private RecyclerView mMoviesGridView ;
    private RecyclerViewAdaptor mMoviesAdaptor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGridView = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(this , 2 );

        mMoviesGridView.setLayoutManager(layoutManager);
        mMoviesGridView.setHasFixedSize(true);

        loadData();

        numberofItems = 20 ;

        mMoviesAdaptor = new RecyclerViewAdaptor( this ,numberofItems, this);

        //mMoviesGridView.setAdapter(mMoviesAdaptor);

    }

    private void loadData() {

        String sort_type = "popular" ; //= SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchMoviesTask().execute(sort_type);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

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
