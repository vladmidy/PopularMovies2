package com.ciscoedge.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class Video implements Parcelable {

    // The constructor to create an instance of the class.
    public Video(){
    }


    // This is the overloaded constructor.
    public Video(String videoId,
                 String movieId,
                 String video_Key,
                 String site,
                 String type) {
        this.videoId = videoId;
        this.movieId = movieId;
        this.video_key = video_Key;
        this.site = site;
        this.type = type;

    }

    public String videoId;
    public String movieId;
    public String video_key;
    public String site;
    public String type;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoId);
        dest.writeString(this.movieId);
        dest.writeString(this.video_key);
        dest.writeString(this.site);
        dest.writeString(this.type);

    }

    protected Video(Parcel in) {
        this.videoId = in.readString();
        this.movieId = in.readString();
        this.video_key = in.readString();
        this.site = in.readString();
        this.type = in.readString();

    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
