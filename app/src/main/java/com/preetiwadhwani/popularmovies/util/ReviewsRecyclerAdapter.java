package com.preetiwadhwani.popularmovies.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.preetiwadhwani.popularmovies.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Preeti on 10-04-2016.
 */
public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ViewHolder>
{
    public Context context;
    private ArrayList<ReviewItem> reviewDataSource;

    public ReviewsRecyclerAdapter(ArrayList<ReviewItem> trailerDataSourceParam, Context contextParam)
    {
        reviewDataSource = trailerDataSourceParam;
        context = contextParam;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.bindView(position);
    }


    @Override
    public int getItemCount()
    {
        return reviewDataSource.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.review_content)
        TextView reviewContent;
        @Bind(R.id.reviewer_name) TextView reviewerName;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int position)
        {
            reviewContent.setText(reviewDataSource.get(position).getReviewContent());
            reviewerName.setText(reviewDataSource.get(position).getReviewerName());
        }

    }

}
