package com.example.jp.projectofinal.dataModels;

import java.util.HashMap;

/**
 * Created by JP on 10/24/2017.
 */

public class MovieUserProfile {
    private String userName;
    private String movieName;
    private String movieGenre;
    private HashMap<String, Double> emotionValues = new HashMap<>();

    public MovieUserProfile(String userName, String movieName, String movieGenre, HashMap<String, Double> emotionValues) {
        this.userName = userName;
        this.movieName = movieName;
        this.movieGenre = movieGenre;
        this.emotionValues = emotionValues;
    }

    public String getUserName() {
        return userName;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public HashMap<String, Double> getEmotionValues() {
        return emotionValues;
    }
}
