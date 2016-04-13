package com.preetiwadhwani.popularmovies.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.preetiwadhwani.popularmovies.R;
import com.preetiwadhwani.popularmovies.database.MoviesContract;
import com.preetiwadhwani.popularmovies.util.FavoritemoviesTable;
import com.preetiwadhwani.popularmovies.util.FetchReviewsTask;
import com.preetiwadhwani.popularmovies.util.FetchTrailersTask;
import com.preetiwadhwani.popularmovies.util.MovieItem;
import com.preetiwadhwani.popularmovies.util.ReviewItem;
import com.preetiwadhwani.popularmovies.util.ReviewsAsyncListener;
import com.preetiwadhwani.popularmovies.util.ReviewsRecyclerAdapter;
import com.preetiwadhwani.popularmovies.util.TrailerItem;
import com.preetiwadhwani.popularmovies.util.TrailersAsyncListener;
import com.preetiwadhwani.popularmovies.util.TrailersRecyclerAdapter;
import com.preetiwadhwani.popularmovies.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.deanwild.flowtextview.FlowTextView;

public class MovieDetailFragment extends Fragment implements TrailersAsyncListener, ReviewsAsyncListener
{

    @Bind(R.id.detail_toolbar) Toolbar detailToolbar;
    @Bind(R.id.backdrop_image) ImageView backdropMovieImage;
    @Bind(R.id.movie_poster) ImageView moviePoster;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_summary) FlowTextView movieSummary;
    @Bind(R.id.movie_rating) AppCompatRatingBar movieRating;
    @Bind(R.id.favorite_fab) FloatingActionButton favoriteFAB;
    @Bind(R.id.trailers_container)
    RecyclerView trailersContainer;
    @Bind(R.id.reviews_container) RecyclerView reviewsContainer;

    TrailersRecyclerAdapter trailersRecyclerAdapter;
    ReviewsRecyclerAdapter reviewsRecyclerAdapter;
    ArrayList<TrailerItem> trailersData;
    ArrayList<ReviewItem> reviewsData;

    MovieItem selectedMovieItem;
    AppCompatActivity activity;
    Cursor cursor;
    boolean isFavorite;

    static final String TRAILERS_DATA_KEY = "trailersData";
    static final String REVIEWS_DATA_KEY = "reviewsData";
    static final String SELECTED_MOVIE_DATA_KEY = "selectedMovieData";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this,view);
        activity = (AppCompatActivity) getActivity();

        setUI(savedInstanceState);
        return view;

    }

    private void setUI(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            selectedMovieItem = savedInstanceState.getParcelable(SELECTED_MOVIE_DATA_KEY);
            reviewsData = savedInstanceState.getParcelableArrayList(REVIEWS_DATA_KEY);
            trailersData = savedInstanceState.getParcelableArrayList(TRAILERS_DATA_KEY);
        }
        else
        {

            reviewsData = new ArrayList<>();
            trailersData = new ArrayList<>();
            selectedMovieItem = getArguments().getParcelable("selectedMovieItem");

            FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(getActivity());
            fetchTrailersTask.setTrailersAsyncListener(this);
            fetchTrailersTask.execute(selectedMovieItem.getMovieId());

            FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(getActivity());
            fetchReviewsTask.setReviewsAsyncListener(this);
            fetchReviewsTask.execute(selectedMovieItem.getMovieId());
        }
        if(!getArguments().getBoolean("isTablet"))
        {
            activity.setSupportActionBar(detailToolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        activity.getSupportActionBar().setTitle(selectedMovieItem.getMovieName());
        movieReleaseDate.setText(selectedMovieItem.getMovieReleaseDate());
        movieSummary.setText(selectedMovieItem.getMovieSummary());
        movieSummary.setTextSize(48);
        movieRating.setProgress((int)Float.parseFloat(selectedMovieItem.getMovieRating()));


        Picasso.with(activity)
                .load(selectedMovieItem.getMovieImageUrl())
                .placeholder(R.drawable.grid_item_image_placeholder)
                .into(moviePoster);

        Picasso.with(activity)
                .load(selectedMovieItem.getBackdropMovieImageUrl())
                .into(backdropMovieImage);

        // Setup trailers
        trailersContainer.setHasFixedSize(false);
        trailersContainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        trailersRecyclerAdapter = new TrailersRecyclerAdapter(trailersData, getActivity());
        trailersContainer.setAdapter(trailersRecyclerAdapter);

        // Setup reviews
        reviewsContainer.setHasFixedSize(false);
        reviewsContainer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviewsData, getActivity());
        reviewsContainer.setAdapter(reviewsRecyclerAdapter);


        try
        {


            Cursor cursor = getActivity().getContentResolver().query(FavoritemoviesTable.CONTENT_URI,
                    null, MoviesContract.FavoritesEntry.COLUMN_MOVIEID + " = " + selectedMovieItem.getMovieId(), null, null);

            if (cursor.getCount() !=0)
            {
                favoriteFAB.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_favorite_white_24dp));
                isFavorite = true;
            }
            else
            {
                favoriteFAB.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_favorite_border_white_24dp));
                isFavorite = false;

            }
        }
        catch (Exception e)
        {
            Util.handleException("MovieDetailFragment", "setUI", e);
        }
        finally
        {
            if(cursor != null)
                cursor.close();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(SELECTED_MOVIE_DATA_KEY, selectedMovieItem);
        outState.putParcelableArrayList(TRAILERS_DATA_KEY, trailersData);
        outState.putParcelableArrayList(REVIEWS_DATA_KEY, reviewsData);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.favorite_fab)
    void setFavoriteMovie()
    {
        if(!isFavorite)
        {
            favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white_24dp));
            //DB INSERT STATEMENT
            insertDataIntoFavoriteMoviesTable();
            isFavorite = true;
        }
        else if(isFavorite)
        {
            // DB DELETE STATEMENT
            if(deleteDataFromFavoriteMoviesTable() !=0)
                favoriteFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_border_white_24dp));
                isFavorite = false;
        }
    }

    private int deleteDataFromFavoriteMoviesTable()
    {

        // Defines selection criteria for the rows you want to delete
        String mSelectionClause =  MoviesContract.FavoritesEntry.COLUMN_MOVIEID + " LIKE ?";
        String[] mSelectionArgs = {selectedMovieItem.getMovieId()};

        // Defines a variable to contain the number of rows deleted
        int mRowsDeleted = 0;


        // Deletes the words that match the selection criteria
        mRowsDeleted = activity.getContentResolver().delete(
                FavoritemoviesTable.CONTENT_URI,   // the user dictionary content URI
                mSelectionClause,                    // the column to select on
                mSelectionArgs                      // the value to compare to
        );

        return mRowsDeleted;
    }

    private void insertDataIntoFavoriteMoviesTable()
    {
        Uri newUri;


        ContentValues mNewValues = new ContentValues();
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIEID, selectedMovieItem.getMovieId());
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIETITLE, selectedMovieItem.getMovieName());
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_POSTERURL, selectedMovieItem.getMovieImageUrl());
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_SYNOPSIS, selectedMovieItem.getMovieSummary());
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_RATING, selectedMovieItem.getMovieRating());
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_REALEASEDATE, selectedMovieItem.getMovieReleaseDate());
        mNewValues.put(MoviesContract.FavoritesEntry.COLUMN_BACKDROP, selectedMovieItem.getBackdropMovieImageUrl());

        newUri = activity.getContentResolver().insert(FavoritemoviesTable.CONTENT_URI, mNewValues);

    }


    @Override
    public void onTrailersPreExecute()
    {

    }

    @Override
    public void onReviewsPreExecute()
    {

    }

    @Override
    public void onTrailersPostExecute(ArrayList<TrailerItem> receivedTrailersData)
    {
        if (receivedTrailersData != null)
        {
            trailersData.clear();
            trailersData.addAll(receivedTrailersData);
            trailersRecyclerAdapter.notifyDataSetChanged();
            setHasOptionsMenu(true);
        }
        else
        {

        }
    }

    @Override
    public void onReviewsPostExecute(ArrayList<ReviewItem> receivedReviewsData)
    {
        if (receivedReviewsData != null)
        {
            reviewsData.clear();
            reviewsData.addAll(receivedReviewsData);
            reviewsRecyclerAdapter.notifyDataSetChanged();
            Log.i("pmovies", "onReviewssPostExecute: ");
        }
        else
        {

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, trailersData.get(0).getTrailerUrl() + " Shared from Popular Movies Enjoy!!");
        myShareActionProvider.setShareIntent(myShareIntent);
    }

}
