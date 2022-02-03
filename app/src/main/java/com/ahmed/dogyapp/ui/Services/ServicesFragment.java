package com.ahmed.dogyapp.ui.Services;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ahmed.dogyapp.R;
import com.ahmed.dogyapp.databinding.FragmentServicesBinding;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseAdress;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseServieces;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseTime;
import com.ahmed.dogyapp.ui.FrgsServices.Finish;
import com.ahmed.dogyapp.ui.myworks.MyWorkFragment;
import com.ahmed.dogyapp.ui.rating.RatingshowFragment;
import com.ahmed.dogyapp.ui.wishlist.WishListFragment;


public class ServicesFragment extends Fragment {

    private FragmentServicesBinding binding;

    //TODO nrakah l toolbar
    //TODO nekhdem fazet l services lkol


    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentServicesBinding.inflate(inflater, container, false);


        resetMenu();
        binding.ser.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
        binding.ser.setTextColor(getResources().getColor(R.color.white));


        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(),
                new Fragment[]{new ChooseServieces(), new ChooseTime(), new ChooseAdress(), new Finish()});
        binding.viewPager.setAdapter(pagerAdapter);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        resetMenu();
                        binding.ser.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
                        binding.ser.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        resetMenu();
                        binding.tim.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
                        binding.tim.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        resetMenu();
                        binding.add.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
                        binding.add.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 3:
                        resetMenu();
                        binding.fin.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
                        binding.fin.setTextColor(getResources().getColor(R.color.white));
                        break;
                    default:
                        resetMenu();
                        binding.ser.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
                        binding.ser.setTextColor(getResources().getColor(R.color.white));

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        binding.ser.setOnClickListener(view -> {
            resetMenu();
            binding.ser.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
            binding.ser.setTextColor(getResources().getColor(R.color.white));
            binding.viewPager.setCurrentItem(0);

        });
        binding.tim.setOnClickListener(view -> {
            resetMenu();
            binding.tim.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
            binding.tim.setTextColor(getResources().getColor(R.color.white));
            binding.viewPager.setCurrentItem(1);

        });
        binding.add.setOnClickListener(view -> {
            resetMenu();
            binding.add.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
            binding.add.setTextColor(getResources().getColor(R.color.white));
            binding.viewPager.setCurrentItem(2);

        });
        binding.fin.setOnClickListener(view -> {
            resetMenu();
            binding.fin.setBackground(getActivity().getResources().getDrawable(R.drawable.back_tab));
            binding.fin.setTextColor(getResources().getColor(R.color.white));
            binding.viewPager.setCurrentItem(3);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void resetMenu(){
        binding.ser.setBackgroundColor(0);
        binding.tim.setBackgroundColor(0);
        binding.add.setBackgroundColor(0);
        binding.fin.setBackgroundColor(0);

        binding.ser.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.tim.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.add.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.fin.setTextColor(getResources().getColor(R.color.txt_clr));

    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private final Fragment[] fragments;
        public ScreenSlidePagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}