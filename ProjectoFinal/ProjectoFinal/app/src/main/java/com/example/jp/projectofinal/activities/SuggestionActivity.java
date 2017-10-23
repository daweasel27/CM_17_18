package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.MovieInfo;
import com.example.jp.projectofinal.fragments.SuggestionListFragment;

import java.util.ArrayList;


/**
 * Created by TiagoHenriques on 13/10/2017.
 */
public class SuggestionActivity extends AppCompatActivity
         {
    ArrayList<MovieInfo> list;
    //private TextView textViewTest;


    public ArrayList<MovieInfo> getList() {
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_suggestion_list);

        Intent intent = getIntent();
        list = (ArrayList<MovieInfo>) intent.getSerializableExtra("arg_key");
        for(MovieInfo movie : list){
            Log.d("lista", movie.getTitle());
        }
        getFragment(savedInstanceState);

        //textViewTest = (TextView) findViewById(R.id.textViewTest);
        //final String[] daysLabels = getTheWeatherForecast();
        //textViewTest.setText(daysLabels[0]);
    }

    private void getFragment(Bundle savedInstanceState){
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.layout_suggestions_list_fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            SuggestionListFragment dailyListFragment = new SuggestionListFragment(list);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_suggestions_list_fragment, dailyListFragment).commit();
        }
    }
}
