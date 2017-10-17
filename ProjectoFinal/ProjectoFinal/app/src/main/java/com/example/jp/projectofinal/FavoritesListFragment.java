package com.example.jp.projectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


        // Just for test
        //addTestDB();
        //returnValuesTestDB("\"The Recruit\"");

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

    public void addTestDB(){
        Log.d("addTestDB", "addTestDB");
        db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Killing Season");
        testValues.put(MovieContract.MovieEntry.COLUMN_YEAR, 2013);
        testValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, 91);
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, 5.4);
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "Action, Drama");
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,  "Two veterans of the Bosnian War - one American, one Serbian - find their unlikely friendship tested when one of them reveals their true intentions.");
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

        private final String SANTIAGO = "Refeitório de Santiago";
        private final String CRASTO = "Refeitório do Crasto";
        private final String SNACK_BAR = "Snack-Bar/Self";
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

            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(description[3]).getContent());
                imageView.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            textView.setText(values.get(position));
            // Change the icon for Windows and iPhone
            String s = EmentasUAParser.getMeal(position);
            */

            return rowView;
        }
    }
}
