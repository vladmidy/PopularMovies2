package com.ciscoedge.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.ciscoedge.popularmovies.data.MovieDataSource;
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


public class AppTask extends AsyncTask<String, Void, List<Object>> {



    public Context mContext;
    MovieDataSource myMovieDataSource;
    private OnLoadDataListener mListener;
    public void setListener(OnLoadDataListener listener){ mListener = listener;}


    public AppTask(Context context) {
        super();
        this.mContext = context;
    }

    public AppTask(){
        super();
    }

    private final String LOG_TAG = com.ciscoedge.popularmovies.AppTask.class.getSimpleName();

    private List<Object> getDataFromJson(String JsonStr, String _id, String dataType)
            throws JSONException {

        List<Object> objectList = null;

        if (dataType.equals("videos")) {

            final String VIDEO_ID = "id";
            final String VIDEO_RESULTS = "results";
            final String VIDEO_KEY = "key";
            final String VIDEO_SITE = "site";
            final String VIDEO_TYPE = "type";


            JSONObject videoJson = new JSONObject(JsonStr);
            JSONArray videoArray = videoJson.getJSONArray(VIDEO_RESULTS);

            Video[] videos = new Video[videoArray.length()];

            for (int i = 0; i < videoArray.length(); i++) {

                String video_id;
                String movie_id = _id;
                String video_key;
                String site;
                String type;


                JSONObject videoJSObject = videoArray.getJSONObject(i);

                video_id = videoJSObject.getString(VIDEO_ID);
                video_key = videoJSObject.getString(VIDEO_KEY);
                site = videoJSObject.getString(VIDEO_SITE);
                type = videoJSObject.getString(VIDEO_TYPE);

                videos[i] = new Video(video_id,
                        movie_id,
                        video_key,
                        site,
                        type);

            }

            for (Video v : videos) {
                Log.v(LOG_TAG, "Video entry: " + v.videoId);
            }

            List<Video> videoList = Arrays.asList(videos);
            myMovieDataSource.bulkInsertVideo(videoList);
            objectList = new ArrayList<Object>(videoList);

            return objectList;
        }else if (dataType.equals("reviews")){


            final String REVIEW_RESULTS = "results";
            final String REVIEW_ID = "id";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";


            JSONObject reviewJson = new JSONObject(JsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray(REVIEW_RESULTS);

            Review[] reviews = new Review[reviewArray.length()];

            for (int i = 0; i < reviewArray.length(); i++) {

                String review_id;
                String movie_id = _id;
                String review_author;
                String review_content;


                JSONObject videoJSObject = reviewArray.getJSONObject(i);

                review_id = videoJSObject.getString(REVIEW_ID);
                review_author = videoJSObject.getString(REVIEW_AUTHOR);
                review_content = videoJSObject.getString(REVIEW_CONTENT);

                reviews[i] = new Review(review_id,
                        movie_id,
                        review_author,
                        review_content);
            }

            for (Review r : reviews) {
                Log.v(LOG_TAG, "Review entry: " + r.reviewId);
            }

            List<Review> reviewList = Arrays.asList(reviews);
            myMovieDataSource.bulkInsertReview(reviewList);
            objectList = new ArrayList<Object>(reviewList);


            return objectList;

        }else{

            return objectList;
        }
    }


    private List<Object> getDataFromDatabase(String movieId, String dataType){

        // we create the source of our data
        myMovieDataSource = new MovieDataSource(this.mContext);

        if (dataType.equals("videos")) {

            // then we try to get the data from the database
            List<Video> videoList = myMovieDataSource.getVideo(movieId);
            List<Object> objectList = new ArrayList<Object>(videoList);

            return objectList;
        }else if (dataType.equals("reviews")){

            // then we try to get the data from the database
            List<Review> reviewList = myMovieDataSource.getReview(movieId);
            List<Object> objectList = new ArrayList<Object>(reviewList);

            return objectList;
        }else{

            return null;
        }
    }


    @Override
    protected List<Object> doInBackground(String... params) {


        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        String movieId = params[0];
        String dataType = params[1];

        List<Object> objectList = getDataFromDatabase(movieId, dataType);

        // If it is not being sorted by popularity or
        // rating, we choose not to move further, it goes
        // against project requirement.
        if (params.length == 0) {
            return null;
        }

        // we fist is there is data in the database
        if (objectList.size() == 0) {
            // if not we check the network if there is a connection.
            if(isConnected){

                // These two need to be declared outside the try/catch
                // so that they can be closed in the finally block.
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String JsonStr = null;

                final String MOVIE_BASE_URL = " https://api.themoviedb.org/3/movie/";
                final String MOVIE_ID = params[0];
                final String TYPE_OF_DATA = params[1];
                final String API_KEY_PARAM = "api_key";

                // Construct the URL for the MovieDB query
                try {

                    Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                            .appendPath(MOVIE_ID)
                            .appendPath(TYPE_OF_DATA)
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
                    JsonStr = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error:  " + e.toString(), e);
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
                    return getDataFromJson(JsonStr, MOVIE_ID, TYPE_OF_DATA);
                    // (Old version) -- return getMovieDataFromJson(movieJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }else{

                return null;
            }

        } else {

            return objectList;
        }
    }


    @Override
    protected void onPostExecute(List<Object> result) {

        mListener.onLoadComplete(result);
    }

}