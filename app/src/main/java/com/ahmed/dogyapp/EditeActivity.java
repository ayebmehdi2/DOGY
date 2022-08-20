package com.ahmed.dogyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmed.dogyapp.databinding.EditeScreenBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditeActivity extends AppCompatActivity implements AdapterPhotos.Click {


    private EditeScreenBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    String uniqueID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.edite_screen);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        uniqueID = getIntent().getStringExtra("id");

            reference.child("SUPLLIER").child(uniqueID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Supplier supplier = snapshot.getValue(Supplier.class);
                    Glide.with(EditeActivity.this).load(supplier.getImage()).into(binding.photo);
                    binding.name.setText(supplier.getName());
                    binding.lastname.setText(supplier.getLastname());
                    binding.phonenumber.setText(supplier.getPhone());
                    binding.email.setText(supplier.getEmail());
                    binding.desc.setText(supplier.getDescription());
                    binding.address.setText(supplier.getAdress());
                    binding.spec.setText(supplier.getSpecialty());
                    binding.gov.setText(supplier.getGov());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        AdapterPhotos photos = new AdapterPhotos(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.rec.setLayoutManager(layoutManager);
        binding.rec.setHasFixedSize(true);
        binding.rec.setAdapter(photos);

        reference.child("SUPLLIER").child(uniqueID).child("PHOTOS").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChildren()) {
                            photos.swapAdapter(null);
                            binding.hn.setVisibility(View.VISIBLE);
                            binding.rec.setVisibility(View.GONE);
                            return;
                        }
                        ArrayList<Photo> data = new ArrayList<>();
                        for (DataSnapshot shot : snapshot.getChildren()){
                            data.add(shot.getValue(Photo.class));
                        }

                        binding.hn.setVisibility(View.GONE);
                        binding.rec.setVisibility(View.VISIBLE);
                        photos.swapAdapter(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void open(Photo photo) {
        Intent i = new Intent(EditeActivity.this, AddPhotoActivity.class);
        i.putExtra("id", uniqueID);
        i.putExtra("idphoto", photo.getId());
        startActivity(i);
    }
}
