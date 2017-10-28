package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.EmotionValues;
import com.example.jp.projectofinal.dataModels.MovieInfo;
import com.example.jp.projectofinal.dataModels.ToFirebase;
import com.example.jp.projectofinal.fragments.MovieProfileFragment;
import com.example.jp.projectofinal.fragments.SuggestionListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jota on 10/28/2017.
 */

public class MovieEmotionalProfileActivity extends AppCompatActivity {


    private void getFragment(Bundle savedInstanceState){
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.layout_movieprofile_list_fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MovieProfileFragment dailyListFragment = new MovieProfileFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_movieprofile_list_fragment, dailyListFragment).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movierofile_list);

        getFragment(savedInstanceState);
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_profile);

        getData();

        myAdapter = new SuggestionListFragment.MyAdapter(
                getActivity(),
                new ArrayList<String>());

        List<MovieInfo> list2 = list.subList(0,10);
        myAdapter.clear();
        for (MovieInfo movie : list2 ) {
            myAdapter.add(movie.getId()+":"+movie.getTitle()+":"+movie.getVote_average()+":"+movie.getRelease_date()+ ":" +movie.getBackdrop_path());
        }

    }*/


}

