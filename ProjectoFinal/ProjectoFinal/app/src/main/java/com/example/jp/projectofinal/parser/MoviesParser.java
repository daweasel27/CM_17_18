package com.example.jp.projectofinal.parser;

import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by TiagoHenriques on 13/10/2017.
 */

public class MoviesParser {

    private static final String LOG_TAG = "PARSER";
    private static String[] mealsInfo;

    private static final String movie_url = "http://services.web.ua.pt/sas/ementas?date=week&format=json";
    //private static final String movie_url = "http://www.theimdbapi.org/api/find/movie?title=therecruit";

    public String getMealsInfo() {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String mealsJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //URL url = new URL("http://services.web.ua.pt/sas/ementas?date=week&format=json");
            URL url = new URL(movie_url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                mealsJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                mealsJsonStr = null;
            }
            mealsJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            mealsJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        Log.d("CENAS", mealsJsonStr.toString());
        return mealsJsonStr;
    }


    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private static String getReadableDateString(long time) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private static String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }


    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public static String[] getMealDataFromJson(String mealsJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.

        final String OWN_LIST_MENUS = "menus";
        final String OWN_LIST_MENU = "menu";
        final String OWN_ATTRIBUTES = "@attributes";
        final String OWN_CANTEEN = "canteen";
        final String OWN_MEAL = "meal";
        final String OWN_DATE = "date";
        final String OWN_ITEMS = "items";
        final String OWN_ITEM = "item";


        JSONObject mealsJson = new JSONObject(mealsJsonStr);

        JSONObject menusArray = mealsJson.getJSONObject(OWN_LIST_MENUS);
        JSONArray menuArray = menusArray.getJSONArray(OWN_LIST_MENU);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[menuArray.length()];
        mealsInfo = new String[menuArray.length()];

        for (int i = 0; i < menuArray.length(); i++) {
            String canteen;
            String mealType;
            String dayFormat;

            // Get the JSON object representing the day
            JSONObject dayMeal = menuArray.getJSONObject(i);

            Log.d("TAG", dayMeal.toString());

            //JSONArray attributesObject = dayMeal.getJSONArray(OWN_ATTRIBUTES).getJSONObject(0);
            canteen = dayMeal.getJSONObject(OWN_ATTRIBUTES).getString(OWN_CANTEEN);
            mealType = dayMeal.getJSONObject(OWN_ATTRIBUTES).getString(OWN_MEAL);
            dayFormat = dayMeal.getJSONObject(OWN_ATTRIBUTES).getString(OWN_DATE);

            /*

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayMeal.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayMeal.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }
        */
            resultStrs[i] = canteen + ":" + dayFormat.substring(0, 16) + ":" + mealType;

            String sopa, carne, peixe, dieta, vegetariano, opcao, salada, diversos, sobremesa;

            // TODO - Fazer um fragmento para snack-bar porque os opcoes sao menos

            try {
                if (dayMeal.getJSONObject(OWN_ATTRIBUTES).getString("disabled").equals("0")) {

                    JSONArray elementosEmenta = dayMeal.getJSONObject(OWN_ITEMS).getJSONArray(OWN_ITEM);
                    // get the several meals in a canteen, in a day
                    sopa = parseForObjectOrString(elementosEmenta, 0);
                    carne = parseForObjectOrString(elementosEmenta, 1);
                    peixe = parseForObjectOrString(elementosEmenta, 2);
                    dieta = parseForObjectOrString(elementosEmenta, 3);
                    vegetariano = parseForObjectOrString(elementosEmenta, 4);
                    opcao = parseForObjectOrString(elementosEmenta, 5);
                    salada = parseForObjectOrString(elementosEmenta, 6);
                    diversos = parseForObjectOrString(elementosEmenta, 7);
                    sobremesa = parseForObjectOrString(elementosEmenta, 8);

                    /* sopa:prato_normal_carne:prato_normal_peixe:prato_dieta:prato_vegetariano
                    *   :prato_opcao:salada:diversos:sobremesa*/

                    mealsInfo[i] = sopa + ":" + carne + ":" + peixe + ":" + dieta + ":" + vegetariano
                            + ":" + opcao + ":" + salada + ":" + diversos + ":" + sobremesa;
                } else {
                    mealsInfo[i] = "ENCERRADO";
                }
            } catch (Exception e) {
                // Snack-Bar/Self
                if (dayMeal.getJSONObject(OWN_ATTRIBUTES).getString("disabled").equals("0")) {
                    JSONArray elementosEmenta = dayMeal.getJSONObject(OWN_ITEMS).getJSONArray(OWN_ITEM);
                    sopa = parseForObjectOrString(elementosEmenta, 0);
                    carne = parseForObjectOrString(elementosEmenta, 1);
                    peixe = parseForObjectOrString(elementosEmenta, 2);
                    salada = parseForObjectOrString(elementosEmenta, 3);
                    diversos = parseForObjectOrString(elementosEmenta, 4);
                    sobremesa = parseForObjectOrString(elementosEmenta, 5);

                    mealsInfo[i] = sopa + ":" + carne + ":" + peixe + ":" + salada + ":" + diversos + ":" + sobremesa;
                }
            }
        }
        for (String meal : mealsInfo) {
            Log.d("MEAL_CENAS", meal);
        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }

        return resultStrs;
    }

    public static String getMealInfo(int position) {
        return mealsInfo[position];
    }

    private static String parseForObjectOrString(JSONArray array, int index) throws JSONException {
        JSONObject tempJsonObject = array.optJSONObject(index);
        if (null == tempJsonObject) {
            // no json object, treat as string
            return array.getString(index);
        } else {
            return array.getJSONObject(index).getJSONObject("@attributes").getString("name");
        }
    }
}