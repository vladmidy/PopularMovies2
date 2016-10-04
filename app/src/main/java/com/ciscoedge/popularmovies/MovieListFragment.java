package com.ciscoedge.popularmovies;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class MovieListFragment extends Fragment
implements AdapterView.OnItemClickListener {


    boolean isLandscape;
    int currentPosition = 0;
    GridView g;


    MovieArrayAdapter myMovieAdapter;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        // Populate list
        myMovieAdapter =
                new MovieArrayAdapter(getActivity(),
                        R.layout.fragment_grid_view, new ArrayList<Movie>());



         g.setAdapter(myMovieAdapter);
         g.setOnItemClickListener(this);


        // To find out if the device is in LandScape mode, the codes below
        // check whether both Detail Frame exist (not null) and that it is
        // visible on the User interface.
        View detailsFrame = getActivity().findViewById(R.id.details);
        isLandscape = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {

            // If we have an Instance saved, this code retrieves
            // our current position on the grid.
            currentPosition = savedInstanceState.getInt("itemSelected", 0);

        }


        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        g = (GridView) inflater.inflate(R.layout.fragment_grid_view, container, false);

        return g;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // When a grid item is clicked, this code calls the displayDetails method,
        // and pass in the current index position of this grid item.

        displayDetails(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // This code saves our current item selected (position on the grid)
        // in a key/value pair.
        outState.putInt("itemSelected", currentPosition);

        super.onSaveInstanceState(outState);
    }





    public void displayDetails(int index) {

        // This takes the current index location of the grid
        // from the index parameter passed into the method.
        currentPosition = index;
        Movie movieTransferData = myMovieAdapter.getItem(index);
        if (isLandscape) {


            // If we are in Landscape mode, we don't need to call "Intent" to display
            // our detail activity. Both List Fragment and Detail Fragment can fit in
            // one screen.


            // This code retrieves the current detail fragment;
            MovieDetailFragment details = (MovieDetailFragment)
                    getFragmentManager().findFragmentById(R.id.details);

            // but if the detail fragment does not exist or we got
            // a detail fragment that is not at the expected index
            // position, we recreate the detail fragment and pass in
            // the position from the method parameter.





            if (details == null || movieTransferData.title != details.getMovie().title) {

                // This code calls in the createFragment method,
                // which takes in one parameter, the list index location
                // and returns the MovieDetailFragment.
                details = MovieDetailFragment.createFragment(movieTransferData);

                // Similar to a database transaction, it begins the process of adding
                // our detail view to the the main activity.
                FragmentTransaction myFragmentTransaction = getFragmentManager().beginTransaction();

                // Replace any detail fragment already added with our newly created one
                myFragmentTransaction.replace(R.id.details, details);

                //This code lets the Fragment simply fade in or out (no strong navigation)
                myFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                //Finally, we commit our fragment onto our layout or activity;
                // hence becoming part of it.
                myFragmentTransaction.commit();
            }

        } else {


            // if it is not in landscape mode, we create a new intent
            Intent intent = new Intent();

            // sets the class we want to call to create that activity
            intent.setClass(getActivity(), MovieDetailActivity.class);

            // tells intent that we have some extra data we want to pass
            // in this case, it is a data parcel.

            intent.putExtra("movie_parcel", movieTransferData);

            // starts the detail activity via intent.
            startActivity(intent);
        }
    }


    public void updateMovie(){


        GetMovieTask movieTask = new GetMovieTask();


        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_popular));




        movieTask.execute(sort_by);

    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovie();


    }


    public class GetMovieTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = GetMovieTask.class.getSimpleName();


        private List<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {


            final String MOVIE_RESULTS = "results";
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_OVERVIEW = "overview";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_RELEASE_DATE ="release_date";
            final String MOVIE_RATING = "vote_average";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(MOVIE_RESULTS);

            Movie[] movies = new Movie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {

                String title;
                String poster_path;
                String overview;
                String release_date;
                String rating;



                JSONObject movieJSObject = movieArray.getJSONObject(i);


                title = movieJSObject.getString(MOVIE_TITLE);
                poster_path = movieJSObject.getString(MOVIE_POSTER);
                overview = movieJSObject.getString(MOVIE_OVERVIEW);
                release_date = movieJSObject.getString(MOVIE_RELEASE_DATE);
                rating = Double.toString(movieJSObject.getDouble(MOVIE_RATING));

                movies[i] = new Movie(title,
                        poster_path,
                        overview,
                        rating,
                        release_date);


            }

            for (Movie s : movies) {
                Log.v(LOG_TAG, "Movie entry: " + s.title);
            }

            List<Movie> movieList = Arrays.asList(movies);
            return  movieList;

        }


        @Override
        protected List<Movie> doInBackground(String... params) {

            // If it is not being sorted by popularity or
            // rating, we choose not to move further, it goes
            // against project requirement.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;


            try {
                // Construct the URL for the MovieDB query

                final String MOVIE_BASE_URL = " https://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to MovieDB.org, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    //exit from the method
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // exit from the method.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the Movie data, there's no point in attemping
                // to parse it.

                // we exit out of the method.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                myMovieAdapter.clear();

                // This code populate our adapter with new data.
                for (Movie movieStr : result) {
                    myMovieAdapter.add(movieStr);
                }
            }

            if (isLandscape) {


                // the code below in turn call displayDetails and pass in
                // our current position on the list.
                displayDetails(currentPosition);
            }


        }
    }
}

