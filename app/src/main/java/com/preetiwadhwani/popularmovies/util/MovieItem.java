package com.preetiwadhwani.popularmovies.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Preeti on 14-02-2016.
 */
public class MovieItem implements Parcelable {

    String movieName, movieImageUrl, movieId, backdropMovieImageUrl, movieReleaseDate, movieSummary, movieRating;

    public String getBackdropMovieImageUrl()
    {
        return backdropMovieImageUrl;
    }

    public String getMovieRating()
    {
        return movieRating;
    }

    public void setMovieRating(String movieRating)
    {
        this.movieRating = movieRating;
    }

    public void setBackdropMovieImageUrl(String backdropMovieImageUrl)

    {
        this.backdropMovieImageUrl = backdropMovieImageUrl;
    }

    public String getMovieReleaseDate()
    {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate)
    {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieSummary()
    {
        return movieSummary;
    }

    public void setMovieSummary(String movieSummary)
    {
        this.movieSummary = movieSummary;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId)
    {
        this.movieId = movieId;
    }

    public String getMovieImageUrl()
    {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl)
    {
        this.movieImageUrl = movieImageUrl;
    }

    public String getMovieName()
    {
        return movieName;
    }

    public void setMovieName(String movieName)
    {
        this.movieName = movieName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.movieName);
        dest.writeString(this.movieImageUrl);
        dest.writeString(this.movieId);
        dest.writeString(this.backdropMovieImageUrl);
        dest.writeString(this.movieReleaseDate);
        dest.writeString(this.movieSummary);
        dest.writeString(this.movieRating);
    }

    public MovieItem()
    {
    }

    protected MovieItem(Parcel in)
    {
        this.movieName = in.readString();
        this.movieImageUrl = in.readString();
        this.movieId = in.readString();
        this.backdropMovieImageUrl = in.readString();
        this.movieReleaseDate = in.readString();
        this.movieSummary = in.readString();
        this.movieRating = in.readString();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>()
    {
        public MovieItem createFromParcel(Parcel source)
        {
            return new MovieItem(source);
        }

        public MovieItem[] newArray(int size)
        {
            return new MovieItem[size];
        }
    };
}
