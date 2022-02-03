package com.ahmed.dogyapp.ui.FrgsServices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ahmed.dogyapp.databinding.ChooseTimeBinding;
import com.ahmed.dogyapp.databinding.FragmentMyworkBinding;

public class ChooseTime extends Fragment {

    private ChooseTimeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ChooseTimeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}