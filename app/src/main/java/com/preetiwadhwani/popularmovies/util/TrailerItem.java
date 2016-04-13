package com.preetiwadhwani.popularmovies.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Preeti on 10-04-2016.
 */
public class TrailerItem implements Parcelable
{
    String trailerThumnbailUrl;
    String trailerUrl;

    public String getTrailerThumnbailUrl()
    {
        return trailerThumnbailUrl;
    }

    public void setTrailerThumnbailUrl(String trailerThumnbailUrl)
    {
        this.trailerThumnbailUrl = trailerThumnbailUrl;
    }

    public String getTrailerUrl()
    {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl)
    {
        this.trailerUrl = trailerUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trailerThumnbailUrl);
        dest.writeString(this.trailerUrl);
    }

    public TrailerItem() {
    }

    protected TrailerItem(Parcel in) {
        this.trailerThumnbailUrl = in.readString();
        this.trailerUrl = in.readString();
    }

    public static final Parcelable.Creator<TrailerItem> CREATOR = new Parcelable.Creator<TrailerItem>() {
        public TrailerItem createFromParcel(Parcel source) {
            return new TrailerItem(source);
        }

        public TrailerItem[] newArray(int size) {
            return new TrailerItem[size];
        }
    };
}
