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
public class FetchTrailersTask  extends AsyncTask<String, Void, ArrayList<TrailerItem>>
{
    TrailersAsyncListener trailersAsyncListener;
    Context context;

    public FetchTrailersTask(Context context)
    {
        this.context = context;
    }

    public void setTrailersAsyncListener(TrailersAsyncListener trailersAsyncListener)
    {
        this.trailersAsyncListener = trailersAsyncListener;
    }

    @Override
    protected void onPreExecute()
    {
        trailersAsyncListener.onTrailersPreExecute();
    }

    @Override
    public ArrayList<TrailerItem> doInBackground(String... params)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailerJsonStr = null;
        if (params.length == 0)
        {
            return null;
        }
        try
        {
            String TRAILERS_BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
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
                trailerJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                trailerJsonStr = null;
            }
            trailerJsonStr = buffer.toString();

            try
            {
                return getTrailerDatafromJson(trailerJsonStr);
            }
            catch (JSONException jsonException)
            {
                jsonException.printStackTrace();
            }
        }
        catch (IOException e)
        {
            trailerJsonStr = null;
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
    protected void onPostExecute(ArrayList<TrailerItem> receivedTrailersData)
    {
        trailersAsyncListener.onTrailersPostExecute(receivedTrailersData);
    }

    private ArrayList<TrailerItem> getTrailerDatafromJson(String trailerDataJsonstr) throws JSONException
    {
        final String TMDB_TRAILERS_RESULTS = "results";
        final String TMDB_TRAILERS_KEY = "key";

        final String TRAILER_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";
        final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

        ArrayList<TrailerItem> trailerItemsList = new ArrayList<>();

        JSONObject trailersJson = new JSONObject(trailerDataJsonstr);
        JSONArray resultsArray = trailersJson.getJSONArray(TMDB_TRAILERS_RESULTS);

        for (int i = 0; i < resultsArray.length(); i++)
        {
            JSONObject trailerItemJson = resultsArray.getJSONObject(i);
            TrailerItem trailerItem = new TrailerItem();

            trailerItem.setTrailerThumnbailUrl(TRAILER_THUMBNAIL_BASE_URL + trailerItemJson.getString(TMDB_TRAILERS_KEY) + "/0.jpg");
            trailerItem.setTrailerUrl(TRAILER_BASE_URL + trailerItemJson.getString(TMDB_TRAILERS_KEY));

            trailerItemsList.add(trailerItem);
        }

        return trailerItemsList;

    }


}