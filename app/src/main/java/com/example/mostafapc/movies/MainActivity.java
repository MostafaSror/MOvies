package com.example.mostafapc.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentMain.Callback  {

    private static final String SORT_TYPE_PREF_FILE = "sort_type_file";
    private static final String SORT_TYPE_PREF_KEY = "sort_type_key";
    private String mSortOrder ;
    private boolean mTwoPane;

    SharedPreferences sharedPref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences(SORT_TYPE_PREF_FILE, MODE_PRIVATE);
        mSortOrder = sharedPref.getString(SORT_TYPE_PREF_KEY, "popular");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SORT_TYPE_PREF_KEY, mSortOrder);
        editor.commit();

        if (findViewById(R.id.details_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_container, new FragmentDetail())
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String SortOrder = sharedPref.getString(SORT_TYPE_PREF_KEY, "popular");
        // update the location in our second pane using the fragment manager
        if (SortOrder != null && !SortOrder.equals(mSortOrder)) {
            FragmentMain ff = (FragmentMain) getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != ff ) {
                ff.onSortOrderChanged(SortOrder);
            }
            mSortOrder = SortOrder;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentMain ff = (FragmentMain) getSupportFragmentManager().findFragmentById(R.id.fragment);
        SharedPreferences.Editor editor = sharedPref.edit();

        switch (id) {
            case R.id.action_popular:{
                editor.putString(SORT_TYPE_PREF_KEY, "popular");
                editor.commit();

                ff.onSortOrderChanged("popular");
                return true;
            }
            case R.id.action_rated:{
                editor.putString(SORT_TYPE_PREF_KEY, "top_rated");
                editor.commit();

                ff.onSortOrderChanged("top_rated");
                return true;
            }
            case R.id.action_fav:{
                editor.putString(SORT_TYPE_PREF_KEY, "favourite");
                editor.commit();

                ff.onSortOrderChanged("favourite");
                return true;
            }
            default:{
                Toast.makeText(this, R.string.error_choose_sort, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPosterSelected(String [] MovieContent) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle detailsargs = new Bundle();
            detailsargs.putStringArray("edttext", getIntent().getStringArrayExtra(Intent.EXTRA_TEXT));

            FragmentDetail detailsFragment = new FragmentDetail();
            detailsFragment.setArguments(detailsargs);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, detailsFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, MovieContent);
            startActivity(intent);
        }
    }
}
