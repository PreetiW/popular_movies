package com.preetiwadhwani.popularmovies.util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.preetiwadhwani.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Preeti on 15-02-2016.
 */
public class GridRecylerAdapter extends RecyclerView.Adapter<GridRecylerAdapter.ViewHolder>
{
    Context context;
    ArrayList<MovieItem> movieItemArrayList;
    GridItemClickHandler gridItemClickHandler;


    public GridRecylerAdapter(Context context, ArrayList<MovieItem> movieItemArrayList)
    {
        this.context = context;
        this.movieItemArrayList = movieItemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.gridItemLabel.setText(movieItemArrayList.get(position).getMovieName());
        Picasso.with(context)
                .load(movieItemArrayList.get(position).getMovieImageUrl())
                .placeholder(R.drawable.grid_item_image_placeholder)
                .into(holder.gridItemImage);
    }

    @Override
    public int getItemCount()
    {
        return movieItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.grid_item_container) CardView gridItemContainer;
        @Bind(R.id.grid_item_image) ImageView gridItemImage;
        @Bind(R.id.grid_item_title) TextView gridItemLabel;


        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.grid_item_container)
        public void gridItemClicked()
        {
            int itemPosition = getAdapterPosition();
            gridItemClickHandler.onGridItemClick(movieItemArrayList.get(itemPosition));
        }
    }

    public interface GridItemClickHandler
    {
        void onGridItemClick(MovieItem selectedMovieItem);
    }

    public void setOnGridItemClickListener(GridItemClickHandler gridItemClickHandler)
    {
        this.gridItemClickHandler = gridItemClickHandler;
    }

}
