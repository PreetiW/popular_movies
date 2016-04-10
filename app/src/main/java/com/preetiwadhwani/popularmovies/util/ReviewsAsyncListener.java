package com.preetiwadhwani.popularmovies.util;

import java.util.ArrayList;

/**
 * Created by Preeti on 10-04-2016.
 */
public interface ReviewsAsyncListener
{
    void onReviewsPreExecute();
    void onReviewsPostExecute(ArrayList<ReviewItem> receivedTrailersData);
}
