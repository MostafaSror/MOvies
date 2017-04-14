package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mostafa-pc on 4/14/2017.
 */

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.MoviesViewHolder> {

    final private ListItemClickListener mOnClickListener;

    ContentValues [] mMoviesData ;

    Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public RecyclerViewAdaptor(Context context ,int numberOfItems ,ListItemClickListener listener) {
        mOnClickListener = listener;
        mContext = context ;
    }


    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent , false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMoviePoster;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mMoviePoster = (ImageView) itemView.findViewById(R.id.movie_poster_item);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

            String tempPoster = mMoviesData[listIndex].getAsString("COLUMN_POSTER_PATH");

            Picasso.with(mContext).load(tempPoster).into(mMoviePoster);
            //Glide.with(this).load("http://image.tmdb.org/t/p/w185/" + mMoviesData[listIndex].getAsString("poster_path")).into(mMoviePoster);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setMoviesData(ContentValues[] moviesData) {
        mMoviesData = moviesData;
    }
}
