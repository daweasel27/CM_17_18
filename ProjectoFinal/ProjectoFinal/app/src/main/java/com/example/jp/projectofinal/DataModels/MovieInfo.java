package com.example.jp.projectofinal.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jota on 10/22/2017.
 */

public class MovieInfo  implements Parcelable {
    private String title;
    private String id;
    private String backdrop_path;
    private String overview;
    private String release_date;
    private String vote_average;

    public MovieInfo(String title, String id, String backdrop_path, String overview, String release_date, String vote_average) {
        this.title = title;
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    public MovieInfo (Parcel in){
        this.title = in.readString();
        this.id = in.readString();
        this.backdrop_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.vote_average = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(vote_average);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
