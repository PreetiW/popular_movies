package com.preetiwadhwani.popularmovies.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Preeti on 10-04-2016.
 */
public class ReviewItem implements Parcelable
{
    String reviewerName;
    String reviewContent;

    public String getReviewerName()
    {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
    }

    public String getReviewContent()
    {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent)
    {
        this.reviewContent = reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewerName);
        dest.writeString(this.reviewContent);
    }

    public ReviewItem() {
    }

    protected ReviewItem(Parcel in) {
        this.reviewerName = in.readString();
        this.reviewContent = in.readString();
    }

    public static final Parcelable.Creator<ReviewItem> CREATOR = new Parcelable.Creator<ReviewItem>() {
        public ReviewItem createFromParcel(Parcel source) {
            return new ReviewItem(source);
        }

        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };
}
