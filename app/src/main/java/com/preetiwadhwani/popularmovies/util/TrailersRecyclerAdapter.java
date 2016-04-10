package com.preetiwadhwani.popularmovies.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.preetiwadhwani.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Preeti on 10-04-2016.
 */
public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.ViewHolder>
{
    public Context context;
    private ArrayList<TrailerItem> trailerDataSource;

    public TrailersRecyclerAdapter(ArrayList<TrailerItem> trailerDataSourceParam, Context contextParam)
    {
        trailerDataSource = trailerDataSourceParam;
        context = contextParam;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
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
        return trailerDataSource.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.trailer_item_image)
        ImageView trailerItemImage;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int position)
        {
            Picasso.with(context).load(trailerDataSource.get(position).getTrailerThumnbailUrl()).placeholder(R.drawable.grid_item_image_placeholder).into(trailerItemImage);
        }

        @OnClick(R.id.trailer_item_container)
        public void trailerItemClick()
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerDataSource.get(getAdapterPosition()).getTrailerUrl())));
        }

    }

}