package com.ahmed.dogyapp.ui.FrgsServices;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.ahmed.dogyapp.HomeActivity;
import com.ahmed.dogyapp.databinding.ChooseServiceBinding;

public class ChooseServieces extends Fragment {

    private ChooseServiceBinding binding;

    private boolean tran = false;
    private  boolean hot = false;
    private boolean vet = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ChooseServiceBinding.inflate(inflater, container, false);

        resetSers();

        binding.tran.setOnClickListener(view -> {
            tran = !tran;
            resetSers();

            click.clickT(tran);
        });

        binding.hot.setOnClickListener(view -> {
            hot = !hot;
            resetSers();

            click.clickH(hot);
        });

        binding.vet.setOnClickListener(view -> {
            vet = !vet;
            resetSers();

            click.clickV(vet);
        });

        return binding.getRoot();
    }

    public void resetSers(){
        if (tran) binding.trainingV.setVisibility(View.VISIBLE); else binding.trainingV.setVisibility(View.GONE);
        if (hot) binding.hotelV.setVisibility(View.VISIBLE); else binding.hotelV.setVisibility(View.GONE);
        if (vet) binding.veterinayV.setVisibility(View.VISIBLE); else binding.veterinayV.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface clickInChooseServices{
        void clickT(boolean t);
        void clickH(boolean h);
        void clickV(boolean v);
    }

    private clickInChooseServices click;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            click = (clickInChooseServices) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}