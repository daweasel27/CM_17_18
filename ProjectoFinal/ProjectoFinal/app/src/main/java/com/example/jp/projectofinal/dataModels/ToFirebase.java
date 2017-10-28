package com.example.jp.projectofinal.dataModels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jota on 10/28/2017.
 */

public class ToFirebase {
    private String userName;
    private String movieName;
    private String movieGenre;

    private Map<String, Double> valuesList = new HashMap<>();

    public ToFirebase() {
    }

    public ToFirebase(String userName, String movieName, String movieGenre, Map<String, Double> valuesList) {
        this.userName = userName;
        this.movieName = movieName;
        this.movieGenre = movieGenre;
        this.valuesList = valuesList;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public Map<String, Double> getValuesList() {
        return valuesList;
    }
}
