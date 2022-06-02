package com.dev.e_money.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.e_money.R;
import com.dev.e_money.model.history;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyHolder>{
    Context context;
    ArrayList<history> userhistory;
    public AdapterHistory(Context context, ArrayList<history> userhistory) {
        this.context = context;
        this.userhistory= userhistory;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_events,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String historytime=userhistory.get(i).getTime();
        String historyfrom=userhistory.get(i).getSendFrom();
        String historyto=userhistory.get(i).getSendTo();
        String historyamount=userhistory.get(i).getAmount();
        String historystatus=userhistory.get(i).getStatus();


        //set data
        myHolder.historyDate.setText(historytime);
        myHolder.historyamount.setText(historyamount);
        myHolder.historyfrom.setText(historyfrom);
        myHolder.historyto.setText(historyto);
        myHolder.historystatus.setText(historystatus);
    }

    @Override
    public int getItemCount() {
        return userhistory.size();
    }


    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        TextView historyDate,historyTime,historyfrom,historyto,historystatus,historyamount;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            historyDate=itemView.findViewById(R.id.historyDate);
            historyfrom=itemView.findViewById(R.id.historyfrom);
            historyto=itemView.findViewById(R.id.historyTo);
            historystatus=itemView.findViewById(R.id.historyStatus);
            historyamount=itemView.findViewById(R.id.historyAmount);
        }
    }
}
