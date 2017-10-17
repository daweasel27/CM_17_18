package com.example.jp.projectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by TiagoHenriques on 17/10/2017.
 */
public class FavoritesActivity extends AppCompatActivity
        implements FavoritesListFragment.OnDaySelectedListener{

    //private TextView textViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_suggestion_list);

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
            FavoritesListFragment dailyListFragment = new FavoritesListFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_suggestions_list_fragment, dailyListFragment).commit();
        }
    }


    @Override
    public void onDaySelected(String s) {
        Log.d("ON_DAY_SELECTED", "AIAI");
        setContentView(R.layout.fragment_movie_info);
        MovieInfoFragment detailsFragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        //args.putString(TAG, s);
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //detailsFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_container_movie_info_fragment, detailsFragment).commit();
    }
}

