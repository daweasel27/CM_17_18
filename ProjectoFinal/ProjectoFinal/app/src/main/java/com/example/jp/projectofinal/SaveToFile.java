package com.example.jp.projectofinal;

import android.os.Environment;
import android.util.Log;

import com.example.jp.projectofinal.DataModels.ValuesToStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JP on 10/14/2017.
 */

public class SaveToFile {
    //String name;
    File file;

    public static HashMap<String, List<ValuesToStore>> valuesList = new HashMap<>();

    //private HashMap<Integer, String> movieValues = new HashMap<>();


    public SaveToFile(){
    }

    public void setFile(String name){
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"SmileDir");
        if(!fileDir.exists()){
            try{
                fileDir.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+ name);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addValuesExpressions(String emotion, double value, String movie) {
        if(value != 0.0){
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            ValuesToStore vs = new ValuesToStore(emotion, value, currentDateTimeString, movie);
            if(valuesList.containsKey("Joao")){
                List<ValuesToStore> list;
                list = valuesList.get("Joao");
                list.add(vs);
                valuesList.remove("Joao");
                valuesList.put("Joao", list);
                if(emotion == "attention"){
                    Log.e("ola", String.valueOf(value));
                }
                //valuesList.remove("João");
                //valuesList.put("João", list);
            }
            else{
                List<ValuesToStore> list = new ArrayList<>();
                list.add(vs);
                valuesList.put("Joao", list);
                Log.e("ola", String.valueOf(valuesList.size()));

            }
            //HashMap<String, Double> temp = new HashMap<>();
            //temp.put(emotion, value);
            //expressionList.put("João", temp);
        }



    }

    public void saveList(){
        try {
            FileWriter fileWriter  = new FileWriter(file);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            Iterator it = valuesList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                bfWriter.write(  pair.getKey().toString());

                bfWriter.newLine();
                List<ValuesToStore> vs = (List<ValuesToStore>) pair.getValue();
                Iterator<ValuesToStore> listIterator = vs.iterator();

                while (listIterator.hasNext()){
                    ValuesToStore vs1 = listIterator.next();
                    bfWriter.write(vs1.getTime()+ " -- " + vs1.getMovieName() + " -> " +vs1.getValueName() + " : " + vs1.getValue());
                    bfWriter.newLine();
                }

                bfWriter.newLine();
            }

            bfWriter.close();

            MainActivity.ms.parseResults();

            Log.e("ola", String.valueOf(SaveToFile.valuesList.size()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
