package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.MovieInfo;
import com.example.jp.projectofinal.fragments.MovieInfoFragment;

/**
 * Created by Jota on 10/22/2017.
 */

public class MovieInfoActivity extends AppCompatActivity {
    String list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_info);

        Intent intent = getIntent();
        list = intent.getStringExtra("arg");
        Log.e("filme", list);
        getFragment(savedInstanceState);
    }

    private void getFragment(Bundle savedInstanceState){
        if (findViewById(R.id.layout_suggestions_list_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            MovieInfoFragment dailyListFragment = new MovieInfoFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_suggestions_list_fragment, dailyListFragment).commit();
        }
    }


}


