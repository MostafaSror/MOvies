package com.example.mostafapc.movies;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class DetailActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {

            Bundle detailsargs = new Bundle();
            detailsargs.putStringArray("edttext", getIntent().getStringArrayExtra(Intent.EXTRA_TEXT));

            FragmentDetail detailsFragment = new FragmentDetail();
            detailsFragment.setArguments(detailsargs);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, detailsFragment)
                    .commit();
        }
    }


}
