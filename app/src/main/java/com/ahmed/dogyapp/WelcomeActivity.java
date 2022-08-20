package com.ahmed.dogyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.AUTH.LoginActivity;
import com.ahmed.dogyapp.databinding.WelcomeScreenBinding;

public class WelcomeActivity extends AppCompatActivity {

    WelcomeScreenBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.welcome_screen);

        binding.starte.setOnClickListener(view ->
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class)));

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isWelcomed", true);
        editor.apply();

    }
}
