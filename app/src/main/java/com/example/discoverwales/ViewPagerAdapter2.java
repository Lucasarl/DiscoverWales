package com.example.discoverwales;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.discoverwales.fragments.InformationFragment1;
import com.example.discoverwales.fragments.ReviewsFragment1;
import com.example.discoverwales.fragments.VirtualTourFragment1;

public class ViewPagerAdapter2 extends FragmentStateAdapter {
    public ViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new InformationFragment1();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
