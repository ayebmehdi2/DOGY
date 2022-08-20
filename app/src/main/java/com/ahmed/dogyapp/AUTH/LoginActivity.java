package com.ahmed.dogyapp.AUTH;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.HomeActivity;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.USER.ProfilData;
import com.ahmed.dogyapp.databinding.SigninBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog PD;
    GoogleSignInClient mGoogleSignInClient;
    SigninBinding binding;
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signin);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            auth.getCurrentUser().reload();
            boolean emailVerified = auth.getCurrentUser().isEmailVerified();
            if (emailVerified){
                startActivity(new Intent(this, HomeActivity.class));
            }
        }


        binding.signin.setOnClickListener(view -> {

            final String email = binding.addrs.getText().toString();
            final String password = binding.passw.getText().toString();

            try {

                if (password.length() > 0 && email.length() > 0) {
                    PD.show();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, task -> {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(
                                            LoginActivity.this,
                                            /*task.getResult().toString()*/"Login Failed",
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                    preference.putString("uid", auth.getUid());
                                    preference.apply();

                                    //FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());

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

        binding.google.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("113631404027-dgngq56gpakul9n8dpibscm4p33ghtq5.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

            signIn();
        });

        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager lm = LoginManager.getInstance();

        lm.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook:onError", exception);

                    }
                });

        binding.fcb.setOnClickListener(view -> {
            String EMAIL = "email";
            lm.logIn(LoginActivity.this, Collections.singletonList(EMAIL));
        });


    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            if (user == null) {
                                Toast.makeText(LoginActivity.this, "Login Failed \n Try Again ! ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                            preference.putString("uid", auth.getUid());
                            preference.putString("name", user.getDisplayName());
                            preference.apply();
                            FirebaseDatabase.getInstance().getReference().child("PROFILES/"+auth.getUid()).setValue(new ProfilData(auth.getUid(),
                                    user.getDisplayName(), user.getEmail(), user.getUid(), user.getPhotoUrl().toString(), user.getPhoneNumber()));
                            //FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1412);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1412) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        if (user == null) {
                            Toast.makeText(LoginActivity.this, "Login Failed \n Try Again ! ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        preference.putString("uid", auth.getUid());
                        preference.putString("name", user.getDisplayName());
                        preference.apply();
                        FirebaseDatabase.getInstance().getReference().child("PROFILES/"+auth.getUid()).setValue(new ProfilData(auth.getUid(),
                                user.getDisplayName(), user.getEmail(), user.getUid(), user.getPhotoUrl().toString(), user.getPhoneNumber()));
                        //FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Login Failed \n Try Again ! ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
