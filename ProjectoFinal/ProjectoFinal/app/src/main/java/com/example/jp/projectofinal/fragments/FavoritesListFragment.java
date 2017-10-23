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

        // Just for test
        //addTestDB1();
        //addTestDB2();
        //returnValuesTestDB("\"The Recruit\"");

        myAdapter = new MyAdapter(
                getActivity(), // The current context (this activity)
                new ArrayList<String>());

        final String[] daysLabels = getFavoriteMoviesFromDB();

        // IMP...
        myAdapter.clear();
        for (String dayEntry : daysLabels) {
            Log.d("DAY_ENTRY", dayEntry);
            myAdapter.add(dayEntry);
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
        if (context instanceof OnMovieSelectedListener) {
            mListener = (OnMovieSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDaySelectedListener");
        }
    }

    public void deleteDB(){
        getContext().deleteDatabase("movies.db");
    }

    public void addTestDB1(){
        Log.d("addTestDB", "addTestDB");
        db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Killing Season");
        testValues.put(MovieContract.MovieEntry.COLUMN_YEAR, 2013);
        testValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, 91);
        testValues.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, "Mark Steven Johnson");
        testValues.put(MovieContract.MovieEntry.COLUMN_STARS, "Robert De Niro, John Travolta, Milo Ventimiglia");
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, 5.4);
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "Action, Drama");
        testValues.put(MovieContract.MovieEntry.COLUMN_STORY_LINE, "Killing Season tells the story of two veterans of the Bosnian War, one American, one Serbian, who clash in the Appalachian Mountain wilderness. FORD is a former American soldier who fought on the front lines in Bosnia. When our story begins, he has retreated to a remote cabin in the woods, trying to escape painful memories of war. The drama begins when KOVAC, a former Serbian soldier, seeks Ford out, hoping to settle an old score. What follows is a cat-and-mouse game in which Ford and Kovac fight their own personal World War III, with battles both physical and psychological. By the end of the film, old wounds are opened, suppressed memories are drawn to the surface and long-hidden secrets about both Ford and Kovac are revealed.");
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,  "Two veterans of the Bosnian War - one American, one Serbian - find their unlikely friendship tested when one of them reveals their true intentions.");
        testValues.put(MovieContract.MovieEntry.COLUMN_THUMB, "https://images-na.ssl-images-amazon.com/images/M/MV5BOTQ2MzA3MTk3MV5BMl5BanBnXkFtZTcwMDM5ODk2OQ@@._V1_.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER, "https://images-na.ssl-images-amazon.com/images/M/MV5BOTQ2MzA3MTk3MV5BMl5BanBnXkFtZTcwMDM5ODk2OQ@@._V1_UX182_CR0,0,182,268_AL_.jpg");


        long locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        if (locationRowId == -1) {
            Log.i(LOG_TAG, "Failed to insert row !");
        }

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {
            do {
                int columnIndex =
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnIndex));
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();
    }

    public void addTestDB2(){
        Log.d("addTestDB2", "addTestDB2");
        db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Recruit");
        testValues.put(MovieContract.MovieEntry.COLUMN_YEAR, 2003);
        testValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, 115);
        testValues.put(MovieContract.MovieEntry.COLUMN_DIRECTOR, "Roger Donaldson");
        testValues.put(MovieContract.MovieEntry.COLUMN_STARS, "Robert De Niro, John Travolta, Milo Ventimiglia");
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, 5.4);
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "Action, Drama, Mistery");
        testValues.put(MovieContract.MovieEntry.COLUMN_STORY_LINE, "In an era when the country's first line of defense, " +
                "intelligence, is more important than ever, this story opens the CIA's infamous closed doors and gives an " +
                "insider's view into the Agency: how trainees are recruited, how they are prepared for the spy game, and " +
                "what they learn to survive. James Clayton might not have the attitude of a typical recruit, but he is one " +
                "of the smartest graduating seniors in the country - and he's just the person that Walter Burke wants in the " +
                "Agency. James regards the CIA's mission as an intriguing alternative to an ordinary life, but before he becomes " +
                "an Ops Officer, James has to survive the Agency's secret training ground, where green recruits are molded " +
                "into seasoned veterans. As Burke teaches him the ropes and the rules of the game, James quickly rises through " +
                "the ranks and falls for Layla, one of his fellow recruits. But just when James starts to question his role " +
                "and his cat-and-mouse relationship with his mentor, Burke taps him for a special ...");
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,  "A brilliant young CIA trainee is asked by his mentor to help find a mole in the Agency.");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER, "https://images-na.ssl-images-amazon.com/images/M/MV5BMjE5MDMzOTk3MV5BMl5BanBnXkFtZTYwNTE0NTg2._V1_.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_THUMB, "https://images-na.ssl-images-amazon.com/images/M/MV5BMjE5MDMzOTk3MV5BMl5BanBnXkFtZTYwNTE0NTg2._V1_UX182_CR0,0,182,268_AL_.jpg");


        long locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        if (locationRowId == -1) {
            Log.i(LOG_TAG, "Failed to insert row !");
        }

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {
            do {
                int columnIndex =
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnIndex));
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();
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
                int columnThumb = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMB);
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

    public class MyAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> values;

        public MyAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.row, values);
            this.context = context;
            this.values = values;
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

            myTitle.setText(description[0] + " ("+description[1]+")");
            myDescription.setText(description[2]);

            Log.i("DESCRIPTION3", description[3]);

            /*
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(description[3]).getContent());
                imageView.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            new ImageLoadTaskFavorites(description[3], imageView).execute();

            return rowView;
        }
    }
}

