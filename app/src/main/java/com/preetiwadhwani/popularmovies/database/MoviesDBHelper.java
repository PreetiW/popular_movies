package com.preetiwadhwani.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.preetiwadhwani.popularmovies.database.MoviesContract.*;

/**
 * Created by Preeti on 27-03-2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper
{
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favoritemovies.db";

    MoviesDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME +
                " (" + FavoritesEntry.COLUMN_MOVIEID + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_REALEASEDATE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_MOVIETITLE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_POSTERURL + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_RATING + " TEXT NOT NULL" + ");" ;



        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
