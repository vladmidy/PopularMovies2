package com.ciscoedge.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ciscoedge.popularmovies.Movie;
import com.ciscoedge.popularmovies.Review;
import com.ciscoedge.popularmovies.Utility;
import com.ciscoedge.popularmovies.Video;
import com.ciscoedge.popularmovies.data.MovieContract.MovieEntry;
import com.ciscoedge.popularmovies.data.MovieContract.ReviewEntry;
import com.ciscoedge.popularmovies.data.MovieContract.VideoEntry;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by tpq on 11/15/16.
 */

public class MovieDataSource {

    public static final String LOGTAG = "MOVIE_APP";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context context;


    public MovieDataSource(Context context){
        dbhelper = new MovieDbHelper(context);
        this.context = context;
    }


    public SQLiteDatabase open(){
        Log.i(LOGTAG, "Database opened");
        return dbhelper.getWritableDatabase();
    }


    private String getSortOrder(){
    String preferredSortOrder = Utility.getPreferredSortOrder(context);

    switch (preferredSortOrder) {
        case "popular":
            String newPreferredSortOrder = "popularity";
            return newPreferredSortOrder;
        default:
            return preferredSortOrder;
    }
}


    private static final String[] movieColumns = {
            MovieEntry._ID,
            MovieEntry.COLUMN_ORIGINAL_TILE,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_TOP_RATED,
            MovieEntry.COLUMN_POPULAR,
            MovieEntry.COLUMN_FAVORITE
    };


    private static String[] videoColumns = {
            VideoEntry._ID,
            VideoEntry.COLUMN_MOVIE_ID,
            VideoEntry.COLUMN_VIDEO_KEY,
            VideoEntry.COLUMN_SITE,
            VideoEntry.COLUMN_VIDEO_TYPE
    };


    private static String[] reviewColumns  = {
            ReviewEntry._ID,
            ReviewEntry.COLUMN_MOVIE_ID,
            ReviewEntry.COLUMN_AUTHOR,
            ReviewEntry.COLUMN_CONTENT
    };


