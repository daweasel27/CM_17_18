package com.example.jp.projectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.json.JSONException;

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
    private OnDaySelectedListener mListener;
    private static final String LOG_TAG = "LOG_TAG";

    public FavoritesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion_list, container, false);

        myAdapter = new MyAdapter(
                getActivity(), // The current context (this activity)
                new ArrayList<String>());

        final String[] daysLabels = getTheWeatherForecast();

        // IMP...
        myAdapter.clear();
        for (String dayEntry : daysLabels) {
            Log.d("DAY_ENTRY", dayEntry);
            myAdapter.add(dayEntry);
        }

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = myAdapter.getItem(position);
                Context context = view.getContext();
                Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
                mListener.onDaySelected(s);
            }
        });

        */

        // Just for test
        //addTestDB();
        returnValuesTestDB("\"The Recruit\"");

        return view;
    }

    // Container Activity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDaySelectedListener) {
            mListener = (OnDaySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDaySelectedListener");
        }
    }

    public void addTestDB(){
        MovieDbHelper dbHelper = new MovieDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "The Recruit");
        testValues.put(MovieContract.MovieEntry.COLUMN_YEAR, 2003);
        testValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, 115);
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, 6.6);
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "Action, Adventure");
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,  "A brilliant young CIA trainee is asked by his mentor to help find a mole in the Agency.");
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


    public void returnValuesTestDB(String s){
        MovieDbHelper dbHelper = new MovieDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String column = MovieContract.MovieEntry.COLUMN_TITLE;
        String selection = column+" like "+s;

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                selection, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (cursor.moveToFirst()) {
            do {
                int columnTitle = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnTitle));
                int columnYear = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnYear));
                int columnDesc = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
                Log.i(LOG_TAG, "Retrieving entry: " + cursor.getString(columnDesc));
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();
    }

    public static String[] getFavoriteMovies(){
        String[] resultStrs = new String[2];

        String movietitle = "The Recruit";
        String movieRating = "6.6";
        resultStrs[0] = movietitle + ":" + movieRating;

        resultStrs[1] = movietitle+"2" + ":" + movieRating+"2";

        return resultStrs;
    }

    private String [] getTheWeatherForecast() {
        /* Disable Strict Mode - Temporary Solution */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Parser
        //MoviesParser parser = new MoviesParser();
        String[] days = getFavoriteMovies();

        return days;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnDaySelectedListener {
        public void onDaySelected(String s);
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

            String description[] = values.get(position).split(":");

            switch (description[0]){
                case SANTIAGO:
                    imageView.setImageResource(R.drawable.deadpool2);
                    break;
                case CRASTO:
                    imageView.setImageResource(R.drawable.deadpool2);
                    break;
                case SNACK_BAR:
                    imageView.setImageResource(R.drawable.deadpool2);
                    break;
                default:
                    imageView.setImageResource(R.drawable.deadpool2);
                    //imageView.
            }

            myTitle.setText(description[0]);
            myDescription.setText(description[1]);

            // TODO - Quando estiver encerrado, fazer aparecer um TOAST a dizer encerrado

        /*
        textView.setText(values.get(position));
        // Change the icon for Windows and iPhone
        String s = EmentasUAParser.getMeal(position);
        */

            return rowView;
        }
    }
}
