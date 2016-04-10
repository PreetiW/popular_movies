package com.preetiwadhwani.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.preetiwadhwani.popularmovies.R;
import com.preetiwadhwani.popularmovies.fragment.MoviesListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{

    @Bind(R.id.parent_fragment_container) FrameLayout parentFragmentContainer;
    MoviesListFragment moviesListFragment;
    public static String IS_TABLET_KEY = "IS_TABLET_KEY";

    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI(savedInstanceState);

    }

    private void initUI(Bundle savedInstanceState)
    {
        ButterKnife.bind(this);

        moviesListFragment = new MoviesListFragment();

        if(savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.parent_fragment_container, moviesListFragment).commit();
        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            getSupportFragmentManager().popBackStackImmediate();
        }
        return super.onOptionsItemSelected(item);
    }





}