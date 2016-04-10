package com.preetiwadhwani.popularmovies.util;

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
 * Created by Preeti on 03-04-2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieItem>>
{
    FetchMoviesAsyncListener fetchMoviesAsyncListener;

    public FetchMovieTask(FetchMoviesAsyncListener fetchMoviesAsyncListener)
    {
        this.fetchMoviesAsyncListener = fetchMoviesAsyncListener;

    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        fetchMoviesAsyncListener.onPreExicute();
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try
        {

            String MOVIESDB_BASE_URL = null;
            final String APIKEY_PARAM = "api_key";
            final String API_KEY = BuildConfig.TMDB_API_KEY;


            if(params[0].equals("popularity.desc"))
            {
                MOVIESDB_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
            }
            else
            {
                MOVIESDB_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
            }

            Uri builtUri = Uri.parse(MOVIESDB_BASE_URL).buildUpon().appendQueryParameter(APIKEY_PARAM, API_KEY).build();

            URL url = new URL(builtUri.toString());


            // Create the request to theMovieDB api, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
            {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                //No point in parsing if the response is empty
                return null;
            }
            movieJsonStr = buffer.toString();

            return getMoviesDataFromJson(movieJsonStr);
        }
        catch (Exception e)
        {
            Log.e("Main Activity", "Error ", e);
            return null;
        }
        finally
        {
            if (urlConnection != null) {
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
                    Log.e("Main Activity", "Error ", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> movieItemArrayList)
    {
        super.onPostExecute(movieItemArrayList);
        fetchMoviesAsyncListener.onPostExecute(movieItemArrayList);
    }

    private ArrayList<MovieItem> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException
    {

        final String TMDB_RESULTS = "results";
        final String TMDB_TITLE = "title";
        final String TMDB_POSTER = "poster_path";
        final String TMDB_ID = "id";
        final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w342/";
        final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w780/";
        ArrayList<MovieItem> movieItemsList = new ArrayList<>();

        JSONObject forecastJson = new JSONObject(moviesJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(TMDB_RESULTS);

        for (int i = 0; i < weatherArray.length(); i++)
        {
            JSONObject movieItemJson = weatherArray.getJSONObject(i);
            MovieItem movieItem = new MovieItem();
            movieItem.setMovieName(movieItemJson.getString(TMDB_TITLE));
            movieItem.setMovieImageUrl(IMG_BASE_URL + movieItemJson.getString(TMDB_POSTER));
            movieItem.setMovieId(movieItemJson.getString(TMDB_ID));
            movieItem.setBackdropMovieImageUrl(BACKDROP_BASE_URL + movieItemJson.getString("backdrop_path"));
            movieItem.setMovieReleaseDate(movieItemJson.getString("release_date"));
            movieItem.setMovieSummary(movieItemJson.getString("overview"));
            movieItem.setMovieRating(movieItemJson.getString("vote_average"));

            movieItemsList.add(movieItem);
        }

        return movieItemsList;

    }


    public interface FetchMoviesAsyncListener
    {
        void onPreExicute();
        void onPostExecute(ArrayList<MovieItem> receivedMoviesList);
    }


}

