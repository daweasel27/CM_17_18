package com.example.jp.projectofinal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jp.projectofinal.activities.SuggestionActivity;
import com.example.jp.projectofinal.dataModels.MovieInfo;
import com.example.jp.projectofinal.dataModels.SaveToFile;
import com.example.jp.projectofinal.dataModels.ToFirebase;
import com.example.jp.projectofinal.dataModels.ValuesToStore;
import com.example.jp.projectofinal.db.SendDataToFirebase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JP on 10/17/2017.
 */

public class MovieSuggestion{

    private final static String api_key = "5ea84afa0c7a5e8b2e3f94c9de502974";
    private Map<String, Integer> gender = new HashMap<>();

    private HashMap<String, List<ValuesToStore>> watched = new HashMap<>();

    private Map<String,  Map<String, Double>> results = new HashMap<>();
    private Map<String, Double> resultsFinal = new HashMap<>();
    private List<String> genderWatched = new ArrayList<>();
    private String movieN;
    private ArrayList<MovieInfo> suggestions = new ArrayList<>();
    int i = 0;

    private final Context context;

    public MovieSuggestion(Context context) {
        this.context = context;
    }

    public void putValues(){

        gender.put("Action", 28);
        gender.put("Adventure", 12);
        gender.put("Animation", 16);
        gender.put("Comedy", 35);
        gender.put("Crime", 80);
        gender.put("Documentary", 99);
        gender.put("Drama", 18);
        gender.put("Family", 10751);
        gender.put("Fantasy", 14);
        gender.put("History", 36);
        gender.put("Horror", 27);
        gender.put("Mystery", 9648);
        gender.put("Romance", 10749);
        gender.put("Science Fiction", 878);
        gender.put("Thriller", 53);
        gender.put("War", 10752);
        gender.put("Western", 37);
    }

    public void parseResults(){
        watched = SaveToFile.valuesList;
        putValues();
        List<ValuesToStore> vs = watched.get("Joao");
        if(vs == null){
            return;
        }
        Iterator<ValuesToStore> listIterator = vs.iterator();
        String valueName, name;
        Double value;
        HashMap<String, Double> watched = new HashMap<>();


        Map<String, Map<String,List<Double>>> firstPass = new HashMap<>();

        while (listIterator.hasNext()) {
            ValuesToStore vs1 = listIterator.next();
            valueName = vs1.getValueName();
            value = vs1.getValue();
            name = vs1.getMovieName();
            movieN = name;


            if (firstPass.containsKey(name)) {
                Map<String, List<Double>> temp = firstPass.get(name);

                if (temp.containsKey(valueName)) {
                    List<Double> tempList = temp.get(valueName);
                    tempList.add(value);
                    temp.put(valueName, tempList);
                    firstPass.put(name, temp);
                } else {
                    List<Double> tempList = new ArrayList<>();
                    tempList.add(value);
                    temp.put(valueName, tempList);
                    firstPass.put(name, temp);
                }

            } else {
                Map<String, List<Double>> temp = new HashMap<>();
                List<Double> tempList = new ArrayList<>();
                Log.e("valor", String.valueOf(value));
                try{
                    tempList.add(value);
                }catch (Exception e){
                    tempList.add(0.1);
                }
                temp.put(valueName, tempList);
                firstPass.put(name, temp);
            }
        }

        Iterator<Map.Entry<String, Map<String,List<Double>>>> it = firstPass.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Map<String,List<Double>>> pair = it.next();
            Map<String,List<Double>> ne = pair.getValue();

            Iterator<Map.Entry<String,List<Double>>> it2 = ne.entrySet().iterator();
            genderWatched.add(pair.getKey().split("-")[1]);
            while (it2.hasNext()) {
                Map.Entry<String,List<Double>> pair2 = it2.next();
                Double average = calcAverage(pair2.getValue());

                resultsFinal.put(pair2.getKey(), average);
                results.put(pair.getKey(),resultsFinal);

                Log.e("valores - ",pair2.getKey() + average  + " filme - " + pair.getKey());
            }
        }

        suggest();
        sendData();
        }


    private double calcAverage(List<Double> values) {
        double result = 0;
        for (Double value : values) {
            result += value;
        }
        return result / values.size();
    }

    public void sendData(){
        ToFirebase tf = new ToFirebase("Joao", movieN, String.valueOf(gender.get(genderWatched.get(0))),resultsFinal);
        SendDataToFirebase sf = new SendDataToFirebase();
        sf.send(tf);

    }

    public void suggest(){
        double max = 0;
        String pass = "";
        Iterator<Map.Entry<String, Map<String, Double>>> it4 = results.entrySet().iterator();
        while (it4.hasNext()) {
            Map.Entry<String, Map<String, Double>> pair = it4.next();
            Iterator<Map.Entry<String, Double>> it3 = pair.getValue().entrySet().iterator();
            while (it3.hasNext()) {
                Map.Entry<String, Double> pair2 = it3.next();
                if (pair2.getKey() == "attention" && pair2.getValue() > 90.0) {
                    if(pair2.getValue() > max){
                        max = pair2.getValue();
                        pass = String.valueOf(gender.get(genderWatched.get(i)));
                    }
                    Log.e("final", genderWatched.get(i));
                    Log.e("final", String.valueOf(gender.get(genderWatched.get(i))));
                    i++;
                }
            }
        }
        if(pass != "")
            new GetDataSync().execute(pass);

        /*

        Iterator<Map.Entry<String, Double>> it3 = resultsFinal.entrySet().iterator();
        while (it3.hasNext()) {
            Map.Entry<String, Double> pair = it3.next();
            if(pair.getKey() == "attention" && pair.getValue() > 90.0){
                Log.e("final", genderWatched.get(i));
                Log.e("final", String.valueOf(gender.get(genderWatched.get(i))));
                new GetDataSync().execute(String.valueOf(gender.get(genderWatched.get(i))));
                i++;
            }
        }*/

    }

    public void addSuggestion(MovieInfo e){
        suggestions.add(e);
    }




class GetDataSync extends AsyncTask<String, Void, String> {

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        // Start Activity C

        Intent intentFavs = new Intent(context, SuggestionActivity.class);
        intentFavs.putExtra("arg_key", suggestions);
        context.startActivity(intentFavs);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            getData(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void getData(String gender) throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://api.themoviedb.org/3/discover/movie?with_genres=" + gender + "&page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key="+api_key);
        try {
            JSONObject json1;
            JSONArray arrJson = json.getJSONArray("results");

            String[] arr = new String[arrJson.length()];
            for(int i = 0; i < arrJson.length(); i++){
                arr[i] = arrJson.getString(i);
                MovieInfo mi = new MovieInfo(String.valueOf(arrJson.optJSONObject(i).get("title")),
                        String.valueOf(arrJson.optJSONObject(i).get("id")),
                        String.valueOf(arrJson.optJSONObject(i).get("poster_path")),
                        String.valueOf(arrJson.optJSONObject(i).get("overview")),
                        String.valueOf(arrJson.optJSONObject(i).get("release_date")),
                        String.valueOf(arrJson.optJSONObject(i).get("vote_average")));
                suggestions.add(mi);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();

        }
    }
}
}