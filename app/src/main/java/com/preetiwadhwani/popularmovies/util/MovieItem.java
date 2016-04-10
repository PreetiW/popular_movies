package com.preetiwadhwani.popularmovies.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.preetiwadhwani.popularmovies.database.MoviesContract;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Preeti on 14-02-2016.
 */

@SimpleSQLTable(table = MoviesContract.FavoritesEntry.TABLE_NAME, provider = "FavoriteProvider")
public class MovieItem implements Parcelable {

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_MOVIETITLE)
    String movieName;

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_POSTERURL)
    String movieImageUrl;

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_MOVIEID)
    String movieId;

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_BACKDROP)
    String backdropMovieImageUrl;

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_REALEASEDATE)
    String movieReleaseDate;

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_SYNOPSIS)
    String movieSummary;

    @SimpleSQLColumn(MoviesContract.FavoritesEntry.COLUMN_RATING)
    String movieRating;

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
