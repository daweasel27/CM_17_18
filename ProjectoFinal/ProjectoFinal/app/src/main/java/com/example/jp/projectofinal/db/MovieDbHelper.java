package com.example.jp.projectofinal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TiagoHenriques on 17/10/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                // the ID of the location entry associated with this movie data
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_DIRECTOR + " TEXT NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_STARS + " TEXT NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_LENGTH + " INTEGER NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_GENRE + " TEXT NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_STORY_LINE + " TEXT NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +

                MovieContract.MovieEntry.COLUMN_THUMB + " TEXT NOT NULL);";

        // To assure the application have just one weather entry per day
        // per location, it's created a UNIQUE constraint with REPLACE strategy
        //" UNIQUE (" + MovieContract.MovieEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}