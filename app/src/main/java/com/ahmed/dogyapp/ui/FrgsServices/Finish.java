package com.ahmed.dogyapp.ui.FrgsServices;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.ahmed.dogyapp.HomeActivity;
import com.ahmed.dogyapp.USER.ProfilData;
import com.ahmed.dogyapp.databinding.FinishFragBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Finish extends Fragment {

    private FinishFragBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FinishFragBinding.inflate(inflater, container, false);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = preferences.getString("uid", "");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("PROFILES").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfilData data = snapshot.getValue(ProfilData.class);
                        if (data == null) return;
                        Glide.with(getActivity()).load(data.getImage()).into(binding.userimg);
                        binding.username.setText(data.getUsername());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });


        binding.adress.setText(HomeActivity.service.getAdress());

        if (HomeActivity.service.isHotel()){
            binding.serHot.setVisibility(View.VISIBLE);
            binding.serHotV.setVisibility(View.VISIBLE);
        }else {
            binding.serHot.setVisibility(View.GONE);
            binding.serHotV.setVisibility(View.GONE);
        }

        if (HomeActivity.service.isVetirinary()){
            binding.serVet.setVisibility(View.VISIBLE);
            binding.serVetV.setVisibility(View.VISIBLE);
        }else {
            binding.serVet.setVisibility(View.GONE);
            binding.serVetV.setVisibility(View.GONE);
        }

        if (HomeActivity.service.isTraning()){
            binding.serTrain.setVisibility(View.VISIBLE);
            binding.serTrainV.setVisibility(View.VISIBLE);
        }else {
            binding.serTrain.setVisibility(View.GONE);
            binding.serTrainV.setVisibility(View.GONE);
        }

        String date_time = HomeActivity.service.getDate() + "\n" + HomeActivity.service.getTime();
        binding.dateTime.setText(date_time);

        HomeActivity.bitmap = loadBitmapFromView(binding.finishPage);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Bitmap loadBitmapFromView(View v) {
        int specWidth = View.MeasureSpec.makeMeasureSpec(900 /* any */, View.MeasureSpec.EXACTLY);
        v.measure(specWidth, specWidth);
        int questionWidth = v.getMeasuredWidth();
        Bitmap b = Bitmap.createBitmap( questionWidth, questionWidth, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }


}