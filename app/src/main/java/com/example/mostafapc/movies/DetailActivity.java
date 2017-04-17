package com.example.mostafapc.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    int poster_path = 0;
    int movie_title = 1;
    int movie_overview = 3;
    int movie_release_date = 4;
    int movie_vote = 6;

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVote;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = (ImageView) findViewById(R.id.details_movie_poster);
        mTitle = (TextView) findViewById(R.id.details_title_view);
        mReleaseDate = (TextView) findViewById(R.id.details_release_date);
        mVote = (TextView) findViewById(R.id.details_vote_view);
        mOverview = (TextView) findViewById(R.id.details_overview);

        String [] movieItemDetails ;

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            movieItemDetails = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

            Picasso.with(this).load(movieItemDetails[poster_path]).into(mPoster);
            mTitle.setText(movieItemDetails[movie_title]);
            mReleaseDate.setText(movieItemDetails[movie_release_date]);
            mVote.setText(movieItemDetails[movie_vote]);
            mOverview.setText(movieItemDetails[movie_overview]);

        }
        else {
            Toast.makeText(this,"unable to fetch movie data" , Toast.LENGTH_LONG).show();
        }
    }
}
