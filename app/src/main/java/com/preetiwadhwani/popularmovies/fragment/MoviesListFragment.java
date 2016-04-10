package com.preetiwadhwani.popularmovies.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.preetiwadhwani.popularmovies.R;
import com.preetiwadhwani.popularmovies.util.FavoritemoviesTable;
import com.preetiwadhwani.popularmovies.util.FetchMovieTask;
import com.preetiwadhwani.popularmovies.util.GridRecylerAdapter;
import com.preetiwadhwani.popularmovies.util.MovieItem;
import com.preetiwadhwani.popularmovies.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesListFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, GridRecylerAdapter.GridItemClickHandler,
        FetchMovieTask.FetchMoviesAsyncListener, FragmentManager.OnBackStackChangedListener
{

    AppCompatActivity activity;
    @Bind(R.id.toolbar)
    Toolbar appToolbar;

    @Bind(R.id.movies_grid)
    RecyclerView movieGridRecycleView;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.sort_spinner)
    Spinner sortMovieSpinner;

    @Bind(R.id.snackbar_position)
    CoordinatorLayout snackbarPosition;

    GridRecylerAdapter moviesGridAdapter;
    ArrayList<MovieItem> moviesData = new ArrayList<>();
    String sortOrder = "popularity.desc";
    Snackbar snackbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this,view);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(appToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        sortMovieSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(activity,
                R.array.sort_movie_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortMovieSpinner.setAdapter(sortAdapter);

        movieGridRecycleView.setLayoutManager(new GridLayoutManager(activity, getResources().getInteger(R.integer.grid_column_count)));
        moviesGridAdapter = new GridRecylerAdapter(activity, moviesData);
        movieGridRecycleView.setAdapter(moviesGridAdapter);
        moviesGridAdapter.setOnGridItemClickListener(this);

        fetchMovieData(sortOrder, activity);

        return view;
    }


    @Override
    public void onGridItemClick(MovieItem selectedMovieItem)
    {
        MovieDetailFragment movieDetailsFragment = new MovieDetailFragment();
        Bundle movieItemBundle = new Bundle();


        if(activity.getResources().getBoolean(R.bool.is_tablet))
        {
            movieItemBundle.putParcelable("selectedMovieItem", selectedMovieItem);
            movieItemBundle.putBoolean("isTablet", true);
            movieDetailsFragment.setArguments(movieItemBundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, movieDetailsFragment).commit();
        }
        else
        {
            movieItemBundle.putParcelable("selectedMovieItem", selectedMovieItem);
            movieItemBundle.putBoolean("isTablet", false);
            movieDetailsFragment.setArguments(movieItemBundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.parent_fragment_container, movieDetailsFragment)
                    .addToBackStack("detail")
                    .commit();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String sortOption = parent.getItemAtPosition(position).toString();
        if(sortOption.equalsIgnoreCase("By Popularity"))
        {
            sortOrder = "popularity.desc";
            fetchMovieData(sortOrder, activity);
        }
        else if(sortOption.equalsIgnoreCase("By Rating"))
        {
            sortOrder = "vote_average.desc";
            fetchMovieData(sortOrder, activity);
        }
        else if(sortOption.equalsIgnoreCase("Favorite Movies"))
        {

            Cursor cursor =  activity.getContentResolver().query(FavoritemoviesTable.CONTENT_URI, null, null, null, null);
            List<MovieItem> favoriteMoviesList = FavoritemoviesTable.getRows(cursor, true);

            ArrayList<MovieItem> favoriteMoviesArrayList = new ArrayList<>(favoriteMoviesList);

            moviesData.clear();
            moviesData.addAll(favoriteMoviesArrayList);
            moviesGridAdapter.notifyDataSetChanged();
            movieGridRecycleView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fetchMovieData(String sortBy, Context context)
    {
        if(Util.isOnline(context))
        {
            new FetchMovieTask(this).execute(sortBy);
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
                            fetchMovieData(sortOrder, activity);
                        }
                    });

            snackbar.show();
        }
    }

    @Override
    public void onPreExicute()
    {
        movieGridRecycleView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(ArrayList<MovieItem> receivedMoviesList)
    {
        progressBar.setVisibility(View.GONE);
        if(receivedMoviesList != null)
        {
            moviesData.clear();
            moviesData.addAll(receivedMoviesList);
            moviesGridAdapter.notifyDataSetChanged();
            movieGridRecycleView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackStackChanged()
    {
        if(activity.getSupportFragmentManager().getBackStackEntryCount() == 0 && sortMovieSpinner.getSelectedItemPosition() == 2)
        {
            List<MovieItem> favoriteMoviesAsList = FavoritemoviesTable.getRows(activity.getContentResolver().query(FavoritemoviesTable.CONTENT_URI, null, null, null, null), true);
            ArrayList<MovieItem> favoriteMovies = new ArrayList<MovieItem>(favoriteMoviesAsList);
            moviesData.clear();
            moviesData.addAll(favoriteMovies);
            moviesGridAdapter.notifyDataSetChanged();
            movieGridRecycleView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            //failureContainer.setVisibility(View.GONE);
        }
    }
}
