package com.tatonimatteo.waterhealth.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tatonimatteo.waterhealth.fragment.stations.list.StationListFragment;
import com.tatonimatteo.waterhealth.fragment.stations.map.StationMapsFragment;

public class HomeFragementAdapter extends FragmentStateAdapter {
    public HomeFragementAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new StationMapsFragment();
            default:
                return new StationListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
