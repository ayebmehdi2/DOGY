package com.ahmed.dogyapp.USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.HomeActivity;
import com.ahmed.dogyapp.OrdersActivity;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.SplachActivity;
import com.ahmed.dogyapp.databinding.AccountScreenBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    AccountScreenBinding binding;
    private String uid;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.account_screen);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", "");

        reference.child("PROFILES").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfilData data = snapshot.getValue(ProfilData.class);
                        if (data == null) return;
                        Glide.with(AccountActivity.this).load(data.getImage()).into(binding.user);
                        binding.username.setText(data.getUsername());
                        binding.email.setText(data.getEmail());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

        binding.editProfile.setOnClickListener(view -> {
            startActivity(new Intent(this, EditProfile.class));
        });

        binding.editAdress.setOnClickListener(view -> {
            startActivity(new Intent(this, EditAdresse.class));
        });

        binding.orderHistory.setOnClickListener(view -> {
            startActivity(new Intent(this, OrdersActivity.class));
        });

        binding.logout.setOnClickListener(view -> {
            SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).edit();
            preference.putString("uid", null);
            preference.apply();
            //FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + uid).addOnSuccessListener(aVoid ->
                    Toast.makeText(getApplicationContext(), "Unsubscribe Success", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AccountActivity.this, SplachActivity.class));
        });

        binding.back.setOnClickListener(view -> startActivity(new Intent(AccountActivity.this, HomeActivity.class)));
    }
}
