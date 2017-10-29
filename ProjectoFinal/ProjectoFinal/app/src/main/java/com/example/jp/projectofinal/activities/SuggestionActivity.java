package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.MovieInfo;
import com.example.jp.projectofinal.fragments.FavoritesListFragment;
import com.example.jp.projectofinal.fragments.MovieInfoFragment;
import com.example.jp.projectofinal.fragments.MovieSuggestionInfoFragment;
import com.example.jp.projectofinal.fragments.MovieSuggestionFragment;
import com.example.jp.projectofinal.fragments.SuggestionListFragment;

import java.util.ArrayList;


/**
 * Created by TiagoHenriques on 13/10/2017.
 */
public class SuggestionActivity extends AppCompatActivity
        implements SuggestionListFragment.OnMovieSelectedListener
         {
             ArrayList<MovieInfo> list;

             public int frag = 0;
    private final static String MOVIE_ID_TAG = "MOVIE_ID_TAG";
             private ActionBar actionBar;
             //private TextView textViewTest;


    public ArrayList<MovieInfo> getList() {
        return list;
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case android.R.id.home:
                 if (frag == 1){
                     setContentView(R.layout.fragment_suggestion_list);
                     SuggestionListFragment dailyListFragment = new SuggestionListFragment(list);
                     getSupportFragmentManager().beginTransaction()
                             .replace(R.id.layout_suggestions_list_fragment, dailyListFragment)
                             .addToBackStack(null)
                             .commit();
                     frag = 0;
                 }
                 else{
                     Camera.sv.reset();
                     Intent intent = new Intent(this, Camera.class);
                     startActivity(intent);
                 }
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_suggestion_list);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        list = (ArrayList<MovieInfo>) intent.getSerializableExtra("arg_key");
        for(MovieInfo movie : list){
            Log.d("lista", movie.getTitle());
        }
        getFragment(savedInstanceState);
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
            frag = 0;

            // Create a new Fragment to be placed in the activity layout
            SuggestionListFragment dailyListFragment = new SuggestionListFragment(list);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_suggestions_list_fragment, dailyListFragment)
                        .addToBackStack(null)
                            .commit();
        }
    }

    /*
     @Override
     public void onMovieSelected(String s) {
         Log.d("onMovieSelected", "onMovieSelected");
         setContentView(R.layout.fragment_movie_details);
         MovieSuggestionInfoFragment detailsFragment = new MovieSuggestionInfoFragment();
         Bundle args = new Bundle();
         args.putString("arg", s);
     }*/

    @Override
     public void onMovieSelected(Integer s) {
         Log.d("OM_SUGGESTION", "OM_SUGGESTION");

         setContentView(R.layout.fragment_suggestion_details);
         MovieSuggestionFragment detailsFragment = new MovieSuggestionFragment();
         Bundle args = new Bundle();
         args.putInt(MOVIE_ID_TAG, s);
         // In case this activity was started with special instructions from an
         // Intent, pass the Intent's extras to the fragment as arguments
         detailsFragment.setArguments(args);

        frag = 1;

         // Add the fragment to the 'fragment_container' FrameLayout
         getSupportFragmentManager().beginTransaction()
                 .add(R.id.layout_container_movie_details_fragment, detailsFragment)
                    .commit();
     }

     @Override
     public void onBackPressed() {
             super.onBackPressed();
     }

 }

