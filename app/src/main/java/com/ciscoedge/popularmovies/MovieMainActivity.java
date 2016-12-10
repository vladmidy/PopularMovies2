package com.ciscoedge.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.ciscoedge.popularmovies.data.MovieDbHelper;

import java.lang.reflect.Field;

public class MovieMainActivity extends Activity {

    SQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity_main);

        /**
         * BEGIN: HACK
         *
         * This hack forces the App to show the overflow menu on devices with a menu button.

         * source: StockOverflow
         * Link: http://stackoverflow.com/questions/9286822/how-to-force-use-of-overflow-menu-on-devices-with-menu-button
         *
         */

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {
        }
        //END: HACK

        dbHelper = new MovieDbHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    startActivity(new Intent(MovieMainActivity.this, MovieAppSettingsActivity.class));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
}

