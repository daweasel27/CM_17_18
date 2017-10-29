package com.example.jp.projectofinal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.asyncTasks.ImageLoadTaskFavorites;
import com.example.jp.projectofinal.dataModels.EmotionValues;
import com.example.jp.projectofinal.dataModels.ToFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by TiagoHenriques on 17/10/2017.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieProfileFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private MyAdapter myAdapter;
    private String[] resultStrs;

    final List<ToFirebase> mJournalEntries = new ArrayList<>();
    final HashMap<String, List<ToFirebase>> listMovies = new HashMap<>();
    final HashMap<String, List<EmotionValues>> listFinal = new HashMap<>();


    public MovieProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movierofile_list, container, false);


        getData();


        //final ArrayList<String> a = new ArrayList<>();
        //for (String dayEntry : listFinal.keySet()) {
        //    a.add(dayEntry);
        //}


        //arrayAdapter.clear();
        /*
        Log.e("conas", Integer.toString(this.resultStrs.length));
        for(int i=0; i<this.resultStrs.length;i++){
            Log.e("conas", this.resultStrs[i]);
        }
        */

        /*
        myAdapter = new MyAdapter(
                getActivity(), // The current context (this activity)
                new ArrayList<String>());

        final String[] daysLabels = this.resultStrs;

        // IMP...
        myAdapter.clear();
        for (String dayEntry : daysLabels) {
            myAdapter.add(dayEntry);
        }


        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);
        */

        cenas();

        return view;
    }

    public void cenas(){
        Log.e("cenas", "cenas");
    }

    public String[] getFavoriteMovies(){

        return this.resultStrs;
    }

    @Override
    public void onClick(View v) {

    }

    public void getData(){
        Log.e("getData", "getData");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("movie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.e("firebase - ", noteSnapshot.child("valuesList").toString());
                    noteSnapshot.getChildren().iterator();
                    ToFirebase note = noteSnapshot.getValue(ToFirebase.class);
                    mJournalEntries.add(note);
                    if(!listMovies.containsKey(note.getMovieName())){
                        List<ToFirebase> ne = new ArrayList<>();
                        ne.add(note);
                        listMovies.put(note.getMovieName(), ne);
                    }
                    else {
                        List<ToFirebase> ne = listMovies.get(note.getMovieName());
                        listMovies.remove(note.getMovieName());
                        ne.add(note);
                        listMovies.put(note.getMovieName(), ne);
                    }
                }
                avg();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d(LOG_TAG, databaseError.getMessage());
            }
        });
    }

    public void avg(){
        Iterator it = listMovies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ToFirebase>> pair = (Map.Entry)it.next();
            List<ToFirebase> ls = pair.getValue();
            HashMap<String,Double> le = new HashMap<>();
            for(ToFirebase tf : ls){
                for(String ke :tf.getValuesList().keySet()){
                    if(!le.containsKey(ke)){
                        le.put(ke,tf.getValuesList().get(ke));
                    }
                    else{
                        le.put(ke,tf.getValuesList().get(ke)+le.get(ke));
                    }
                }
            }
            ArrayList<EmotionValues> listE = new ArrayList<>();
            for(String ke : le.keySet()){
                listE.add(new EmotionValues(ke,le.get(ke)/ls.size()));
            }
            this.listFinal.put(pair.getKey(),listE);
        }

        this.resultStrs = new String[this.listFinal.size()];

        int i=0;
        for(String ke : this.listFinal.keySet()){
            Log.e("final result -",ke);
            String cenas="";
            for(EmotionValues e: this.listFinal.get(ke)){
                Log.e("TESTE - " , e.getName() + " --- " + e.getValue());
                cenas += e.getName() + "@" + e.getValue();
            }
            this.resultStrs[i] = ke+":"+cenas;
            Log.e("UIUIUI", this.resultStrs[i]);
            i++;
        }

        Log.d("tropa", Integer.toString(this.resultStrs.length));

        writeToFile(this.resultStrs, getContext());

    }


    public void writeToFile(String data[],Context context){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("movie_profiles.txt", Context.MODE_PRIVATE));
            for(int i=0; i<data.length; i++)
                outputStreamWriter.write(data[i]+"\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
/*
    // Container Activity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof databaseEmpty && context instanceof OnMovieSelectedListener) {
            mListener = (OnMovieSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDaySelectedListener");
        }
    }

    public static String[] getFavoriteMovies(){

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, //Table to Query
                null, // all columns
                null, // Columns for the "where" clause //selection
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        String[] resultStrs = new String[getNumberDBRows()];

        if (cursor.moveToFirst()) {
            do {
                Log.i(LOG_TAG, "Retrieving entry position : " + cursor.getColumnIndex(MovieContract.MovieEntry._ID));
                int columnTitle = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                String title = cursor.getString(columnTitle);
                Log.i(LOG_TAG, "Retrieving entry title: " + title);
                int columnYear = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR);
                String year = cursor.getString(columnYear);
                Log.i(LOG_TAG, "Retrieving entry year: " + year);
                int columnRat = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
                String rating = cursor.getString(columnRat);
                Log.i(LOG_TAG, "Retrieving entry rating: " + rating);
                int columnThumb = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
                String thumb = cursor.getString(columnThumb);
                Log.i(LOG_TAG, "Retrieving entry thumb: " + thumb);
                resultStrs[cursor.getPosition()] =  title + "_" + year + "_" + rating + "_" + thumb;
            } while (cursor.moveToNext());
        } else {
            Log.i(LOG_TAG, "No results from Location table!");
        }

        cursor.close();
        db.close();

        return resultStrs;
    }
    */


    //private String [] getFavoriteMoviesFromDB() {
        /* Disable Strict Mode - Temporary Solution */
/*        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] movies = getFavoriteMovies();

        return movies;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnMovieSelectedListener {
        public void onMovieSelected(String s);
    }

    public interface databaseEmpty {
        public void onDatabaseFragment();
    }
*/
    public class MyAdapter extends ArrayAdapter<String> {

        private Context context;
        //final HashMap<String, List<EmotionValues>> values = new HashMap<>();

        private ArrayList<String> values;

        public MyAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.row, values);
            this.context = context;
            this.values = values;
        }

        public int getSize(){
            return values.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.row, parent, false);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView myTitle = (TextView) rowView.findViewById(R.id.text1);
            TextView myDescription = (TextView) rowView.findViewById(R.id.text2);

            //String description[] = values.get(position).split("_");

            imageView.setImageResource(R.drawable.deadpool2);

            String movieValues[] = values.get(position).split(":");

            myTitle.setText(movieValues[0]);
            myDescription.setText("");

            //myTitle.setText(values.get(position));
            //myDescription.setText(values.get(position));
            //myDescription.setText("");
            Log.e("olaaaaaaaaaaaaaaaa " , values.get(position));
            //new ImageLoadTaskFavorites("https://www.subaru-global.com/technology/images/technology/drivetrain_awd/img07.jpg", imageView).execute();

            return rowView;
        }
    }
}

