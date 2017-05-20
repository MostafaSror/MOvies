package com.example.mostafapc.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mostafapc.movies.storage.MoviesDBContract;

import org.json.JSONException;

import java.net.URL;

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

    private static final int FETCH_POPULAR_MOVIES_FROM_INTERNET_LOADER = 10;
    private static final int FETCH_TOP_RATED_MOVIES_FROM_INTERNET_LOADER = 11;
    private static final int SHOW_MOVIES_FROM_DB_LOADER = 20;

    public interface Callback {
        void onPosterSelected(String [] dataValues);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        sharedPref = getActivity().getSharedPreferences(SORT_TYPE_PREF_FILE, Context.MODE_PRIVATE);

        loadData();

        mMoviesGridView = (RecyclerView) rootView.findViewById(R.id.rv_movies_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity() , 2 );
        mMoviesGridView.setLayoutManager(layoutManager);
        mMoviesGridView.setHasFixedSize(true);

        mMoviesAdaptor = new ModifiedRecyclerViewadaptor( getActivity(), this);
        mMoviesGridView.setAdapter(mMoviesAdaptor);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        showMoviePosters(sharedPref.getString(SORT_TYPE_PREF_KEY, "popular"));
    }

    private void loadData() {

        Bundle queryPopularBundle = new Bundle();
        Bundle queryTopRatedBundle = new Bundle();
        queryPopularBundle.putString(SEARCH_QUERY_SORT_EXTRA , "popular" );
        queryTopRatedBundle.putString(SEARCH_QUERY_SORT_EXTRA , "top_rated" );

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<ContentValues[]> queryPopularLoader = loaderManager.getLoader(FETCH_POPULAR_MOVIES_FROM_INTERNET_LOADER);
        Loader<ContentValues[]> queryTopRatedLoader = loaderManager.getLoader(FETCH_TOP_RATED_MOVIES_FROM_INTERNET_LOADER);

        if (queryPopularLoader == null){
            loaderManager.initLoader(FETCH_POPULAR_MOVIES_FROM_INTERNET_LOADER,queryPopularBundle,this).forceLoad();
        }else {
            loaderManager.restartLoader(FETCH_POPULAR_MOVIES_FROM_INTERNET_LOADER,queryPopularBundle,this).forceLoad();
        }
        if (queryTopRatedLoader == null){
            loaderManager.initLoader(FETCH_TOP_RATED_MOVIES_FROM_INTERNET_LOADER,queryTopRatedBundle,this).forceLoad();
        }else {
            loaderManager.restartLoader(FETCH_TOP_RATED_MOVIES_FROM_INTERNET_LOADER,queryTopRatedBundle,this).forceLoad();
        }
    }
    public void showMoviePosters(String sort_type) {

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_SORT_EXTRA , sort_type );
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<ContentValues[]> queryLoader = loaderManager.getLoader(SHOW_MOVIES_FROM_DB_LOADER);

        if (queryLoader == null){
            loaderManager.initLoader(SHOW_MOVIES_FROM_DB_LOADER,queryBundle,this).forceLoad();
        }else {
            loaderManager.restartLoader(SHOW_MOVIES_FROM_DB_LOADER,queryBundle,this).forceLoad();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id,final Bundle args) {
        if (id == SHOW_MOVIES_FROM_DB_LOADER ) {
            String searchType = args.getString(SEARCH_QUERY_SORT_EXTRA);

            if (searchType == "popular") {
                return new CursorLoader(getActivity(),
                        MoviesDBContract.popularMoviesEntries.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            } else if (searchType == "top_rated") {
                return new CursorLoader(getActivity(),
                        MoviesDBContract.topRatedMoviesEntries.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }
        }else {
            return new AsyncTaskLoader<Cursor>(getActivity()) {
                @Override
                public Cursor loadInBackground() {
                    String SortType = args.getString(SEARCH_QUERY_SORT_EXTRA);
                    if(SortType == null || SortType.isEmpty()){
                        return null;
                    }
                    String jsonResponse = NetworkUtils.buildMoviesUrl(SortType);

                    ContentValues[] fetchedMoviesData = new ContentValues[0];
                    try {
                        fetchedMoviesData = FetchMovieJsonElement.getMovieDataFromJson(jsonResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if ( fetchedMoviesData.length > 0 ) {
                            switch (SortType){
                                case "popular":
                                    getContext().getContentResolver().
                                            bulkInsert(MoviesDBContract.popularMoviesEntries.CONTENT_URI, fetchedMoviesData);
                                    return null;
                                case "top_rated":
                                    getContext().getContentResolver().
                                            bulkInsert(MoviesDBContract.topRatedMoviesEntries.CONTENT_URI, fetchedMoviesData);
                                    return null;
                            }
                        }

                    return null;
                }
            };
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == FETCH_POPULAR_MOVIES_FROM_INTERNET_LOADER ||
                loader.getId() == FETCH_TOP_RATED_MOVIES_FROM_INTERNET_LOADER) {
            showMoviePosters(sharedPref.getString(SORT_TYPE_PREF_KEY, "popular"));
        }
        if(loader != null && data != null ) {
            ContentValues [] retVal = new ContentValues[data.getCount()];
            ContentValues map;
            /*if (data.moveToFirst()) {
                for(int i=0 ; i < data.getCount();i++){
                    map = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(data, map);
                    data.moveToNext();
                    retVal[i] = map ;
                }
            }*/
            mMoviesAdaptor.setCursor(data);
        }
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
        showMoviePosters(sort_type);
    }
}
