package com.dev.e_money.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.e_money.DashboardActivity;
import com.dev.e_money.R;
import com.dev.e_money.model.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TransfermoneyActivity extends AppCompatActivity {
    EditText recv_amount, recv_email;
    Button recv_send;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser user;
    private ProgressDialog progressDialog;
    String[] user_uid = new String[2];
    int[] user_balance = new int[2];
    ArrayList<user> userlist = new ArrayList<user>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfermoney);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        getusers();
        recv_amount = findViewById(R.id.recv_amount);
        recv_email = findViewById(R.id.recv_email);
        recv_send = findViewById(R.id.recv_send);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Transferring Money");
        recv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = recv_email.getText().toString().trim();
                String amount = recv_amount.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    recv_email.setError("Invalid Email");
                    recv_email.setFocusable(true);
                } else if (amount.length() == 0) {
                    recv_amount.setError("Amount cannot be empty");
                    recv_amount.setFocusable(true);
                } else {
                    progressDialog.show();
                    if (userlist.size() != 0) {
                        if (getUid(email, amount)) {
                            if(checkbalance(email, amount))
                            {

                                startActivity(new Intent(TransfermoneyActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(TransfermoneyActivity.this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
                            recv_amount.setText("");
                            recv_email.setText("");
                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(TransfermoneyActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
                        recv_amount.setText("");
                        recv_email.setText("");
                    }
                }
            }
        });

    }

    void getusers() {
        myRef = database.getReference("users");
        if (myRef != null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            user model = ds.getValue(user.class);
                            userlist.add(model);
                            Log.e(TAG,"model value : "+model.getEmail());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(TransfermoneyActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    recv_amount.setText("");
                    recv_email.setText("");
                }
            });
        }

    }

    boolean getUid(String Email, String Amount) {
        boolean flag = false;
        for (user u : userlist) {
            if (user.getEmail().equals(u.getEmail())) {
                user_uid[1] = u.getUid();
                user_balance[1] = Integer.parseInt(u.getBalance());
            }
            if (u.getEmail().equals(Email)) {
                user_uid[0] = u.getUid();
                user_balance[0] = Integer.parseInt(u.getBalance());
                flag = true;
            }
        }
        if (flag) {
            return true;
        }
        progressDialog.dismiss();
        Toast.makeText(TransfermoneyActivity.this, "User Doesn't exist in our database.", Toast.LENGTH_SHORT).show();
        recv_amount.setText("");
        recv_email.setText("");
        return false;
    }

    boolean checkbalance(String email, String amount) {
        int amt = Integer.parseInt(amount);
        if (amt < 1) {
            Toast.makeText(TransfermoneyActivity.this, "Cannot transfer money less than 1.", Toast.LENGTH_SHORT).show();
            return false;
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        if (user_balance[1] >= amt) {
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy hh:mm:ss a");
            HashMap<Object, String> hashMap1 = new HashMap<>();
            hashMap1.put("SendFrom", user.getEmail());
            hashMap1.put("SendTo", email);
            hashMap1.put("Amount", amount);
            hashMap1.put("Status", "sent");
            hashMap1.put("Time", ft.format(dNow));
            HashMap<Object, String> hashMap2 = new HashMap<>();
            hashMap2.put("SendFrom", user.getEmail());
            hashMap2.put("SendTo", email);
            hashMap2.put("Amount", amount);
            hashMap2.put("Status", "received");
            hashMap2.put("Time", ft.format(dNow));
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("history");
            reference.child(user_uid[0]).child(timestamp).setValue(hashMap2);
            reference.child(user.getUid()).child(timestamp).setValue(hashMap1);
            AddAmount(user_uid[0],user_balance[0],amt);
            RemoveAmount(user_uid[1],user_balance[1],amt);
            progressDialog.dismiss();
            Toast.makeText(TransfermoneyActivity.this, "Money Transferred", Toast.LENGTH_SHORT).show();
            recv_amount.setText("");
            recv_email.setText("");
            return true;
            // reference.child(Objects.requireNonNull(user.getEmail())).setValue(hashMap2);
        } else {
            progressDialog.dismiss();
            Toast.makeText(TransfermoneyActivity.this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
     void AddAmount(String uid,int bal, int amt) {
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference Ref;
         Ref = database.getReference("users/"+uid+"/balance");
         Ref.setValue(""+(bal+amt));
    }

    void RemoveAmount(String uid,int bal, int amt) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref;
        Ref = database.getReference("users/"+uid+"/balance");
        Ref.setValue(""+(bal-amt));
    }
}