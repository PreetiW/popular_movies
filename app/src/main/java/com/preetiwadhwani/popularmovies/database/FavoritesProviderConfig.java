package com.preetiwadhwani.popularmovies.database;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by Preeti on 27-03-2016.
 */


@SimpleSQLConfig(
        name = "FavoriteProvider",
        authority = "com.preetiwadhwani.popularmovies",
        database = MoviesDBHelper.DATABASE_NAME,
        version = MoviesDBHelper.DATABASE_VERSION)

public class FavoritesProviderConfig implements ProviderConfig
{
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}

