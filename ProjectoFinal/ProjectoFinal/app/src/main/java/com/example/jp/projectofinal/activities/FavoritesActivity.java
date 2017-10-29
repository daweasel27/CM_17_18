package com.example.jp.projectofinal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.fragments.FavoritesListFragment;
import com.example.jp.projectofinal.fragments.MovieInfoFragment;


/**
 * Created by TiagoHenriques on 17/10/2017.
 */
public class FavoritesActivity extends AppCompatActivity
        implements FavoritesListFragment.OnMovieSelectedListener,
            FavoritesListFragment.databaseEmpty {

    private static final String TAG = "MOVIE_TITLE";
    public int frag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favorites_list);

        getFragment(savedInstanceState);
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (frag == 1) {
                    setContentView(R.layout.fragment_suggestion_details);
                    MovieInfoFragment dailyListFragment = new MovieInfoFragment();

                    // In case this activity was started with special instructions from an
                    // Intent, pass the Intent's extras to the fragment as arguments
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layout_container_movie_details_fragment, dailyListFragment)
                            .addToBackStack(null)
                            .commit();
                    frag = 0;

                }else{
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    */
    private void getFragment(Bundle savedInstanceState){
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.layout_favorites_list_fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
           // frag = 0;

            // Create a new Fragment to be placed in the activity layout
            FavoritesListFragment dailyListFragment = new FavoritesListFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_favorites_list_fragment, dailyListFragment).commit();
        }
    }


    @Override
    public void onMovieSelected(String s) {
        Log.d("onMovieSelected", "onMovieSelected");
        setContentView(R.layout.fragment_movie_details);
        MovieInfoFragment detailsFragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putString(TAG, s);

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        detailsFragment.setArguments(args);

        //frag = 1;

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_container_movie_details_fragment, detailsFragment).commit();
    }

    @Override
    public void onDatabaseFragment() {
        setContentView(R.layout.empty_database_fragment);
    }
}

