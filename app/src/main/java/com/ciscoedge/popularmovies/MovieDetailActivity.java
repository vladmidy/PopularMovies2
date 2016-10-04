package com.ciscoedge.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;




public class MovieDetailActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // If the orientation is Landscape, we don't want to finish the task of
        // creating  the view from here.
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {

            // Therefore, we call the finish() method, which
            // simply states " I'm finish here with this activity and
            // it should be closed. It sends control back to whatever
            // called this activity.
            finish();
        }
        setContentView(R.layout.movie_activity_detail);
        if (savedInstanceState == null) {

            // Here we create a new MovieDetailFragment.
            MovieDetailFragment details = new MovieDetailFragment();

            // we set our bunble of key value pair into it from the calling
            // party.
            details.setArguments(getIntent().getExtras());

            // we add the fragment into the activity state.
            getFragmentManager().beginTransaction()
                    .add(R.id.container, details)
                    .commit();


        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MovieDetailActivity.this, MovieAppSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

