package com.ahmed.dogyapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ahmed.dogyapp.AUTH.LoginActivity;

public class SplachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splach_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!isNetworkConnected()){
            new AlertDialog.Builder(this)
                    .setTitle("Connexion failed")
                    .setMessage("Please check your connexion and try again !")

                    .setPositiveButton("Ok", (dialog, which) -> {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        new Handler().postDelayed(() -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplachActivity.this);
            boolean w = preferences.getBoolean("isWelcomed", false);

            if (w){
                startActivity(new Intent(SplachActivity.this, LoginActivity.class));
            }else {
                startActivity(new Intent(SplachActivity.this, WelcomeActivity.class));
            }

        }, 2000);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
