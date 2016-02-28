package com.preetiwadhwani.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.preetiwadhwani.popularmovies.R;
import com.preetiwadhwani.popularmovies.util.MovieItem;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.deanwild.flowtextview.FlowTextView;

public class MovieDetailActivity extends AppCompatActivity
{

    @Bind(R.id.detail_toolbar) Toolbar detailToolbar;
    @Bind(R.id.backdrop_image) ImageView backdropMovieImage;
    @Bind(R.id.movie_poster) ImageView moviePoster;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_summary) FlowTextView movieSummary;
    @Bind(R.id.movie_rating) AppCompatRatingBar movieRating;

    MovieItem movieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(getIntent().getExtras() != null)
        {
            movieItem = getIntent().getParcelableExtra("selectedMovieItem");
        }
        setUI();

    }

    private void setUI()
    {
        ButterKnife.bind(this);
        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(movieItem.getMovieName());
        movieReleaseDate.setText(movieItem.getMovieReleaseDate());
        movieSummary.setText(movieItem.getMovieSummary());
        movieSummary.setTextSize(48);
        movieRating.setProgress((int)Float.parseFloat(movieItem.getMovieRating()));


        Picasso.with(this)
                .load(movieItem.getMovieImageUrl())
                .placeholder(R.drawable.grid_item_image_placeholder)
                .into(moviePoster);

        Picasso.with(this)
                .load(movieItem.getBackdropMovieImageUrl())
                .into(backdropMovieImage);

    }

}
