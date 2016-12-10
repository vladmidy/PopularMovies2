package com.ciscoedge.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {

    // The constructor to create an instance of the class.
    public Movie(){
    }

    // This is the overloaded constructor.
    public Movie(String movieId,
                 String title,
                 String image_location,
                 String overview,
                 String user_rating,
                 String release_date,
                 String popular,
                 String favorite){
        this.movieId = movieId;
        this.title = title;
        this.image_location = image_location;
        this.overview= overview;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.popular = popular;
        this.favorite =  favorite;
    }

     public String movieId;
     public String title;
     public String image_location;
     public String overview;
     public String user_rating;
     public String release_date;
     public String popular;
     public String favorite;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.image_location);
        dest.writeString(this.overview);
        dest.writeString(this.user_rating);
        dest.writeString(this.release_date);
        dest.writeString(this.popular);
        dest.writeString(this.favorite);
    }

    protected Movie(Parcel in) {
        this.movieId = in.readString();
        this.title = in.readString();
        this.image_location = in.readString();
        this.overview = in.readString();
        this.user_rating = in.readString();
        this.release_date = in.readString();
        this.popular = in.readString();
        this.favorite = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
