package com.tatonimatteo.waterhealth.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tatonimatteo.waterhealth.fragment.stations.list.StationListFragment;
import com.tatonimatteo.waterhealth.fragment.stations.map.StationMapsFragment;

public class HomeFragmentAdapter extends FragmentStateAdapter {
    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new StationMapsFragment();
        }
        return new StationListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