    //Movie database activity area.
    public List<Movie> getAllMovies(){

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Movie> movies = new ArrayList<Movie>();

        Cursor cursor = db.query(MovieEntry.TABLE_NAME,
                        movieColumns,
                        null,
                        null,
                        null,
                        null,
                        getSortOrder() +" DESC" );

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Movie movie = new Movie();

                movie.movieId = Integer.toString(cursor.getInt(cursor.getColumnIndex(MovieEntry._ID)));
                movie.title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TILE));
                movie.overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                movie.image_location = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
                movie.release_date = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
                movie.user_rating = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TOP_RATED));
                movie.popular = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POPULAR));
                movie.favorite = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_FAVORITE));

                movies.add(movie);
            }
        }
        db.close();
        return movies;
    }


    //Movie database activity area.
    public List<Movie> getFavoriteMovies(String favorite){

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Movie> movies = new ArrayList<Movie>();


        String where = MovieEntry.COLUMN_FAVORITE + "= ?";
        String[] whereArgs = {favorite};

        Cursor cursor = db.query(MovieEntry.TABLE_NAME,
                movieColumns,
                where,
                whereArgs,
                null,
                null,
                getSortOrder() +" DESC" );

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Movie movie = new Movie();

                movie.movieId = Integer.toString(cursor.getInt(cursor.getColumnIndex(MovieEntry._ID)));
                movie.title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TILE));
                movie.overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                movie.image_location = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
                movie.release_date = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
                movie.user_rating = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TOP_RATED));
                movie.popular = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POPULAR));
                movie.favorite = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_FAVORITE));

                movies.add(movie);
            }
        }
        db.close();
        return movies;
    }


    public void insertMovie(Movie movie) {

        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {

            values.put(MovieEntry._ID, movie.movieId );
            values.put(MovieEntry.COLUMN_ORIGINAL_TILE, movie.title );
            values.put(MovieEntry.COLUMN_POSTER_PATH, movie.image_location );
            values.put(MovieEntry.COLUMN_OVERVIEW, movie.overview );
            values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.release_date );
            values.put(MovieEntry.COLUMN_TOP_RATED, movie.user_rating );
            values.put(MovieEntry.COLUMN_POPULAR, movie.popular );
            db.insert(MovieEntry.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
        }finally {
            db.endTransaction();
        }

        db.close();

    }


    public void bulkInsertMovie(List<Movie> list) {

        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        try {

            for (Movie movie : list) {
                values.put(MovieEntry._ID, Integer.parseInt(movie.movieId) );
                values.put(MovieEntry.COLUMN_ORIGINAL_TILE, movie.title );
                values.put(MovieEntry.COLUMN_POSTER_PATH, movie.image_location );
                values.put(MovieEntry.COLUMN_OVERVIEW, movie.overview );
                values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.release_date );
                values.put(MovieEntry.COLUMN_TOP_RATED, movie.user_rating );
                values.put(MovieEntry.COLUMN_POPULAR, movie.popular );
                db.insert(MovieEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
        }finally {
            db.endTransaction();
        }
        db.close();
    }


    public int updateMovie(Movie movie){

        int rowCount = -1;
        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {

            values.put(MovieEntry._ID, movie.movieId );
            values.put(MovieEntry.COLUMN_ORIGINAL_TILE, movie.title );
            values.put(MovieEntry.COLUMN_POSTER_PATH, movie.image_location );
            values.put(MovieEntry.COLUMN_OVERVIEW, movie.overview );
            values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.release_date );
            values.put(MovieEntry.COLUMN_TOP_RATED, movie.user_rating );
            values.put(MovieEntry.COLUMN_POPULAR, movie.popular );
            values.put(MovieEntry.COLUMN_FAVORITE, movie.favorite);

            String where = MovieEntry._ID + "= ?";
            String[] whereArgs = {String.valueOf(movie.movieId)};

            rowCount = db.update(MovieEntry.TABLE_NAME, values, where, whereArgs);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error updating " + values, e);
        }finally {
            db.endTransaction();
        }

        db.close();

        return rowCount;
    }


    // Video database activity area.
    public List<Video> getAllVideos(){

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Video> videos = new ArrayList<Video>();

        Cursor cursor = db.query(VideoEntry.TABLE_NAME,
                videoColumns,
                null,
                null,
                null,
                null,
                null );

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Video video = new Video();

                video.videoId = Integer.toString(cursor.getInt(cursor.getColumnIndex(VideoEntry._ID)));
                video.movieId = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_MOVIE_ID));
                video.video_key = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_VIDEO_KEY));
                video.site = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_SITE));
                video.type = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_VIDEO_TYPE));

                videos.add(video);
            }
        }
        db.close();
        return videos;
    }


    // Video database activity area.
    public List<Video> getVideo(String movieId){

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Video> videos = new ArrayList<Video>();

        String trailer = "Trailer";

        String where = VideoEntry.COLUMN_MOVIE_ID + " = ? AND " +VideoEntry.COLUMN_VIDEO_TYPE + " = ?" ;
        String[] whereArgs = {movieId, trailer};

        Cursor cursor = db.query(VideoEntry.TABLE_NAME,
                videoColumns,
                where,
                whereArgs,
                null,
                null,
                null );



        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Video video = new Video();

                video.videoId = Integer.toString(cursor.getInt(cursor.getColumnIndex(VideoEntry._ID)));
                video.movieId = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_MOVIE_ID));
                video.video_key = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_VIDEO_KEY));
                video.site = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_SITE));
                video.type = cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_VIDEO_TYPE));

                videos.add(video);
            }
        }
        db.close();
        return videos;
    }


    public void insertVideo(Video video) {

        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {
            values.put(VideoEntry._ID, video.videoId );
            values.put(VideoEntry.COLUMN_MOVIE_ID, video.videoId );
            values.put(VideoEntry.COLUMN_VIDEO_KEY, video.video_key );
            values.put(VideoEntry.COLUMN_SITE, video.site );
            values.put(VideoEntry.COLUMN_VIDEO_TYPE, video.type );

            db.insert(VideoEntry.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
        }finally {
            db.endTransaction();
        }

        db.close();

    }


    public void bulkInsertVideo(List<Video> list) {

        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        try {

            for (Video video : list) {
                values.put(VideoEntry.COLUMN_MOVIE_ID, Integer.parseInt(video.movieId));
                values.put(VideoEntry.COLUMN_VIDEO_KEY, video.video_key );
                values.put(VideoEntry.COLUMN_SITE, video.site );
                values.put(VideoEntry.COLUMN_VIDEO_TYPE, video.type );

                db.insert(VideoEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
        }finally {
            db.endTransaction();
        }
        db.close();
    }


    public int updateVideo(Video video){

        int rowCount = -1;
        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {

            values.put(VideoEntry._ID, video.videoId);
            values.put(VideoEntry.COLUMN_MOVIE_ID, video.movieId );
            values.put(VideoEntry.COLUMN_VIDEO_KEY, video.video_key );
            values.put(VideoEntry.COLUMN_SITE, video.site);
            values.put(VideoEntry.COLUMN_VIDEO_TYPE, video.type );

            String where = VideoEntry._ID + "= ?";
            String[] whereArgs = {String.valueOf(video.movieId)};

            rowCount = db.update(VideoEntry.TABLE_NAME, values, where, whereArgs);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error updating " + values, e);
        }finally {
            db.endTransaction();
        }

        db.close();

        return rowCount;
    }


    // Review database activity area.
    public List<Review> getAllReviews(){

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Review> reviews = new ArrayList<>();

        Cursor cursor = db.query(ReviewEntry.TABLE_NAME,
                reviewColumns,
                null,
                null,
                null,
                null,
                null );

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Review review = new Review();

                review.reviewId = Integer.toString(cursor.getInt(cursor.getColumnIndex(ReviewEntry._ID)));
                review.movieId = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_MOVIE_ID));
                review.author = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR));
                review.content = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT));

                reviews.add(review);
            }
        }
        db.close();
        return reviews;
    }


    // Review database activity area.
    public List<Review> getReview(String movieId){

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Review> reviews = new ArrayList<Review>();

        String where = ReviewEntry.COLUMN_MOVIE_ID + "= ?";
        String[] whereArgs = {movieId};

        Cursor cursor = db.query(ReviewEntry.TABLE_NAME,
                reviewColumns,
                where,
                whereArgs,
                null,
                null,
                null );

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Review review = new Review();

                review.reviewId = Integer.toString(cursor.getInt(cursor.getColumnIndex(ReviewEntry._ID)));
                review.movieId = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_MOVIE_ID));
                review.author = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR));
                review.content = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT));

                reviews.add(review);
            }
        }
        db.close();
        return reviews;
    }


    public void insertReview(Review review) {

        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {
            values.put(ReviewEntry._ID, review.reviewId );
            values.put(ReviewEntry.COLUMN_MOVIE_ID, review.reviewId );
            values.put(ReviewEntry.COLUMN_AUTHOR, review.author );
            values.put(ReviewEntry.COLUMN_CONTENT, review.content );

            db.insert(ReviewEntry.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
        }finally {
            db.endTransaction();
        }

        db.close();

    }


    public void bulkInsertReview(List<Review> list) {

        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        try {

            for (Review review : list) {
                values.put(ReviewEntry.COLUMN_MOVIE_ID, Integer.parseInt(review.movieId));
                values.put(ReviewEntry.COLUMN_AUTHOR, review.author );
                values.put(ReviewEntry.COLUMN_CONTENT, review.content );

                db.insert(ReviewEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
        }finally {
            db.endTransaction();
        }
        db.close();
    }


    public int updateReview(Review review){

        int rowCount = -1;
        SQLiteDatabase db = this.open();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {

            values.put(ReviewEntry._ID, review.reviewId);
            values.put(ReviewEntry.COLUMN_MOVIE_ID, review.movieId );
            values.put(ReviewEntry.COLUMN_AUTHOR, review.author );
            values.put(ReviewEntry.COLUMN_CONTENT, review.content);

            String where = ReviewEntry._ID + "= ?";
            String[] whereArgs = {String.valueOf(review.movieId)};

            rowCount = db.update(ReviewEntry.TABLE_NAME, values, where, whereArgs);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error updating " + values, e);
        }finally {
            db.endTransaction();
        }

        db.close();

        return rowCount;
    }


    public void close(){
        Log.i(LOGTAG, "Database closed");
        dbhelper.close();
    }


    public boolean isFavoriteData(String Table){

    SQLiteDatabase db = this.open();

    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Table + " WHERE " + MovieEntry.COLUMN_FAVORITE + "='true'", null);

    int count;



    if (cursor != null) {

        cursor.moveToFirst();
        count= cursor.getInt(0);
        db.close();
        if (count == 0) {        //always return at least one row
                                 //if the count is zero it is empty
            return false;
        } else if (count > 0) {

            return true;
        }
    }

    return false;


}


}
