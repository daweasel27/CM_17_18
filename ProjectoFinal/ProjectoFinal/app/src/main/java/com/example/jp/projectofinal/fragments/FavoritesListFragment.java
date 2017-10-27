package com.example.jp.projectofinal.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.asyncTasks.ImageLoadTaskFavorites;
import com.example.jp.projectofinal.db.MovieContract;
import com.example.jp.projectofinal.db.MovieDbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by TiagoHenriques on 17/10/2017.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesListFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private MyAdapter myAdapter;
    private OnMovieSelectedListener mListener;
    private databaseEmpty mDatabaseListener;
    private static final String LOG_TAG = "LOG_TAG";

    private static MovieDbHelper dbHelper;
    private static SQLiteDatabase db;

    public FavoritesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion_list, container, false);

        dbHelper = new MovieDbHelper(getActivity());

        myAdapter = new MyAdapter(
                getActivity(), // The current context (this activity)
                new ArrayList<String>());

        final String[] daysLabels = getFavoriteMoviesFromDB();

        // IMP...
        myAdapter.clear();
        for (String dayEntry : daysLabels) {
            myAdapter.add(dayEntry);
        }

        if(myAdapter.getSize() == 0){
            Context context = view.getContext();
            Toast toast = Toast.makeText(context, "You have no favorite movies ...", Toast.LENGTH_SHORT);
            toast.show();
            mDatabaseListener.onDatabaseFragment();
        }

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = myAdapter.getItem(position).split("_")[0];
                Context context = view.getContext();
                Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
                mListener.onMovieSelected(s);
            }
        });

        //returnValuesTestDB();

        return view;
    }

    // Container Activity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof databaseEmpty && context instanceof OnMovieSelectedListener) {
            mDatabaseListener = (databaseEmpty) context;
            mListener = (OnMovieSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDaySelectedListener");
        }
    }

    public static int getNumberDBRows(){
        db = dbHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MovieContract.MovieEntry.TABLE_NAME);
        Log.i("COUNTDB", Integer.toString(numRows));
        return numRows;
    }


    public static String[] getFavoriteMovies(){

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                null, // Columns for the "where" clause //selection
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        String[] resultStrs = new String[getNumberDBRows()];

        if (cursor.moveToFirst()) {
            do {
                Log.i(LOG_TAG, "Retrieving entry position : " + cursor.getColumnIndex(MovieContract.MovieEntry._ID));
                int columnTitle = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                String title = cursor.getString(columnTitle);
                Log.i(LOG_TAG, "Retrieving entry title: " + title);
                int columnYear = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR);
                String year = cursor.getString(columnYear);
                Log.i(LOG_TAG, "Retrieving entry year: " + year);
                int columnRat = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
                String rating = cursor.getString(columnRat);
                Log.i(LOG_TAG, "Retrieving entry rating: " + rating);
                int columnThumb = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
                String thumb = cursor.getString(columnThumb);
                Log.i(LOG_TAG, "Retrieving entry thumb: " + thumb);
                resultStrs[cursor.getPosition()] =  title + "_" + year + "_" + rating + "_" + thumb;
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();

        return resultStrs;
    }

    private String [] getFavoriteMoviesFromDB() {
        /* Disable Strict Mode - Temporary Solution */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] movies = getFavoriteMovies();

        return movies;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnMovieSelectedListener {
        public void onMovieSelected(String s);
    }

    public interface databaseEmpty {
        public void onDatabaseFragment();
    }

    public class MyAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> values;

        public MyAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.row, values);
            this.context = context;
            this.values = values;
        }

        public int getSize(){
            return values.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row, parent, false);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView myTitle = (TextView) rowView.findViewById(R.id.text1);
            TextView myDescription = (TextView) rowView.findViewById(R.id.text2);

            String description[] = values.get(position).split("_");

            myTitle.setText(description[0] + " ("+description[1].split("-")[0]+")");
            myDescription.setText(description[2]);

            new ImageLoadTaskFavorites(description[3], imageView).execute();

            return rowView;
        }
    }
}

