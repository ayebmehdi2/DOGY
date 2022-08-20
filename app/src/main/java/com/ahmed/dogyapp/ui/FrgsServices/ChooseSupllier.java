package com.ahmed.dogyapp.ui.FrgsServices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmed.dogyapp.AdapterSupplier;
import com.ahmed.dogyapp.EditeActivity;
import com.ahmed.dogyapp.HomeActivity;
import com.ahmed.dogyapp.Supplier;
import com.ahmed.dogyapp.databinding.ChooseAddresseBinding;
import com.ahmed.dogyapp.databinding.ChooseSupllierBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseSupllier extends Fragment implements AdapterSupplier.Click {

    FirebaseDatabase database;
    DatabaseReference reference;

    ChooseSupllierBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ChooseSupllierBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        AdapterSupplier suppliers = new AdapterSupplier(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recycle.setLayoutManager(layoutManager);
        binding.recycle.setHasFixedSize(true);
        binding.recycle.setAdapter(suppliers);

        binding.govs.setSelection(1);

        binding.govs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String a = adapterView.getItemAtPosition(i).toString();

                if (i == 0){
                    binding.hn.setVisibility(View.VISIBLE);
                    binding.recycle.setVisibility(View.GONE);
                }else {
                    binding.hn.setVisibility(View.GONE);
                    binding.recycle.setVisibility(View.VISIBLE);
                    reference.child("SUPLLIER").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChildren()) {
                                suppliers.swapAdapter(null);
                                binding.hn.setVisibility(View.VISIBLE);
                                binding.recycle.setVisibility(View.GONE);
                                return;
                            }

                            ArrayList<Supplier> sups = new ArrayList<>();
                            for (DataSnapshot shot : snapshot.getChildren()){
                                Supplier sup  = shot.getValue(Supplier.class);
                                if (sup != null){
                                    if (sup.getGov() != null){
                                        if (sup.getGov().equals(a)) sups.add(sup);
                                    }
                                }
                            }

                            if (sups.size() == 0){
                                binding.hn.setVisibility(View.VISIBLE);
                                binding.recycle.setVisibility(View.GONE);
                                return;
                            }

                            binding.hn.setVisibility(View.GONE);
                            binding.recycle.setVisibility(View.VISIBLE);
                            suppliers.swapAdapter(sups);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return binding.getRoot();
    }

    @Override
    public void open(Supplier supplier) {
        Intent i = new Intent(getContext(), EditeActivity.class);
        i.putExtra("id", supplier.getId());
        startActivity(i);
    }

    @Override
    public void select(Supplier supplier) {
        HomeActivity.supplier = supplier;
    }


}
