package com.example.jp.projectofinal.dataModels;

/**
 * Created by Jota on 10/28/2017.
 */

public class EmotionValues {
    private String name;
    private Double value;

    public EmotionValues(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }
}
