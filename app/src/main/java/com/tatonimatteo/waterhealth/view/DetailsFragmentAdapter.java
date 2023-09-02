package com.tatonimatteo.waterhealth.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tatonimatteo.waterhealth.fragment.details.StationData;
import com.tatonimatteo.waterhealth.fragment.details.StationInfo;

public class DetailsFragmentAdapter extends FragmentStateAdapter {
    public DetailsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new StationData();
        }
        return new StationInfo();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
