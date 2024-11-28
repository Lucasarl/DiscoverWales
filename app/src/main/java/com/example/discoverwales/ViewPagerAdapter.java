package com.example.discoverwales;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.discoverwales.fragments.InformationFragment1;
import com.example.discoverwales.fragments.ReviewsFragment1;
import com.example.discoverwales.fragments.VirtualTourFragment1;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new InformationFragment1();
            case 1:
                return new ReviewsFragment1();
            case 2:
                return new VirtualTourFragment1();
            default:
                return new InformationFragment1();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
