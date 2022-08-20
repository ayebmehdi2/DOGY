package com.ahmed.dogyapp.USER;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.databinding.EditAdressBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAdresse extends AppCompatActivity {


    EditAdressBinding binding;
    public static String adress = null, city = null, state = null, country = null;

    public boolean checkStr(String a){
        return a.length() > 0;
    }

    private String uid;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.edit_adress);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", "");

        reference.child("PROFILES").child(uid).child("ADRESS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Adress a = snapshot.getValue(Adress.class);
                if (a != null) {

                    binding.adress.setText(a.getAdress());
                    binding.city.setText(a.getCity());
                    binding.state.setText(a.getState());
                    binding.country.setText(a.getCountry());

                }

                binding.save.setOnClickListener(view -> {

                    adress = binding.adress.getText().toString();
                    city = binding.city.getText().toString();
                    state = binding.city.getText().toString();
                    country = binding.country.getText().toString();

                    if (checkStr(adress) || checkStr(city) || checkStr(state) || checkStr(country)){
                        Adress A = new Adress(adress, city, state, country);
                        reference.child("PROFILES").child(uid).child("ADRESS").setValue(A);
                        Toast.makeText(EditAdresse.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(EditAdresse.this, "Fill all the form", Toast.LENGTH_SHORT).show();
                    }

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.cancel.setOnClickListener(view -> {
            finish();
        });



    }
}
