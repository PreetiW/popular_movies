package com.preetiwadhwani.popularmovies.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.preetiwadhwani.popularmovies.BuildConfig;
import com.preetiwadhwani.popularmovies.R;
import com.preetiwadhwani.popularmovies.util.GridRecylerAdapter;
import com.preetiwadhwani.popularmovies.util.MovieItem;
import com.preetiwadhwani.popularmovies.util.Util;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, GridRecylerAdapter.GridItemClickHandler
{

    @Bind(R.id.toolbar) Toolbar appToolbar;
    @Bind(R.id.movies_grid) RecyclerView movieGridRecycleView;
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.sort_spinner) Spinner sortMovieSpinner;
    @Bind(R.id.snackbar_position) CoordinatorLayout snackbarPosition;

    GridRecylerAdapter moviesGridAdapter;
    ArrayList<MovieItem> moviesData = new ArrayList<>();
    String sortOrder = "popularity.desc";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

    }

    private void initUI()
    {
        ButterKnife.bind(this);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sortMovieSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_movie_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortMovieSpinner.setAdapter(sortAdapter);

        movieGridRecycleView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count)));
        moviesGridAdapter = new GridRecylerAdapter(this, moviesData);
        movieGridRecycleView.setAdapter(moviesGridAdapter);
        moviesGridAdapter.setOnGridItemClickListener(this);

        fetchMovieData(sortOrder, this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        movieGridRecycleView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_count)));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String sortOption = parent.getItemAtPosition(position).toString();
        if(sortOption.equalsIgnoreCase("By Popularity"))
        {
            sortOrder = "popularity.desc";
            fetchMovieData(sortOrder, this);
        }
        else if(sortOption.equalsIgnoreCase("By Rating"))
        {
            sortOrder = "vote_average.desc";
            fetchMovieData(sortOrder, this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onGridItemClick(MovieItem selectedMovieItem)
    {
        Intent movieDetailIntent =  new Intent(MainActivity.this, MovieDetailActivity.class);
        movieDetailIntent.putExtra("selectedMovieItem", selectedMovieItem);
        startActivity(movieDetailIntent);

    }

    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieItem>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            movieGridRecycleView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.GONE);
            if(movieItemArrayList != null)
            {
                moviesData.clear();
                moviesData.addAll(movieItemArrayList);
                moviesGridAdapter.notifyDataSetChanged();
                movieGridRecycleView.setVisibility(View.VISIBLE);
            }
        }
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

    public void fetchMovieData(String sortBy, Context context)
    {
        if(Util.isOnline(context))
        {
            new FetchMoviesTask().execute(sortBy);
        }
        else
        {
            movieGridRecycleView.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(snackbarPosition,
                            getResources().getString(R.string.no_internet_connection),
                            Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(Color.RED)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fetchMovieData(sortOrder, MainActivity.this);
                                }
                    });

            snackbar.show();
        }
    }
}