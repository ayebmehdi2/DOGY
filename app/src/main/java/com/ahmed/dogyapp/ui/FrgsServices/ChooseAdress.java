package com.ahmed.dogyapp.ui.FrgsServices;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.ahmed.dogyapp.USER.Adress;
import com.ahmed.dogyapp.databinding.ChooseAddresseBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ChooseAdress extends Fragment {

    private ChooseAddresseBinding binding;

    FirebaseDatabase database;
    private String uid;
    DatabaseReference reference;

    public static EditText adress, city, state, country, number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ChooseAddresseBinding.inflate(inflater, container, false);

        adress = binding.adress;
        city = binding.city;
        state = binding.state;
        country = binding.country;
        number = binding.number;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        uid = preferences.getString("uid", "");


        reference.child("PROFILES").child(uid).child("ADRESS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Adress ad = snapshot.getValue(Adress.class);
                if (ad==null) return;
                adress.setText(ad.getAdress());
                state.setText(ad.getState());
                city.setText(ad.getCity());
                country.setText(ad.getCountry());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("PROFILES").child(uid).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String st = snapshot.getValue(String.class);
                if (st == null) return;
                if (st.length() <= 0) return;
                number.setText(st);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}