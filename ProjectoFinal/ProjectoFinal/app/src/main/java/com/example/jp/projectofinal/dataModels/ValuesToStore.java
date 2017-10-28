package com.example.jp.projectofinal.dataModels;

/**
 * Created by JP on 10/17/2017.
 */

public class ValuesToStore {

    String valueName;
    Double value;
    String time;
    String movieName;

    public ValuesToStore(String valueName, Double value, String time, String movieName) {

        this.valueName = valueName;
        this.value = value;
        this.time = time;
        this.movieName = movieName;
    }

    public String getValueName() {
        return valueName;
    }

    public Double getValue() {
        return value;
    }

    public String getTime() {
        return time;
    }

    public String getMovieName() {
        return movieName;
    }
}
