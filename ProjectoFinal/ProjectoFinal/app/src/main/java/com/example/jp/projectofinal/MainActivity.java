package com.example.jp.projectofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
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
