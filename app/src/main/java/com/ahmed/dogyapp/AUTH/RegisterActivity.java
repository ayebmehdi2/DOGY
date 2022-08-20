package com.ahmed.dogyapp.AUTH;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.USER.ProfilData;
import com.ahmed.dogyapp.databinding.SignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private String username, email, password;
    private FirebaseAuth auth;
    private ProgressDialog PD;
    private DatabaseReference reference;
    private String pathImage = null;
    private String img = "https://firebasestorage.googleapis.com/v0/b/dogy-3c9e4.appspot.com/o/ic_profile_icone.png?alt=media&token=b619493c-210d-4abe-8db1-909688296a94";
    FirebaseStorage storage;
    StorageReference storageReference;
    SignupBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = DataBindingUtil.setContentView(this, R.layout.signup);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        binding.back.setOnClickListener(view -> finish());


        binding.image.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();


        binding.signup.setOnClickListener(view -> {
            email = binding.email.getText().toString();
            password = binding.password.getText().toString();
            username = binding.username.getText().toString();


            try {

                if (!(password.length() > 0 )){
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_LONG).show();
                    return;
                }



                if (!(email.length() > 0 )){
                    Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_LONG).show();
                    return;
                }



                PD.show();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                RegisterActivity.this, task -> {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "You have already account with this email ! ",
                                                Toast.LENGTH_LONG).show();
                                        PD.dismiss();

                                    } else {

                                        if (pathImage==null){

                                            SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this).edit();
                                            preference.putString("uid", auth.getUid());
                                            preference.putString("name", username);
                                            preference.apply();
                                            reference.child("PROFILES/"+auth.getUid()).setValue(new ProfilData(auth.getUid(),
                                                    username, email, password, img, ""));
                                            //FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());
                                            PD.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, CheckEmailActivity.class);
                                            startActivity(intent);
                                        }else {
                                            uploadImage(pathImage);
                                        }



                                    }

                                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            if (data.getData() == null) return;
            pathImage = data.getData().toString();
            binding.user.setImageURI(data.getData());
        }
    }


    private void uploadImage(String filePath) {

        if (filePath == null) return;
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(filePath));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (bitmap == null){
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {

            SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(
                    RegisterActivity.this).edit();
            preference.putString("uid", auth.getUid());
            preference.putString("name", username);
            preference.apply();
            reference.child("PROFILES/"+auth.getUid()).setValue(new ProfilData(auth.getUid(),
                    username, email, password, img, ""));
            //FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());
            PD.dismiss();

            Intent intent = new Intent(RegisterActivity.this, CheckEmailActivity.class);
            startActivity(intent);

        }).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = uri.toString();
            SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(
                    RegisterActivity.this).edit();
            preference.putString("uid", auth.getUid());
            preference.putString("name", username);
            preference.putString("img", pathImage);
            preference.apply();
            reference.child("PROFILES/"+auth.getUid()).setValue(new ProfilData(auth.getUid(),
                    username, email, password, url, ""));
            //FirebaseMessaging.getInstance().subscribeToTopic("user_"+auth.getUid());
            PD.dismiss();
            Intent intent = new Intent(RegisterActivity.this, CheckEmailActivity.class);
            startActivity(intent);
        }));


    }


    /*
    public void sendEmail(String address){
        try {

            Log.i(getClass().getSimpleName(), "send  task - start");

            String uniqueID = UUID.randomUUID().toString();

            String subject = "[DOGY] Verify your identity";
            String emailtext = " Your code of Verification is " + uniqueID;

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { address });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext);


            ArrayList<Uri> uris = new ArrayList<Uri>();
            Uri uriList = Uri.fromFile(orderListFile);
            uris.add(uriList);

            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);


            this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        }
        catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    */

}