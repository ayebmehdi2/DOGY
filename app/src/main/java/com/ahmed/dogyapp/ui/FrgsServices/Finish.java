package com.ahmed.dogyapp.ui.FrgsServices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ahmed.dogyapp.databinding.FinishFragBinding;
import com.ahmed.dogyapp.databinding.FragmentMyworkBinding;

public class Finish extends Fragment {

    private FinishFragBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FinishFragBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}