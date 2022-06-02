package com.dev.e_money.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.e_money.R;
import com.dev.e_money.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDetails extends AppCompatActivity {
    TextView displayid,displayname,displaynumber,displaybalance ;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        setContentView(R.layout.activity_user_details);
        displayid = findViewById(R.id.displayid);
        displayname = findViewById(R.id.displayname);
        displaynumber = findViewById(R.id.displaynumber);
        displaybalance = findViewById(R.id.displaybalance);
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        String userid = (sharedPreferences.getString("userEmail", String.valueOf(MODE_PRIVATE)));
        myRef = database.getReference("users/"+user.getUid());
        displayid.setText(userid);
        loaddata(userid);
    }
    void loaddata(String userid){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user model = dataSnapshot.getValue(user.class);
                displayname.setText(model.getName());
                displaynumber.setText(model.getPhoneno());
                displaybalance.setText(model.getBalance());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UserDetails.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}