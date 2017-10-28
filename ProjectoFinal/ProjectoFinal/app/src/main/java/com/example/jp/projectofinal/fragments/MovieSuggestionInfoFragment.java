package com.example.jp.projectofinal.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.dataModels.MovieInfo;

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
public class MovieSuggestionInfoFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "MOVIE_TITLE";
    private static final String LOG_TAG = "LOG_TAG";
    private MovieInfo mi;

    // Layout file
    private ImageView imageViewMovie;
    private TextView textViewTitle;
    private TextView textViewLength;
    private TextView textViewRating;
    private TextView textViewDescription;


    public MovieSuggestionInfoFragment() {
        // Required empty public constructor
    }

    public MovieSuggestionInfoFragment(MovieInfo mi) {
        this.mi = mi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion_details, container, false);

        Bundle bundle = getArguments();
        MovieInfo movie_title = bundle.getParcelable(TAG);

        imageViewMovie = (ImageView) view.findViewById(R.id.imageViewMovie);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewLength = (TextView) view.findViewById(R.id.textViewLength);
        textViewRating = (TextView) view.findViewById(R.id.textViewRating);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);

        populateFragment(movie_title);

        return view;
    }

    public void populateFragment(MovieInfo movie_title){

        String poster = "http://image.tmdb.org/t/p/w185//" + movie_title.getBackdrop_path();
        String title = movie_title.getTitle();
        String year= movie_title.getRelease_date().split("-")[0];
        String rating=movie_title.getVote_average();
        String description= movie_title.getOverview();

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(poster).getContent());
            imageViewMovie.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textViewTitle.setText(title + " ("+year+")");
        textViewRating.setText(rating +"/10.0");
        textViewDescription.setText(description);
    }

    @Override
    public void onClick(View view) {

    }
}
