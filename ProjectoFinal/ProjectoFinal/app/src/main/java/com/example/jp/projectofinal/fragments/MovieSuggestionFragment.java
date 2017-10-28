package com.example.jp.projectofinal.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.example.jp.projectofinal.activities.SuggestionActivity;
import com.example.jp.projectofinal.asyncTasks.ImageLoadTaskSuggestions;
import com.example.jp.projectofinal.db.MovieContract;
import com.example.jp.projectofinal.db.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Utilizador on 26/10/2017.
 */

public class MovieSuggestionFragment extends Fragment {

    private final static String MOVIE_ID_TAG = "MOVIE_ID_TAG";
    private final static String api_key = "5ea84afa0c7a5e8b2e3f94c9de502974";
    private static final String LOG_TAG = "LOG_TAG";

    private static MovieDbHelper dbHelper;
    private static SQLiteDatabase db;

    private ImageView imageView;
    private ImageButton imageButtonHeart;
    private TextView textViewTitle;
    private TextView textViewGenre;
    private TextView textViewLength;
    private TextView textViewRating;
    private TextView textViewReleaseDateInput;
    private TextView textViewDescription;
    private TextView textViewStoryLine;
    private ImageButton imageButtonBack;

    private String poster=null;
    private String title =null;
    private String release_date = null;
    private String rating =null;
    private String overview=null;
    private String tagline=null;
    private String genres=null;
    private String runtime=null;


    public MovieSuggestionFragment() {
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

        imageView = (ImageView) view.findViewById(R.id.imageViewMovie);
        imageButtonHeart = (ImageButton) view.findViewById(R.id.imageViewHeartSuggestion);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewGenre = (TextView) view.findViewById(R.id.textViewGenre);
        textViewLength = (TextView) view.findViewById(R.id.textViewLength);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
        textViewRating = (TextView) view.findViewById(R.id.textViewRating);
        textViewReleaseDateInput = (TextView) view.findViewById(R.id.textViewReleaseDateInput);
        textViewStoryLine = (TextView) view.findViewById(R.id.textViewStoryLine);
        imageButtonBack = (ImageButton) view.findViewById(R.id.imageButtonBack);

        Bundle bundle = getArguments();
        Integer movie_id = bundle.getInt(MOVIE_ID_TAG);
        Log.i(MOVIE_ID_TAG, movie_id.toString());

        getMovie(movie_id);


        imageButtonHeart.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
            //Intent intentFavs = new Intent(MainActivity.this, FavoritesActivity.class);
            //startActivity(intentFavs);
                imageButtonHeart.setImageResource(R.drawable.heartfull_small);
                Context context = view.getContext();
                Toast toast = Toast.makeText(context, "Movie added to favorites", Toast.LENGTH_SHORT);
                toast.show();
                //
                addTestDB();
        }});


        imageButtonBack.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                getActivity().onBackPressed();
                //getActivity().getSupportFragmentManager().popBackStack();

        }});

        return view;
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

        cursor.close();
        db.close();
    }


    private void getMovie(Integer id)  {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //JSONObject json = readJsonFromUrl("https://api.themoviedb.org/3/discover/movie?with_genres=" + gender + "&page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key="+api_key);
        JSONObject json = readJsonFromUrl("https://api.themoviedb.org/3/movie/"+id+"?api_key="+api_key);
        try {
            String strcode = (String) json.get("poster_path");
            Log.d("FELDIXX", strcode);

            JSONArray jsonArray = json.getJSONArray("genres");
            String genres[] = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                genres[i] = name;
                Log.d("GENRE", genres[i]);
            }

            String title = (String) json.get("original_title");
            Log.d("TITLE", title);

            String overview = (String) json.get("overview");
            Log.d("overview", overview);

            String release_date = (String) json.get("release_date");
            Log.d("release_date", release_date);

            Integer runtime = (Integer) json.get("runtime");
            Log.d("runtime", runtime.toString());

            String tagline = (String) json.get("tagline");
            Log.d("tagline", tagline);

            Double vote_average = (Double) json.get("vote_average");
            Log.d("vote_average", vote_average.toString());

            populateFragment(strcode, title, genres, runtime, vote_average, release_date, tagline, overview);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateFragment(String strcode, String title, String genres[], int runtime, Double vote_average, String release_date,
                                  String tagline, String overview){

        this.title = title;
        textViewTitle.setText(title);

        String genreText="";
        for(int i=0; i<genres.length;i++){
            if(i<=2)
                genreText+=genres[i]+"/";
        }
        this.genres = genreText.substring(0, genreText.length()-1);
        textViewGenre.setText(this.genres);

        this.release_date = release_date;
        textViewReleaseDateInput.setText(this.release_date);

        this.runtime = Integer.toString(runtime);
        textViewLength.setText(this.runtime+"min");

        this.rating = vote_average.toString();
        textViewRating.setText(this.rating+"/10.0");

        this.tagline = tagline;
        textViewDescription.setText(this.tagline);

        this.overview = overview;
        textViewStoryLine.setText(this.overview);

        this.poster = "http://image.tmdb.org/t/p/w185"+strcode;
        new ImageLoadTaskSuggestions("http://image.tmdb.org/t/p/w185"+ strcode, imageView).execute();
    }

    private String readAll(Reader rd) {
        StringBuilder sb = new StringBuilder();
        int cp;
        try {
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url)  {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = null;
            try {
                json = new JSONObject(jsonText);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
