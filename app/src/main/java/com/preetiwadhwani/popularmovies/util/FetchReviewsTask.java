package com.preetiwadhwani.popularmovies.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.preetiwadhwani.popularmovies.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Preeti on 10-04-2016.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<ReviewItem>>
{
    ReviewsAsyncListener reviewsAsyncListener;
    Context context;

    public FetchReviewsTask(Context context)
    {
        this.context = context;
    }

    public void setReviewsAsyncListener(ReviewsAsyncListener reviewsAsyncListener)
    {
        this.reviewsAsyncListener = reviewsAsyncListener;
    }

    @Override
    protected void onPreExecute()
    {
        reviewsAsyncListener.onReviewsPreExecute();
    }

    @Override
    public ArrayList<ReviewItem> doInBackground(String... params)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJsonStr = null;
        if (params.length == 0)
        {
            return null;
        }
        try
        {
            String TRAILERS_BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
            final String APIKEY_PARAM = "api_key";
            final String API_KEY = BuildConfig.TMDB_API_KEY;

            Uri builtUri = Uri.parse(TRAILERS_BASE_URL).buildUpon().appendQueryParameter(APIKEY_PARAM, API_KEY).build();
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
            {
                reviewJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                reviewJsonStr = null;
            }
            reviewJsonStr = buffer.toString();

            try
            {
                return getReviewDatafromJson(reviewJsonStr);
            }
            catch (JSONException jsonException)
            {
                jsonException.printStackTrace();
            }
        }
        catch (IOException e)
        {
            reviewJsonStr = null;
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (final IOException e)
                {
                    Log.e("Popular Movies", "Error closing stream ", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewItem> receivedReviewsData)
    {
        reviewsAsyncListener.onReviewsPostExecute(receivedReviewsData);
    }

    private ArrayList<ReviewItem> getReviewDatafromJson(String reviewDataJsonstr) throws JSONException
    {
        final String TMDB_REVIEWS_RESULTS = "results";
        final String TMDB_REVIEWS_AUTHOR = "author";
        final String TMDB_REVIEWS_CONTENT = "content";

        ArrayList<ReviewItem> reviewItemsList = new ArrayList<>();

        JSONObject reviewsJson = new JSONObject(reviewDataJsonstr);
        JSONArray resultsArray = reviewsJson.getJSONArray(TMDB_REVIEWS_RESULTS);

        for (int i = 0; i < resultsArray.length(); i++)
        {
            JSONObject reviewItemJson = resultsArray.getJSONObject(i);
            ReviewItem reviewItem = new ReviewItem();

            reviewItem.setReviewContent(reviewItemJson.getString(TMDB_REVIEWS_CONTENT));
            reviewItem.setReviewerName(reviewItemJson.getString(TMDB_REVIEWS_AUTHOR));

            reviewItemsList.add(reviewItem);
        }

        return reviewItemsList;

    }


}
