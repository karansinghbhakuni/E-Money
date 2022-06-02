package com.dev.e_money;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.dev.e_money.view.HistoryActivity;
import com.dev.e_money.view.TransfermoneyActivity;
import com.dev.e_money.view.UserDetails;

public class DashboardActivity extends AppCompatActivity {
    TextView displayname, logoutview;
    LottieAnimationView detailview, labview, historyview, sensorview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        displayname = findViewById(R.id.display_name);
        logoutview = findViewById(R.id.txtlogout);

        //view
        detailview = findViewById(R.id.detail_view);
        labview = findViewById(R.id.lab_view);
        historyview = findViewById(R.id.history_view);
        //sensorview = findViewById(R.id.sensor_view);


        logoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                checkuserstatus();
            }
        });
        detailview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(DashboardActivity.this, UserDetails.class));
            }
        });
        labview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(DashboardActivity.this, TransfermoneyActivity.class));
            }
        });
        historyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
            }
        });
    }

    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }

    void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        String username = (sharedPreferences.getString("userEmail", String.valueOf(MODE_PRIVATE)));
        displayname.setText(username);
    }

    private void checkuserstatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("loginCounter", Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        if (counter) {
            loadSavedData();
        } else {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }
}