package com.ciscoedge.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {

    // The constructor to create an instance of the class.
    public Movie(){
    }
    // This is the overloaded constructor.
    public Movie(String title,
                 String image_location,
                 String overview,
                 String user_rating,
                 String release_date){
        this.title = title;
        this.image_location = image_location;
        this.overview = overview;
        this.user_rating = user_rating;
        this.release_date = release_date;
    }

     String title;
     String image_location;
     String overview;
     String user_rating;
     String release_date;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.image_location);
        dest.writeString(this.overview);
        dest.writeString(this.user_rating);
        dest.writeString(this.release_date);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.image_location = in.readString();
        this.overview = in.readString();
        this.user_rating = in.readString();
        this.release_date = in.readString();
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
