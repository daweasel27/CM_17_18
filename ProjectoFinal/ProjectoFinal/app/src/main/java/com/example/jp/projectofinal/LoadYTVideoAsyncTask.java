package com.example.jp.projectofinal;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayer;
import java.util.Random;

/**
 * Created by TiagoHenriques on 12/10/2017.
 */

// The types specified here are the input data type, the progress type, and the result type
public class LoadYTVideoAsyncTask extends AsyncTask<YouTubePlayer, Void, Void> {

    final String[] trailers = {"hAUTdjf9rko", "DblEwHkde8U", "ue80QwXMRHg"};

    @Override
    protected Void doInBackground(YouTubePlayer... params) {
        YouTubePlayer player = params[0];
        player.loadVideo(trailers[new Random().nextInt(trailers.length)]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("onPostExecute", "onPostExecute");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("onPreExecute", "onPreExecute");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Log.d("onProgressUpdate", "onProgressUpdate");
    }
}