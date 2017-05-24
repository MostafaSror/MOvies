package com.example.mostafapc.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mostafapc.movies.service.MoviesService;
import com.example.mostafapc.movies.storage.MoviesDBContract;

import org.json.JSONException;

/**
 * Created by mostafa-pc on 5/15/2017.
 */

public class FragmentMain extends Fragment implements ModifiedRecyclerViewadaptor.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    static int poster_path = 0;
    static int movie_title = 1;
    static int movie_id = 2;
    static int movie_overview = 3;
    static int movie_release_date = 4;
    static int movie_popularity = 5;
    static int movie_vote = 6;

    private final static String SEARCH_QUERY_SORT_EXTRA = "query_sort_type";
    private static final String SORT_TYPE_PREF_FILE = "sort_type_file";
    private static final String SORT_TYPE_PREF_KEY = "sort_type_key";

    SharedPreferences sharedPref ;
    private RecyclerView mMoviesGridView ;
    private ModifiedRecyclerViewadaptor mMoviesAdaptor ;


    private MyBroadcastReceiver myBroadcastReceiver;

    private static final int SHOW_MOVIES_FROM_DB_LOADER = 20;

    public interface Callback {
        void onPosterSelected(String [] dataValues);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        sharedPref = getActivity().getSharedPreferences(SORT_TYPE_PREF_FILE, Context.MODE_PRIVATE);
        mMoviesGridView = (RecyclerView) rootView.findViewById(R.id.rv_movies_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity() , 2 );
        mMoviesGridView.setLayoutManager(layoutManager);
        //mMoviesGridView.setHasFixedSize(true);

        mMoviesAdaptor = new ModifiedRecyclerViewadaptor( getActivity(), this);
        mMoviesGridView.setAdapter(mMoviesAdaptor);
        loadData();

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBroadcastReceiver = new MyBroadcastReceiver();

        //register BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(MoviesService.ACTION_MyIntentService);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getContext().registerReceiver(myBroadcastReceiver, intentFilter);

    }
    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonResponse = intent.getStringExtra(MoviesService.EXTRA_KEY_OUT);

            try {
                ContentValues[] fetchedMoviesData = FetchMovieJsonElement.getMovieDataFromJson(jsonResponse);

                if ( fetchedMoviesData.length > 0 ) {
                    switch (sharedPref.getString(SORT_TYPE_PREF_KEY, "popular")){
                        case "popular":
                            getActivity().getContentResolver().
                                    bulkInsert(MoviesDBContract.popularMoviesEntries.CONTENT_URI, fetchedMoviesData);
                            showMoviePosters(sharedPref.getString(SORT_TYPE_PREF_KEY, "popular"));
                            return;
                        case "top_rated":
                            getActivity().getContentResolver().
                                    bulkInsert(MoviesDBContract.topRatedMoviesEntries.CONTENT_URI, fetchedMoviesData);
                            showMoviePosters(sharedPref.getString(SORT_TYPE_PREF_KEY, "popular"));
                            return;
                        default:
                            Toast.makeText( getActivity(), "Favourites", Toast.LENGTH_LONG ).show();
                            return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //un-register BroadcastReceiver
        getContext().unregisterReceiver(myBroadcastReceiver);

    }

    private void loadData() {

        Intent intent = new Intent(getActivity(), MoviesService.class);
        intent.putExtra(SEARCH_QUERY_SORT_EXTRA, sharedPref.getString(SORT_TYPE_PREF_KEY, "popular") );
        getActivity().startService(intent);
    }

    public void showMoviePosters(String sort_type) {

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_SORT_EXTRA , sort_type );
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<ContentValues[]> queryLoader = loaderManager.getLoader(SHOW_MOVIES_FROM_DB_LOADER);

        if (queryLoader == null){
            loaderManager.initLoader(SHOW_MOVIES_FROM_DB_LOADER,queryBundle,this);
        }else {
            loaderManager.restartLoader(SHOW_MOVIES_FROM_DB_LOADER,queryBundle,this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id,final Bundle args) {

            String searchType = args.getString(SEARCH_QUERY_SORT_EXTRA);
            if (searchType == "popular") {
                CursorLoader loader = new CursorLoader(getActivity(),
                        MoviesDBContract.popularMoviesEntries.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                return loader;
            } else if (searchType == "top_rated") {
                CursorLoader loader = new CursorLoader(getActivity(),
                        MoviesDBContract.topRatedMoviesEntries.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                return loader;
            }else if (searchType == "favourite") {
                return new CursorLoader(getActivity(),
                        MoviesDBContract.favouriteMoviesEntries.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }
            return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdaptor.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdaptor.swapCursor(null);
    }

    @Override
    public void onListItemClick(ContentValues clickedItem) {
        String [] movieDataString = new String[clickedItem.size()];
        movieDataString[poster_path] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_POSTER_PATH);
        movieDataString[movie_title] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_TITLE);
        movieDataString[movie_id] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_MOVIE_ID);
        movieDataString[movie_overview] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_OVERVIEW);
        movieDataString[movie_release_date] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_RELEASE_DATE);
        movieDataString[movie_popularity] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_POPULARITY);
        movieDataString[movie_vote] = clickedItem.getAsString(MoviesDBContract.popularMoviesEntries.COLUMN_VOTE);

        ((Callback) getActivity())
                .onPosterSelected(movieDataString);
    }

    void onSortOrderChanged(String sort_type ) {
        if(sort_type == "popular" || sort_type == "top_rated")
            loadData();
        if(sort_type == "favourite")
            showMoviePosters(sort_type);
    }



}
