package com.preetiwadhwani.popularmovies.database;

import android.provider.BaseColumns;

/**
 * Created by Preeti on 27-03-2016.
 */
public class MoviesContract
{

    public static final class FavoritesEntry implements BaseColumns
    {
        // not good to use capitals
        public static final String TABLE_NAME = "favoritemovies";

        public static final String COLUMN_MOVIEID = "movie_id";

        public static final String COLUMN_MOVIETITLE = "movie_title";

        public static final String COLUMN_POSTERURL = "poster_url";

        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_REALEASEDATE = "release_date";

        public static final String COLUMN_BACKDROP = "back_drop";


    }
}
