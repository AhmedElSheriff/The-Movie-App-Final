package com.example.android.themovieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abshafi on 7/27/2016.
 */
public class MoviesData implements Parcelable{

    MoviesData()
    {


    }

    private String title,rate,release,overview,poster_path,id;


    protected MoviesData(Parcel in) {
        title = in.readString();
        rate = in.readString();
        release = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        id = in.readString();
    }

    public static final Creator<MoviesData> CREATOR = new Creator<MoviesData>() {
        @Override
        public MoviesData createFromParcel(Parcel in) {
            return new MoviesData(in);
        }

        @Override
        public MoviesData[] newArray(int size) {
            return new MoviesData[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path =  "http://image.tmdb.org/t/p/w185" + poster_path;
    }



    public void setId(String id) {
        this.id = id;
    }

    ////////////////////////////////////////////////////

    public String getTitle() {
        return title;
    }

    public String getRate() {
        return rate;
    }

    public String getRelease() {
        return release;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }


    public String getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(rate);
        dest.writeString(release);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(id);
    }
}
