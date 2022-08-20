package com.ahmed.dogyapp.USER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.databinding.EditProfileBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    EditProfileBinding binding;

    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference reference;
    StorageReference storageReference;
    private String uid;
    private String pathImage = null;
    private ProfilData profilData = null;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.edit_profile);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", "");

        binding.setImg.setClickable(false);
        binding.setImg.setBackground(getResources().getDrawable(R.drawable.back_gray));

        reference.child("PROFILES").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfilData data = snapshot.getValue(ProfilData.class);
                        if (data == null) return;
                        Glide.with(EditProfile.this).load(data.getImage()).into(binding.user);
                        profilData = data;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

        binding.i.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });



        binding.eUsername.setOnClickListener(view -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);

            final EditText edittext = new EditText(EditProfile.this);
            alert.setTitle("Username");

            alert.setView(edittext);

            edittext.setText(profilData.getUsername());

            alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                //What ever you want to do with the value
                String username = edittext.getText().toString();
                if (!username.equals(profilData.getUsername())){
                    reference.child("PROFILES/"+uid+"/username").setValue(username);
                }

            });

            alert.setNegativeButton("Cancel", (dialog, whichButton) -> {

            });

            alert.show();

        });

        binding.eEmail.setOnClickListener(view -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);

            final EditText edittext = new EditText(EditProfile.this);
            alert.setTitle("Email");

            alert.setView(edittext);

            edittext.setText(profilData.getEmail());

            alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                //What ever you want to do with the value
                String et = edittext.getText().toString();
                if (!et.equals(profilData.getEmail())){
                    reference.child("PROFILES/"+uid+"/email").setValue(et);
                }

            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();

        });

        binding.eNumber.setOnClickListener(view -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);

            final EditText edittext = new EditText(EditProfile.this);
            alert.setTitle("Phone Number");

            alert.setView(edittext);

            edittext.setText(profilData.getPhone());

            alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                //What ever you want to do with the value
                String username = edittext.getText().toString();
                if (!username.equals(profilData.getPhone())){
                    reference.child("PROFILES/"+uid+"/phone").setValue(username);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();

        });

        binding.ePass.setOnClickListener(view -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);

            final EditText edittext = new EditText(EditProfile.this);
            alert.setTitle("Password");

            alert.setView(edittext);

            edittext.setText(profilData.getPassword());

            alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                //What ever you want to do with the value
                String username = edittext.getText().toString();
                if (!username.equals(profilData.getPassword())){
                    reference.child("PROFILES/"+uid+"/password").setValue(username);
                }

            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();

        });

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
            Toast.makeText(EditProfile.this, "Failed Try again", Toast.LENGTH_SHORT).show();
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {

            Toast.makeText(EditProfile.this, "Failed Try again", Toast.LENGTH_SHORT).show();

            //reference.child("PROFILES/"+uid+"/image").setValue();
            //FirebaseMessaging.getInstance().subscribeToTopic("user_"+uid);


        }).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {

            reference.child("PROFILES/"+uid+"/image").setValue(uri.toString());
            Toast.makeText(EditProfile.this, "Photo updated successfuly", Toast.LENGTH_SHORT).show();


        }));



    }


    @SuppressLint("UseCompatLoadingForDrawables")
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
            binding.setImg.setClickable(true);
            binding.setImg.setBackground(getResources().getDrawable(R.drawable.back_icone));

            binding.setImg.setOnClickListener(view -> {
                if (pathImage != null) {
                    uploadImage(pathImage);
                }else {
                    Toast.makeText(this, "Please select image ! ", Toast.LENGTH_SHORT).show();
                    binding.setImg.setClickable(false);
                }
            });

        }
    }

}
