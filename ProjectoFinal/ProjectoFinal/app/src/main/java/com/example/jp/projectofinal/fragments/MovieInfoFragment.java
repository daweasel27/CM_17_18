package com.example.jp.projectofinal.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.activities.FavoritesActivity;
import com.example.jp.projectofinal.db.MovieContract;
import com.example.jp.projectofinal.db.MovieDbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TiagoHenriques on 13/10/2017.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieInfoFragment extends Fragment  {

    private static final String TAG = "MOVIE_TITLE";
    private static final String LOG_TAG = "LOG_TAG";
    private boolean movieOnDB = true;

    private static MovieDbHelper dbHelper;
    private static SQLiteDatabase db;

    // Layout file
    private ImageView imageViewMovie;
    private TextView textViewTitle;
    private TextView textViewGenre;
    private TextView textViewLength;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewDescription;
    private TextView textViewStoryLine;
    private ImageButton imageButtonHeart;

    private String poster=null;
    private String title =null;
    private String release_date = null;
    private String rating =null;
    private String overview=null;
    private String tagline=null;
    private String genres=null;
    private String runtime=null;


    public MovieInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion_details, container, false);

        dbHelper = new MovieDbHelper(getActivity());

        Bundle bundle = getArguments();
        final String movie_title = bundle.getString(TAG);
        Log.i(TAG, movie_title);

        imageViewMovie = (ImageView) view.findViewById(R.id.imageViewMovie);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewReleaseDate = (TextView) view.findViewById(R.id.textViewReleaseDateInput);
        textViewGenre = (TextView) view.findViewById(R.id.textViewGenre);
        textViewLength = (TextView) view.findViewById(R.id.textViewLength);
        textViewRating = (TextView) view.findViewById(R.id.textViewRating);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
        textViewStoryLine = (TextView) view.findViewById(R.id.textViewStoryLine);
        imageButtonHeart = (ImageButton) view.findViewById(R.id.imageViewHeartSuggestion);

        populateFragment(movie_title);


        imageButtonHeart.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                if(movieOnDB){
                    removeFromDB(movie_title);
                    //deleteDB();
                    movieOnDB = false;
                    Context context = view.getContext();
                    Toast toast = Toast.makeText(context, "Movie removed from database", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    addTestDB();
                    Context context = view.getContext();
                    Toast toast = Toast.makeText(context, "Movie added to database", Toast.LENGTH_SHORT);
                    toast.show();
                    movieOnDB=true;
                }

            }});

        return view;
    }

    public void deleteDB(){
        getContext().deleteDatabase("movies.db");
    }

    private void removeFromDB(String movie_title){
        Log.d("REMOVE", "REMOVE");

        db = dbHelper.getWritableDatabase();

        String movieT = ("\""+movie_title+"\"");
        db.execSQL("DELETE FROM "+MovieContract.MovieEntry.TABLE_NAME+" WHERE "+MovieContract.MovieEntry.COLUMN_TITLE+" = " + movieT+";");

        imageButtonHeart.setImageResource(R.drawable.heart_small);

        db.close();
    }

    public void addTestDB(){
        Log.d("addTestDB_MSF", "addTestDB_MSF");
        db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, this.title);
        testValues.put(MovieContract.MovieEntry.COLUMN_YEAR, this.release_date);
        testValues.put(MovieContract.MovieEntry.COLUMN_LENGTH, this.runtime);
        testValues.put(MovieContract.MovieEntry.COLUMN_RATING, Double.parseDouble(this.rating));
        testValues.put(MovieContract.MovieEntry.COLUMN_GENRE, this.genres);
        testValues.put(MovieContract.MovieEntry.COLUMN_STORY_LINE, this.overview);
        testValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, this.tagline);
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER, this.poster);


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

        imageButtonHeart.setImageResource(R.drawable.heartfull_small);

        cursor.close();
        db.close();
    }


    public void populateFragment(String movie_title){

        db = dbHelper.getReadableDatabase();

        //String title = "\""+movie_title+"\";
        String titleM = "\""+movie_title+"\"";

        Log.i(LOG_TAG, titleM);
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                MovieContract.MovieEntry.COLUMN_TITLE+"="+titleM,  // Columns for the "where" clause //selection
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        String poster = null;
        String title = null;
        String year=null;
        String rating=null;
        String description= null;
        String storyLine=null;
        String genre=null;
        String length=null;
        if (cursor.moveToFirst()) {
            Log.i(LOG_TAG, "Retrieving entry position : " + cursor.getColumnIndex(MovieContract.MovieEntry._ID));
            int columnTitle = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            title = cursor.getString(columnTitle);
            Log.i(LOG_TAG, "Retrieving entry title: " + title);
            int columnYear = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR);
            year = cursor.getString(columnYear);
            Log.i(LOG_TAG, "Retrieving entry year: " + year);

            int columnGenre = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE);
            genre = cursor.getString(columnGenre);
            Log.i(LOG_TAG, "Retrieving entry genre: " + genre);

            int columnLength = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_LENGTH);
            length = cursor.getString(columnLength);
            Log.i(LOG_TAG, "Retrieving entry length: " + length);
            int columnRat = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
            rating = cursor.getString(columnRat);
            Log.i(LOG_TAG, "Retrieving entry rating: " + rating);
            int columnDescription = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
            description = cursor.getString(columnDescription);
            Log.i(LOG_TAG, "Retrieving entry description: " + description);
            int columnStoryLine = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_STORY_LINE);
            storyLine = cursor.getString(columnStoryLine);
            Log.i(LOG_TAG, "Retrieving entry storyline: " + storyLine);
            int columnPoster = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
            poster = cursor.getString(columnPoster);
            Log.i(LOG_TAG, "Retrieving entry poster: " + poster);
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(poster).getContent());
            imageViewMovie.setImageBitmap(bitmap);
            this.poster = poster;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.title = title;
        textViewTitle.setText(title);

        this.runtime = length;
        textViewLength.setText(this.runtime + "min");

        this.rating = rating.toString();

        textViewRating.setText(this.rating +"/10.0");

        this.release_date = year;
        textViewReleaseDate.setText(this.release_date);

        this.genres = genre;
        textViewGenre.setText(this.genres);

        this.tagline = description;
        textViewDescription.setText(this.tagline);

        this.overview = storyLine;
        textViewStoryLine.setText(this.overview);

        imageButtonHeart.setImageResource(R.drawable.heartfull_small);

        cursor.close();
        db.close();
    }
}
