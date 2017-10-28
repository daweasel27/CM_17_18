package com.example.jp.projectofinal.db;

import com.example.jp.projectofinal.dataModels.ToFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jota on 10/28/2017.
 */

public class SendDataToFirebase {
    public SendDataToFirebase() {

    }

    public void send(ToFirebase tf){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("movie");
        String movieId = mDatabase.push().getKey();
        mDatabase.child(movieId).setValue(tf);

    }
}
