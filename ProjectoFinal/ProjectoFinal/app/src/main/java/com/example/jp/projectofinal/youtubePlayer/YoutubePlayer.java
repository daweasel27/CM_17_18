package com.example.jp.projectofinal.youtubePlayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.activities.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayer extends YouTubeFailureRecoveryActivity {

    private YouTubePlayerView playerView;
    private String youtube_url;
    //private TextView testTextView;

    // IMPORTANT : CHANGE THIS
    private String DEVELOPER_KEY = "AIzaSyC3GrHmHq_a7MTpXJ2wzO9H5qryoYn7dJw";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);

        // Just for test
        //testTextView = (TextView) findViewById(R.id.textViewEmotion);

        // The unique video id of the youtube video (can be obtained from video url)
        youtube_url = "hAUTdjf9rko";

        playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        playerView.initialize(DEVELOPER_KEY, this);

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
            //player.loadVideo(youtube_url);
            new MyAsyncTask().execute(player);
        }
    }


    // The types specified here are the input data type, the progress type, and the result type
    private class MyAsyncTask extends AsyncTask<YouTubePlayer, Void, Void> {
        @Override
        protected Void doInBackground(YouTubePlayer... params) {
            YouTubePlayer player = params[0];
            player.loadVideo(youtube_url);
            return null;
        }

        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
            //testTextView.setVisibility(TextView.VISIBLE);
        }

        /*
        protected void onProgressUpdate(Progress... values) {
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
            progressBar.setProgress(values[0]);
        }
        */


        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("OnPostExecute", "onPost");
            // This method is executed in the UIThread
            // with access to the result of the long running task
            //imageView.setImageBitmap(result);
            playerView.setVisibility(View.VISIBLE);
            // Hide the progress bar
            //progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}