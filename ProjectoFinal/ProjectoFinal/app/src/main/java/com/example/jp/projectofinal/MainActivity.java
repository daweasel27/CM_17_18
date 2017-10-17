package com.example.jp.projectofinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView one = null;
    private ImageView favsImage;
    private ImageView suggestionsImage;

    private static final String LOG_TAG = "LOG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        one = (ImageView)findViewById(R.id.imageViewStart);
        suggestionsImage = (ImageView) findViewById(R.id.imageViewSuggestions);
        favsImage = (ImageView) findViewById(R.id.imageViewFavorites);


        one.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Camera.class);
                startActivity(intent);
            }});

        suggestionsImage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                Intent intentFavs = new Intent(MainActivity.this, SuggestionActivity.class);
                startActivity(intentFavs);
            }});

        favsImage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                Intent intentFavs = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intentFavs);
            }});

        // Just for test
        //addTestDB();
        returnValuesTestDB("\"The Recruit\"");
    }

    public void returnValuesTestDB(String s){
        MovieDbHelper dbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String column = MovieContract.MovieEntry.COLUMN_TITLE;
        String selection = column+" like "+s;

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                selection, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {
            do {
                int columnTitle = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnTitle));
                int columnYear = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnYear));
                int columnDesc = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnDesc));
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();
    }

    public void addTestDB(){
        MovieDbHelper dbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Recruit");
        testValues.put(MovieContract.MovieEntry.COLUMN_YEAR, 2003);
        testValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, 115);
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, 6.6);
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "Action, Adventure");
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,  "A brilliant young CIA trainee is asked by his mentor to help find a mole in the Agency.");
        testValues.put(MovieContract.MovieEntry.COLUMN_THUMB, "https://images-na.ssl-images-amazon.com/images/M/MV5BMjE5MDMzOTk3MV5BMl5BanBnXkFtZTYwNTE0NTg2._V1_UX182_CR0,0,182,268_AL_.jpg");


        long locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        if (locationRowId == -1) {
            Log.i(LOG_TAG, "Failed to insert row !");
        }

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {
            do {
                int columnIndex =
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnIndex));
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.view_home_page:
                setContentView(R.layout.home_page);
                return true;
            case R.id.view_suggestion_list:
                setContentView(R.layout.suggestion_list);
                return true;
            case R.id.view_movies_suggestions:
                setContentView(R.layout.movie_suggestion);
                return true;
            case R.id.view_trailer:
                setContentView(R.layout.trailer_watch);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
