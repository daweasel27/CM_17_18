package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.jp.projectofinal.MovieSuggestion;
import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.SaveToFile;
import com.example.jp.projectofinal.dataModels.MoviesSuggestionInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ImageView one = null;
    private ImageView favsImage;
    private ImageView profileImage;

    public static MoviesSuggestionInfo mv;
    public static SaveToFile sv;
    public static MovieSuggestion ms;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");


        mv = new MoviesSuggestionInfo();
        mv.initializeMovies();
        sv = new SaveToFile();
        ms = new MovieSuggestion(getBaseContext());

        one = (ImageView)findViewById(R.id.imageViewStart);
        favsImage = (ImageView) findViewById(R.id.imageViewFavorites);
        profileImage = (ImageView) findViewById(R.id.imageViewProfiles);


        one.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Camera.class);
            startActivity(intent);
        }});


        favsImage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
            Intent intentFavs = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intentFavs);
        }});

        profileImage.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
            Intent intentFavs = new Intent(MainActivity.this, MovieEmotionalProfileActivity.class);
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
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
