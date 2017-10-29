package com.example.jp.projectofinal.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.jp.projectofinal.activities.MainActivity;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by TiagoHenriques on 12/10/2017.
 */

// The types specified here are the input data type, the progress type, and the result type
public class LoadYTVideoAsyncTask extends AsyncTask<YouTubePlayer, Void, Void> {

    @Override
    protected Void doInBackground(YouTubePlayer... params) {
        YouTubePlayer player = params[0];
        HashMap<String, String> trailersToWatch = MainActivity.mv.getTrailers();

        Random random = new Random();
        List<String> keys = new ArrayList<>(trailersToWatch.keySet());
        String randomKey = keys.get( random.nextInt(keys.size()));
        String value = trailersToWatch.get(randomKey);

        player.loadVideo(value);

        MainActivity.mv.addWatchedTrailers(randomKey, value);
        Log.e("filme a ver111   - --  ", randomKey);

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