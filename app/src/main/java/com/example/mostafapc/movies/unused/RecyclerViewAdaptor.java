/*

package com.example.mostafapc.movies;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.MoviesViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final String COLUMN_POSTER_PATH = "poster_path";

    ContentValues [] mMoviesData ;

    Context mContext;

    public interface ListItemClickListener {
        void onLlistItemClick(ContentValues contentValues);
    }


    public RecyclerViewAdaptor(Context context ,ListItemClickListener listener) {
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

            String tempPoster = mMoviesData[listIndex].getAsString(COLUMN_POSTER_PATH);
            Picasso.with(mContext).load(tempPoster).into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            ContentValues cv = mMoviesData[clickedPosition];
             mOnClickListener.onListItemClick(cv);
        }
    }

    public void setMoviesData(ContentValues[] moviesData) {
        mMoviesData = moviesData;
    }
}

*/
