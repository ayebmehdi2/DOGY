package com.ahmed.dogyapp.ui.FrgsServices;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.databinding.ChooseTimeBinding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class ChooseTime extends Fragment implements SlyCalendarDialog.Callback {

    private ChooseTimeBinding binding;
    private String StartTime = null, EndTime = null;
    private String t = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ChooseTimeBinding.inflate(inflater, container, false);

        binding.selectDate.setOnClickListener(view -> new SlyCalendarDialog()
                .setSingle(false)
                .setCallback(ChooseTime.this)
                .show(getActivity().getSupportFragmentManager(), "TAG_SLYCALENDAR"));

        binding.date.setText(t);

        binding.start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StartTime = adapterView.getItemAtPosition(i).toString();
                click.startTime(StartTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EndTime = adapterView.getItemAtPosition(i).toString();
                click.endTime(EndTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCancelled() {
    }

    public interface ClickInTime{
        void startTime(String st);
        void endTime(String et);
        void startDate(String sd);
        void endDate(String ed);
    }

    private ClickInTime click;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            click = (ClickInTime) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
        String endDate;
        String startDate;
        if (firstDate == null && secondDate == null) {
            Toast.makeText(getActivity(), "No date selected please try again !", Toast.LENGTH_SHORT).show();
        }else if (firstDate != null && secondDate == null){
            startDate = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(firstDate.getTime());
            click.startDate(startDate);
            click.endDate(null);
            t = startDate;
            binding.date.setText(t);
        }else if (firstDate == null){
            endDate = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(secondDate.getTime());
            click.startDate(null);
            click.endDate(endDate);
            t = endDate;
            binding.date.setText(t);
        }else {
            startDate = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(firstDate.getTime());
            endDate = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(secondDate.getTime());
            click.startDate(startDate);
            click.endDate(endDate);
            t = startDate + " - " + endDate;
            binding.date.setText(t);
        }
    }

}