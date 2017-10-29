package com.example.jp.projectofinal.fragments;

import android.content.Context;
import android.os.AsyncTask;
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

    public ListView listView;
    public MyAdapter myAdapter;
    public String[] resultStrs;

    public final List<ToFirebase> mJournalEntries = new ArrayList<>();
    public final HashMap<String, List<ToFirebase>> listMovies = new HashMap<>();
    public final HashMap<String, List<EmotionValues>> listFinal = new HashMap<>();


    public MovieProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movierofile_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        getData();

        return view;
    }

    public void getData() {
        Log.e("getData", "getData");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("movie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                class data extends AsyncTask<DataSnapshot, Void, Void> {
                    @Override
                    protected Void doInBackground(DataSnapshot... voids) {
                        for (DataSnapshot noteSnapshot: voids[0].getChildren()){
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
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        // Start Activity C
                        final ArrayList<String> a = new ArrayList<>();
                        for (String dayEntry : listFinal.keySet()) {
                            a.add(dayEntry);
                        }

                        myAdapter = new MyAdapter(
                                getActivity(), // The current context (this activity)
                                new ArrayList<String>());
                        final String[] daysLabels = resultStrs;

                        myAdapter.clear();

                        for (String dayEntry : daysLabels) {

                            myAdapter.add(dayEntry);

                        }


                        listView.setAdapter(myAdapter);
                    }
                }

                new data().execute(dataSnapshot);

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
            listFinal.put(pair.getKey(),listE);
        }

        resultStrs = new String[listFinal.size()];

        int i=0;
        for(String ke : listFinal.keySet()){
            Log.e("final result -",ke);
            String cenas="";
            for(EmotionValues e: listFinal.get(ke)){
                Log.e("TESTE - " , e.getName() + " --- " + e.getValue());
                cenas += "@" + e.getName() + "@" + e.getValue();
            }
            resultStrs[i] = ke +cenas;
            Log.e("UIUIUI", resultStrs[i]);
            i++;
        }

        Log.d("tropa", Integer.toString(resultStrs.length));

        writeToFile(resultStrs, getContext());

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


    @Override
    public void onClick(View v) {

    }

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

            String movieValues[] = values.get(position).split("@");

            myTitle.setText(movieValues[0].split("-")[0]);
            myDescription.setText(movieValues[1] + " - " + movieValues[2] + "\n"
                                + movieValues[3] + " - " + movieValues[4] + "\n"
                                + movieValues[5] + " - " + movieValues[6] + "\n"
                                + movieValues[7] + " - " + movieValues[8] + "\n");

            //myTitle.setText(values.get(position));
            //myDescription.setText(values.get(position));
            //myDescription.setText("");
            Log.e("olaaaaaaaaaaaaaaaa " , values.get(position));
            //new ImageLoadTaskFavorites("https://www.subaru-global.com/technology/images/technology/drivetrain_awd/img07.jpg", imageView).execute();

            return rowView;
        }
    }




    }


