package com.example.jp.projectofinal.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.jp.projectofinal.activities.MovieInfoActivity;
import com.example.jp.projectofinal.R;
import com.example.jp.projectofinal.asyncTasks.ImageLoadTaskSuggestions;
import com.example.jp.projectofinal.dataModels.MovieInfo;

import java.util.ArrayList;

/**
 * Created by TiagoHenriques on 13/10/2017.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionListFragment extends Fragment implements View.OnClickListener {

    private OnMovieSelectedListener mListener;
    private ListView listView;
    private MyAdapter myAdapter;
    ArrayList<MovieInfo> list;

    public SuggestionListFragment(ArrayList<MovieInfo> list) {
        this.list = list;
    }
    public SuggestionListFragment() {
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion_list, container, false);



        myAdapter = new MyAdapter(
                getActivity(),
                new ArrayList<String>());

        myAdapter.clear();
        for (MovieInfo movie : list ) {
            Log.d("DAY_ENTRY", movie.getId());
            myAdapter.add(movie.getTitle()+":"+movie.getVote_average()+":"+movie.getRelease_date()+ ":" +movie.getBackdrop_path());
        }

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = myAdapter.getItem(position).split(":")[0];
                Log.e("nome filme", s);
                Context context = view.getContext();
                Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
                mListener.onMovieSelected(s);

            }
        });

        return view;
    }


    public interface OnMovieSelectedListener {
        public void onMovieSelected(String s);
    }

    @Override
    public void onClick(View v) {

    }

    // Container Activity must implement this interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieSelectedListener) {
            mListener = (OnMovieSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDaySelectedListener");
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {
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
            Log.e("irl", "http://image.tmdb.org/t/p/w185//"+ description[3]);

            new ImageLoadTaskSuggestions("http://image.tmdb.org/t/p/w185"+ description[3], imageView).execute();

            myTitle.setText(description[0] + " - " + description[2].split("-")[0]);
            myDescription.setText(description[1]);


            return rowView;
        }
    }
}
