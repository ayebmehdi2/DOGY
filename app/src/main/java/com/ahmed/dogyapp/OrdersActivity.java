package com.ahmed.dogyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity implements AdapterOrders.Click {


    String uid;
    ArrayList<String> orders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        uid = preferences.getString("uid", null);

        orders = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recy_orders);
        AdapterOrders adapterOrders = new AdapterOrders(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterOrders);


        FirebaseDatabase.getInstance().getReference().child("PROFILES/" + uid + "/ORDERS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        adapterOrders.swapAdapter(null);
                        orders.clear();

                        if (!snapshot.hasChildren()) return;

                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            orders.add(snapshot1.getKey());
                        }

                        adapterOrders.swapAdapter(orders);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void open(String photo) {
        Intent i = new Intent(this, OrderDetail.class);
        i.putExtra("id", photo);
        startActivity(i);
    }
}
