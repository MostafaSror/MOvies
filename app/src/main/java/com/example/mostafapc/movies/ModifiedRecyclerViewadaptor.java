package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mostafapc.movies.storage.MoviesDBContract;
import com.squareup.picasso.Picasso;

/**
 * Created by mostafa-pc on 5/20/2017.
 */

public class ModifiedRecyclerViewadaptor extends CursorRecyclerViewAdaptor<ModifiedRecyclerViewadaptor.MoviesViewHolder>{

    final String COLUMN_POSTER_PATH = "poster_path";

    final private ModifiedRecyclerViewadaptor.ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(ContentValues contentValues);
    }

    public ModifiedRecyclerViewadaptor(Context context, ModifiedRecyclerViewadaptor.ListItemClickListener listener){
        super(context);
        mOnClickListener = listener;
    }


    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent , false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MoviesViewHolder viewHolder, Cursor cursor) {
        viewHolder.bind(cursor);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMoviePoster;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mMoviePoster = (ImageView) itemView.findViewById(R.id.movie_poster_item);

            itemView.setOnClickListener(this);
        }

        void bind(Cursor Movies) {

            String tempPoster = Movies.getString(Movies.getColumnIndex(COLUMN_POSTER_PATH));
            Picasso.with(getContext()).load(tempPoster).into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mCursor.moveToPosition(clickedPosition);
            ContentValues cv = new ContentValues();
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_MOVIE_ID,mCursor.getInt(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_MOVIE_ID)));
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_VOTE,mCursor.getInt(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_VOTE)));
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_POPULARITY,mCursor.getInt(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_POPULARITY)));
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_RELEASE_DATE,mCursor.getInt(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_RELEASE_DATE)));
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_TITLE,mCursor.getString(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_TITLE)));
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_POSTER_PATH,mCursor.getString(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_POSTER_PATH)));
            cv.put(MoviesDBContract.popularMoviesEntries.COLUMN_OVERVIEW,mCursor.getString(mCursor.getColumnIndex(MoviesDBContract.popularMoviesEntries.COLUMN_OVERVIEW)));
            mOnClickListener.onListItemClick(cv);
        }
    }
}
