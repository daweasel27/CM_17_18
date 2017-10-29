package com.example.jp.projectofinal.dataModels;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JP on 10/17/2017.
 */

public class MoviesSuggestionInfo {

    private HashMap<String, String> trailers = new HashMap<>();
    private List<String> movieList = new ArrayList<>();
    private HashMap<String, String> watchedTrailers = new HashMap<>();

    public List<String> getMovieList() {
        return movieList;
    }

    public void addMovieList(String movie) {
        movieList.add(movie);
    }

    public void addWatchedTrailers(String name, String watched) {
        watchedTrailers.clear();
        watchedTrailers.put(name, watched);
    }

    public String getLastWatchedTrailer(){

        Iterator<Map.Entry<String, String>> iterator = watchedTrailers.entrySet().iterator();
        Map.Entry<String, String> result = null;
        while (iterator.hasNext()) {
            result = iterator.next();
        }
        Log.e("filme a ver   - --  ", result.getKey());

        return result.getKey();
    }

    public void initializeMovies(){
        trailers.put("It-Horror","xKJmEC5ieOk");
        trailers.put("Red Sparrow-Thriller", "L5KYCsWhado");
        trailers.put("Tomb Raider-Action","8ndhidEmUbI");
        trailers.put("Blockers-Comedy","XMsgjmsfOyY");
        trailers.put("Peter Rabbit-Animation","lsfH0XCuqiw");
    }

    public HashMap<String, String> getTrailers(){
        return trailers;
    }
}
