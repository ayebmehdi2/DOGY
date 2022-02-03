package com.ahmed.dogyapp.AUTH;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.ProfilData;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.databinding.VerificationEmailBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckEmailActivity extends AppCompatActivity {

    private ProgressDialog PD;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    VerificationEmailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.verification_email);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();
        if (user == null) return;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String uid = preferences.getString("uid", null);
        reference.child("PROFILES").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfilData data = snapshot.getValue(ProfilData.class);
                        if (data == null) return;
                        Glide.with(CheckEmailActivity.this).load(data.getImage()).into(binding.user);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

        binding.signinn.setOnClickListener(view -> startActivity(new Intent(
                CheckEmailActivity.this, LoginActivity.class)));

        PD.show();
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        PD.dismiss();
                        binding.continu.setOnClickListener(view -> {
                            user.reload();
                            boolean emailVerified = user.isEmailVerified();
                            if (emailVerified){
                                startActivity(new Intent(CheckEmailActivity.this, WelcomeBack.class));
                            }else {
                                Toast.makeText(this, "Email not verified ! ", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                });

    }


}
