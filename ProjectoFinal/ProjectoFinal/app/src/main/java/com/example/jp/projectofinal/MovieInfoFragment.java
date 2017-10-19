package com.example.jp.projectofinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
public class MovieInfoFragment extends Fragment {

    private static final String TAG = "MOVIE_TITLE";
    private static final String LOG_TAG = "LOG_TAG";

    private static MovieDbHelper dbHelper;
    private static SQLiteDatabase db;

    // Layout file
    private ImageView imageViewMovie;
    private TextView textViewTitle;
    private TextView textViewGenre;
    private TextView textViewLength;
    private TextView textViewRating;
    private TextView textViewDirectorInput;
    private TextView textViewStarsInput;
    private TextView textViewDescription;
    private TextView textViewStoryLine;


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
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        dbHelper = new MovieDbHelper(getActivity());

        Bundle bundle = getArguments();
        String movie_title = bundle.getString(TAG);
        Log.i(TAG, movie_title);

        imageViewMovie = (ImageView) view.findViewById(R.id.imageViewMovie);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewGenre = (TextView) view.findViewById(R.id.textViewGenre);
        textViewLength = (TextView) view.findViewById(R.id.textViewLength);
        textViewRating = (TextView) view.findViewById(R.id.textViewRating);
        textViewDirectorInput = (TextView) view.findViewById(R.id.textViewDirectorInput);
        textViewStarsInput = (TextView) view.findViewById(R.id.textViewStarsInput);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
        textViewStoryLine = (TextView) view.findViewById(R.id.textViewStoryLine);

        populateFragment(movie_title);

        return view;
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
        String director = null;
        String stars=null;
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
            int columnDirector = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DIRECTOR);
            director = cursor.getString(columnDirector);
            Log.i(LOG_TAG, "Retrieving entry director: " + director);
            int columnGenre = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE);
            genre = cursor.getString(columnGenre);
            Log.i(LOG_TAG, "Retrieving entry genre: " + genre);
            int columnStars = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_STARS);
            stars = cursor.getString(columnStars);
            Log.i(LOG_TAG, "Retrieving entry stars: " + stars);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textViewTitle.setText(title + " ("+year+")");
        textViewLength.setText(length + "min");
        textViewRating.setText(rating +"/10.0");
        textViewGenre.setText(genre);
        textViewDirectorInput.setText(director);
        textViewStarsInput.setText(stars);
        textViewDescription.setText(description);
        textViewStoryLine.setText(storyLine);

        cursor.close();
        db.close();
    }
}
