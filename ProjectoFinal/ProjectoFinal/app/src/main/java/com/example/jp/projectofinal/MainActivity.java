package com.example.jp.projectofinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
