package com.example.mostafapc.movies;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mostafapc.movies.service.MoviesService;
import com.example.mostafapc.movies.service.ReviewsService;
import com.example.mostafapc.movies.service.TrailersService;
import com.example.mostafapc.movies.storage.MoviesDBContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * Created by mostafa-pc on 5/16/2017.
 */

public class FragmentDetail extends Fragment implements RecyclerViewTextAdaptor.ListItemClickListener,
        RecyclerViewReviewsAdaptor.ListReviewsClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    int poster_path = 0;
    int movie_title = 1;
    int movie_id = 2;
    int movie_overview = 3;
    int movie_release_date = 4;
    static int movie_popularity = 5;
    int movie_vote = 6;

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVote;
    private TextView mOverview;
    private RecyclerView reviewRecyclerView;
    private RecyclerView trailersRecyclerView;

    RecyclerViewTextAdaptor trailersRecyclerViewAdaptor;
    RecyclerViewReviewsAdaptor reviewsRecyclerViewAdaptor;

    private String movie_entry_id;

    private static final String SEARCH_QUERY_MOVIE_ID_EXTRA = "search_by_id";

    private static final int FETCH_REVIEWS_FROM_INTERNET_LOADER = 100;
    private static final int FETCH_TRAILERS_FROM_INTERNET_LOADER = 101;
    private static final int SHOW_REVIEWS_FROM_DB_LOADER = 200;
    private static final int SHOW_TRAILERS_FROM_DB_LOADER = 201;

    private  MyReviewsReceiver myReviewsReceiver;
    private  MyTrailersReceiver myTrailersReceiver;

    class MyReviewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonResponse = intent.getStringExtra(ReviewsService.EXTRA_KEY_OUT);

            try {
                ContentValues[] fetchedMoviesData = FetchReviewsJsonElement.getTrailersDataFromJson(jsonResponse);

                if ( fetchedMoviesData.length > 0 ) {

                    getContext().getContentResolver().
                                    bulkInsert(MoviesDBContract.reviewsEntry.CONTENT_URI, fetchedMoviesData);
                    showMoviePosters(movie_entry_id , "reviews");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    class MyTrailersReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonResponse = intent.getStringExtra(TrailersService.EXTRA_KEY_OUT);

            try {
                ContentValues[] fetchedMoviesData = FetchTrailersJsonElement.getTrailersDataFromJson(jsonResponse);

                if ( fetchedMoviesData.length > 0 ) {

                    getContext().getContentResolver().
                                bulkInsert(MoviesDBContract.trailersEntry.CONTENT_URI, fetchedMoviesData);
                    showMoviePosters(movie_entry_id , "trailers");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_reviews_grid);
        trailersRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_trailers_grid);
        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(getActivity());

        reviewRecyclerView.setLayoutManager(reviewsLayoutManager);
        trailersRecyclerView.setLayoutManager(trailersLayoutManager);

        trailersRecyclerViewAdaptor = new RecyclerViewTextAdaptor(getActivity(),this);
        reviewsRecyclerViewAdaptor = new RecyclerViewReviewsAdaptor(getActivity(),this);
        final String [] movieItemDetails =getArguments().getStringArray("edttext");

        mPoster = (ImageView) rootView.findViewById(R.id.details_movie_poster);
        mTitle = (TextView) rootView.findViewById(R.id.details_title_view);
        mReleaseDate = (TextView) rootView.findViewById(R.id.details_release_date);
        mVote = (TextView) rootView.findViewById(R.id.details_vote_view);
        mOverview = (TextView) rootView.findViewById(R.id.details_overview);

        final ToggleButton toggleButton;

        toggleButton = (ToggleButton) rootView.findViewById(R.id.myToggleButton);
        toggleButton.setChecked(false);
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.img_star_grey));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.img_star_yellow));

                    ContentValues cv = new ContentValues();
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_POSTER_PATH, movieItemDetails[poster_path]);
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_TITLE, movieItemDetails[movie_title]);
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_MOVIE_ID, movieItemDetails[movie_id]);
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_OVERVIEW, movieItemDetails[movie_overview]);
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_RELEASE_DATE, movieItemDetails[movie_release_date]);
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_POPULARITY, movieItemDetails[movie_popularity]);
                    cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_VOTE, movieItemDetails[movie_vote]);

                    getContext().getContentResolver().insert(MoviesDBContract.favouriteMoviesEntries.CONTENT_URI,cv);

                    Toast.makeText(getContext(),"Added To Favourites",Toast.LENGTH_SHORT).show();
                }else{
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.img_star_grey));
                    getContext().getContentResolver().delete(MoviesDBContract.favouriteMoviesEntries.CONTENT_URI,
                            MoviesDBContract.favouriteMoviesEntries.COLUMN_MOVIE_ID,
                            new String[]{movieItemDetails[movie_id]});
            }}
        });

        if ( movieItemDetails.length != 0) {
            Picasso.with(getActivity()).load(movieItemDetails[poster_path]).into(mPoster);
            mTitle.setText(movieItemDetails[movie_title]);
            mReleaseDate.setText(movieItemDetails[movie_release_date]);
            mVote.setText(movieItemDetails[movie_vote]);
            mOverview.setText(movieItemDetails[movie_overview]);
            movie_entry_id = movieItemDetails[movie_id];
        }
        else {
            Toast.makeText(getActivity(),"unable to fetch movie data" , Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReviewsReceiver = new MyReviewsReceiver();
        myTrailersReceiver = new MyTrailersReceiver();

        IntentFilter intentReviewsFilter = new IntentFilter(ReviewsService.ACTION_MyIntentService);
        IntentFilter intentTrailersFilter = new IntentFilter(TrailersService.ACTION_MyIntentService);

        intentReviewsFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentTrailersFilter.addCategory(Intent.CATEGORY_DEFAULT);

        getContext().registerReceiver(myReviewsReceiver, intentReviewsFilter);
        getContext().registerReceiver(myTrailersReceiver, intentTrailersFilter);
    }

    @Override
    public void onStart(){
        super.onStart();
        loadData(movie_entry_id);
    }
    private void loadData(String movie_ID) {

        Intent reviewsIntent = new Intent(getActivity(), ReviewsService.class);
        reviewsIntent.putExtra(SEARCH_QUERY_MOVIE_ID_EXTRA, movie_ID );
        getActivity().startService(reviewsIntent);

        Intent trailersIntent = new Intent(getActivity(), TrailersService.class);
        trailersIntent.putExtra(SEARCH_QUERY_MOVIE_ID_EXTRA, movie_ID );
        getActivity().startService(trailersIntent);
    }
    private void showMoviePosters(String sort_id , String dataType) {

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_MOVIE_ID_EXTRA , sort_id );
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();

        switch (dataType){
            case "reviews":
                android.support.v4.content.Loader<ContentValues[]> queryReviewsLoader =
                        loaderManager.getLoader(SHOW_REVIEWS_FROM_DB_LOADER);
                if (queryReviewsLoader == null){
                    loaderManager.initLoader(SHOW_REVIEWS_FROM_DB_LOADER,queryBundle,this).forceLoad();
                }else {
                    loaderManager.restartLoader(SHOW_REVIEWS_FROM_DB_LOADER,queryBundle,this).forceLoad();
                }
                break;
            case "trailers":
                android.support.v4.content.Loader<ContentValues[]> queryTrailersLoader =
                        loaderManager.getLoader(SHOW_TRAILERS_FROM_DB_LOADER);
                if (queryTrailersLoader == null){
                    loaderManager.initLoader(SHOW_TRAILERS_FROM_DB_LOADER,queryBundle,this).forceLoad();
                }else {
                    loaderManager.restartLoader(SHOW_TRAILERS_FROM_DB_LOADER,queryBundle,this).forceLoad();
                }
                break;

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        String searchID = args.getString(SEARCH_QUERY_MOVIE_ID_EXTRA);
        if ( id == SHOW_REVIEWS_FROM_DB_LOADER){
            CursorLoader loader = new CursorLoader(getActivity(),
                    MoviesDBContract.reviewsEntry.CONTENT_URI,
                    null,
                    MoviesDBContract.reviewsEntry.COLUMN_MOVIE_ID + "=?",
                    new String[]{searchID},
                    null);
            return loader;
        } else {
            CursorLoader loader = new CursorLoader(getActivity(),
                    MoviesDBContract.trailersEntry.CONTENT_URI,
                    null,
                    MoviesDBContract.trailersEntry.COLUMN_MOVIE_ID + "=?",
                    new String[]{searchID},
                    null);
            return loader;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            ContentValues [] retVal = new ContentValues[data.getCount()];
            ContentValues map;
            if (data.moveToFirst()) {
                for(int i=0 ; i < data.getCount();i++){
                    map = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(data, map);
                    data.moveToNext();
                    retVal[i] = map ;
                }
            }
            if(loader.getId() == SHOW_REVIEWS_FROM_DB_LOADER){
                reviewsRecyclerViewAdaptor.setMoviesData(retVal);
                reviewRecyclerView.setAdapter(reviewsRecyclerViewAdaptor);
            }else {
                trailersRecyclerViewAdaptor.setMoviesData(retVal);
                trailersRecyclerView.setAdapter(trailersRecyclerViewAdaptor);
            }
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onListReviewsClick(ContentValues clickedItem) {
        String [] movieDataString = new String[clickedItem.size()];
        movieDataString[0] = clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_MOVIE_ID);
        movieDataString[1] = clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_KEY);
        movieDataString[2] = clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_TRAILER_NAME);
    }

    @Override
    public void onListItemClick(ContentValues clickedItem) {
        String [] movieDataString = new String[clickedItem.size()];
        movieDataString[0] = clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_MOVIE_ID);
        movieDataString[1] = clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_KEY);
        movieDataString[2] = clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_TRAILER_NAME);

        String url = "https://www.youtube.com/watch?v="
                + clickedItem.getAsString(MoviesDBContract.trailersEntry.COLUMN_KEY);
        launchYoutube(getActivity(),url);
    }
    public static void launchYoutube(Context context, String url) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
