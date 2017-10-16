package com.example.jp.projectofinal;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by JP on 10/14/2017.
 */

public class saveToFile {
    String name;
    File file;
    private List<Double> smileList = new ArrayList<>();
    public saveToFile(String name){
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

    public void addSmileValue(double smile){
        smileList.add(smile);
    }

    public void saveList(){
        try {
            FileWriter fileWriter  = new FileWriter(file);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);
            bfWriter.write("Smile");
            bfWriter.newLine();
            Iterator<Double> listIterator = smileList.iterator();
            while (listIterator.hasNext()) {
                if(listIterator.next() < 1.0){
                    bfWriter.write("0.0");
                }
                else{
                    bfWriter.write(Double.toString(listIterator.next()));
                }
                bfWriter.newLine();

            }
            bfWriter.close();
            Log.println(Log.ASSERT, "smileValue", "ola");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
