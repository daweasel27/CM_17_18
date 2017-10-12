package com.example.jp.projectofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class YoutubePlayer extends YouTubeFailureRecoveryActivity {

    private YouTubePlayerView playerView;
    private String youtube_url;
    private TextView testTextView;

    // IMPORTANT : CHANGE THIS
    private String DEVELOPER_KEY = "AIzaSyC3GrHmHq_a7MTpXJ2wzO9H5qryoYn7dJw";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);

        // Just for test
        testTextView = (TextView) findViewById(R.id.textViewEmotion);

        // The unique video id of the youtube video (can be obtained from video url)
        youtube_url = "hAUTdjf9rko";

        playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        playerView.initialize(DEVELOPER_KEY, this);

        downloadImageAsync();
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        //player.setFullscreen(true);
        player.setShowFullscreenButton(false);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

        if (!wasRestored) {
            player.loadVideo(youtube_url);
        }
    }

    private void downloadImageAsync() {
        // Now we can execute the long-running task at any time.
        //new MyAsyncTask().execute("https://media.contentapi.ea.com/content/dam/ea/easports/fifa/home/2017/june/10/fifa18-homepage-marquee-bg-xs.jpg");
        new MyAsyncTask().execute("Boa textView");
    }

    // The types specified here are the input data type, the progress type, and the result type
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
            testTextView.setVisibility(TextView.VISIBLE);
        }

        protected String doInBackground(String... strings) {
            Log.d("doInBackground", strings[0]);
            // Some long-running task like downloading an image.
            try {
                /*
                java.net.URL url = new java.net.URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
                */
                return strings[0];
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /*
        protected void onProgressUpdate(Progress... values) {
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
            progressBar.setProgress(values[0]);
        }
        */


        protected void onPostExecute(String result) {
            Log.d("OnPostExecute", result);
            // This method is executed in the UIThread
            // with access to the result of the long running task
            //imageView.setImageBitmap(result);
            testTextView.setText(result);
            // Hide the progress bar
            //progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}