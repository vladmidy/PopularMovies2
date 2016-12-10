package com.ciscoedge.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable {

    // The constructor to create an instance of the class.
    public Review(){
    }

    // This is the overloaded constructor.
    public Review(String reviewId,
                 String movieId,
                 String author,
                 String content) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }


    public String reviewId;
    public String movieId;
    public String author;
    public String content;


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewId);
        dest.writeString(this.movieId);
        dest.writeString(this.author);
        dest.writeString(this.content);
    }


    protected Review(Parcel in) {
        this.reviewId = in.readString();
        this.movieId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
    }


    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }


        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
