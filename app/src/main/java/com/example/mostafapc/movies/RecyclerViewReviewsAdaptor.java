package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafapc.movies.storage.MoviesDBContract;

/**
 * Created by mostafa-pc on 5/14/2017.
 */

public class RecyclerViewReviewsAdaptor extends RecyclerView.Adapter<RecyclerViewReviewsAdaptor.MoviesViewHolder> {

    final private ListReviewsClickListener mOnClickListener;

    ContentValues[] mMoviesData ;

    Context mContext;

    public interface ListReviewsClickListener {
        void onListReviewsClick(ContentValues contentValues);
    }

    public RecyclerViewReviewsAdaptor(Context context ,ListReviewsClickListener listener) {
        mOnClickListener = listener;
        mContext = context ;
    }

    @Override
    public RecyclerViewReviewsAdaptor.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_review_layout, parent , false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewReviewsAdaptor.MoviesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mMovieOverview;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            mMovieOverview = (TextView) itemView.findViewById(R.id.review_item);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            String tempPoster = mMoviesData[listIndex].getAsString(MoviesDBContract.reviewsEntry.COLUMN_CONTENT);
            mMovieOverview.setText(tempPoster);
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            ContentValues cv = mMoviesData[clickedPosition];
            mOnClickListener.onListReviewsClick(cv);
        }
    }

    public void setMoviesData(ContentValues[] moviesData) {
        mMoviesData = moviesData;
    }
}

