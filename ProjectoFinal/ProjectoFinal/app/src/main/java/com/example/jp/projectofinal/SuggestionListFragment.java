package com.example.jp.projectofinal;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
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

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by TiagoHenriques on 13/10/2017.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionListFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private MyAdapter myAdapter;
    private OnDaySelectedListener mListener;

    public SuggestionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion_list, container, false);

        //Resources res = getResources();

        //titles = res.getStringArray(R.array.cenas);

        //MyAdapter myAdapter = new MyAdapter();
        myAdapter = new MyAdapter(
                getActivity(), // The current context (this activity)
                new ArrayList<String>());

        final String[] daysLabels = getTheWeatherForecast();

        // IMP...
        myAdapter.clear();
        for (String dayEntry : daysLabels) {
            Log.d("DAY_ENTRY", dayEntry);
            myAdapter.add(dayEntry);
        }

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = myAdapter.getItem(position);
                Context context = view.getContext();
                Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
                mListener.onDaySelected(s);
            }
        });

        return view;
    }

    // Container Activity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDaySelectedListener) {
            mListener = (OnDaySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDaySelectedListener");
        }
    }

    private String [] getTheWeatherForecast() {
        /* Disable Strict Mode - Temporary Solution */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Do not forget to add INTERNET permissions

        // Parser
        MoviesParser parser = new MoviesParser();
        String[] days = new String[0];
        try {
            days =   parser.getMealDataFromJson( parser.getMealsInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return days;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnDaySelectedListener {
        public void onDaySelected(String s);
    }

    public class MyAdapter extends ArrayAdapter<String> {

        private final String SANTIAGO = "Refeitório de Santiago";
        private final String CRASTO = "Refeitório do Crasto";
        private final String SNACK_BAR = "Snack-Bar/Self";
        private Context context;
        private ArrayList<String> values;

        public MyAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row, parent, false);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView myTitle = (TextView) rowView.findViewById(R.id.text1);
            TextView myDescription = (TextView) rowView.findViewById(R.id.text2);

            String description[] = values.get(position).split(":");

            switch (description[0]){
                case SANTIAGO:
                    imageView.setImageResource(R.drawable.deadpool2);
                    break;
                case CRASTO:
                    imageView.setImageResource(R.drawable.deadpool2);
                    break;
                case SNACK_BAR:
                    imageView.setImageResource(R.drawable.deadpool2);
                    break;
                default:
                    imageView.setImageResource(R.drawable.deadpool2);
            }

            myTitle.setText(description[0] + " - " + description[2]);
            myDescription.setText(description[1]);

            // TODO - Quando estiver encerrado, fazer aparecer um TOAST a dizer encerrado

        /*
        textView.setText(values.get(position));
        // Change the icon for Windows and iPhone
        String s = EmentasUAParser.getMeal(position);
        */

            return rowView;
        }
    }


}
