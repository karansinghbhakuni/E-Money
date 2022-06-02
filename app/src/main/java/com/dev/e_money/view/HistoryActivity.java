package com.dev.e_money.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.dev.e_money.R;
import com.dev.e_money.adapter.AdapterHistory;
import com.dev.e_money.model.history;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    FirebaseUser user;
    ArrayList<history> userhistory;
    AdapterHistory adapterhistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //recycler view and its prop
        recyclerView=findViewById(R.id.userhistory);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("history/"+user.getUid());
        loadPosts();
    }
    void loadPosts(){
        final int[] i = {0};
        if(myRef!=null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userhistory = new ArrayList<history>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            history model = ds.getValue(history.class);
                            Log.e(TAG, "Value is: " + model.getStatus());
                            userhistory.add(model);
                        }
                        adapterhistory = new AdapterHistory(HistoryActivity.this, userhistory);

                        //set adapter to recycler view
                        recyclerView.setAdapter(adapterhistory);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //in case of error
                    //  Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}