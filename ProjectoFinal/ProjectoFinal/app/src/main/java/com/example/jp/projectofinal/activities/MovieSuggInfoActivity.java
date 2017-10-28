package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.MovieInfo;
import com.example.jp.projectofinal.fragments.MovieInfoFragment;

/**
 * Created by Jota on 10/22/2017.
 */

public class MovieSuggInfoActivity extends AppCompatActivity {
    MovieInfo list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_suggestion_details);

        Intent intent = getIntent();
        list = (MovieInfo) intent.getSerializableExtra("arg_key");

        getFragment(savedInstanceState);
    }

    private void getFragment(Bundle savedInstanceState){
        if (findViewById(R.id.layout_container_movie_details_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            MovieInfoFragment dailyListFragment = new MovieInfoFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_container_movie_details_fragment, dailyListFragment).commit();
        }
    }


}


