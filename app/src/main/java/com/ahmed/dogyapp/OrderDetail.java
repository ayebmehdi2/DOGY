package com.ahmed.dogyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.databinding.OrderDetaileBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetail extends AppCompatActivity {

    OrderDetaileBinding binding;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.order_detaile);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        uid = preferences.getString("uid", null);

        String id = getIntent().getStringExtra("id");

        FirebaseDatabase.getInstance().getReference().child("PROFILES/"+uid+"/ORDERS/"+id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ODER O = snapshot.getValue(ODER.class);
                if (O == null) return;
                FirebaseDatabase.getInstance().getReference().child("SUPLLIER/"+ O.getSupplier())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Supplier s = snapshot.getValue(Supplier.class);
                                if (s == null) return;
                                binding.email.setText(s.getEmail());
                                binding.name.setText(s.getName());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                Glide.with(OrderDetail.this).load(O.getService()).into(binding.servicesDe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
