package com.ciscoedge.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by tpq on 11/1/16.
 */

public class MovieContract {

    // Inner class that defines the table contents of the movie table
    public static final class MovieEntry implements BaseColumns {

        // The table name
        public static final String TABLE_NAME = "movie";

        // Note: The movie id we received from the moviedb
        // will be entered in the auto-generated _id
        // column.


        public static final String COLUMN_ORIGINAL_TILE = "original_title";


        public static final String COLUMN_POSTER_PATH = "poster_path";


        public static final String COLUMN_OVERVIEW = "site";


        public static final String COLUMN_RELEASE_DATE = "release_date";


        public static final String COLUMN_TOP_RATED = "top_rated";


        public static final String COLUMN_POPULAR = "popularity";


        public static final String COLUMN_FAVORITE = "favorite";


    }


    // Inner class that defines the table contents of the movie table
    public static final class VideoEntry implements BaseColumns {

        // The table name
        public static final String TABLE_NAME = "video";

        // Note: The movie id we received from the moviedb
        // will be entered in the auto-generated _id
        // column.

        public static final String COLUMN_MOVIE_ID = "movie_id";


        public static final String COLUMN_VIDEO_KEY = "video_key";


        public static final String COLUMN_SITE = "site";


        public static final String COLUMN_VIDEO_TYPE = "video_type";

    }


    // Inner class that defines the table contents of the movie table
    public static final class ReviewEntry implements BaseColumns {

        // The table name
        public static final String TABLE_NAME = "review";

        // Note: The movie id we received from the moviedb
        // will be entered in the auto-generated _id
        // column.

        public static final String COLUMN_MOVIE_ID = "movie_id";


        public static final String COLUMN_AUTHOR = "author";


        public static final String COLUMN_CONTENT = "content";

    }

}
