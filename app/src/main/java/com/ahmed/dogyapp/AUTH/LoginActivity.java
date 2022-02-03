package com.ahmed.dogyapp.AUTH;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.HomeActivity;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.databinding.SigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog PD;

    SigninBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signin);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();

        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
        }


        binding.signin.setOnClickListener(view -> {

            final String email = binding.addrs.getText().toString();
            final String password = binding.passw.getText().toString();

            try {

                if (password.length() > 0 && email.length() > 0) {
                    PD.show();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(
                                                LoginActivity.this,
                                                /*task.getResult().toString()*/"Login Failed",
                                                Toast.LENGTH_LONG).show();
                                    } else {

                                        SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                        preference.putString("uid", auth.getUid());
                                        preference.apply();

                                        FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());


                                        Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
                                        Intent intent2 = new Intent(LoginActivity.this, CheckEmailActivity.class);
                                        boolean emailVerified = auth.getCurrentUser().isEmailVerified();
                                        if (emailVerified){
                                            startActivity(intent1);
                                        }else {
                                            startActivity(intent2);
                                        }

                                        finish();

                                    }
                                    PD.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            "Fill All Fields",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.up.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.frgt.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 0)));

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
