package com.tatonimatteo.waterhealth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.fragment.stations.list.StationListFragment;
import com.tatonimatteo.waterhealth.fragment.stations.map.StationMapsFragment;

public class Stations extends Fragment {

    private final int ANIMATION_DURATION = 300;
    private int selectedTabIndex = 1;
    private LinearLayout listLayout;
    private ImageView listIcon;
    private TextView listText;
    private LinearLayout mapLayout;
    private ImageView mapIcon;
    private TextView mapText;
    private StationsViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        return inflater.inflate(R.layout.stations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listLayout = view.findViewById(R.id.listLayout);
        listIcon = view.findViewById(R.id.listIcon);
        listText = view.findViewById(R.id.listText);

        mapLayout = view.findViewById(R.id.mapLayout);
        mapIcon = view.findViewById(R.id.mapIcon);
        mapText = view.findViewById(R.id.mapText);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.listContainer, StationListFragment.class, null)
                .commit();

        for (int i = 1; i <= 2; i++) {
            animateTab(i, i == selectedTabIndex);
        }

        listLayout.setOnClickListener(v -> {
            if (selectedTabIndex != 1) {
                animateTabChange(selectedTabIndex, 1, fragmentManager, StationListFragment.class);
                selectedTabIndex = 1;
            }
        });

        mapLayout.setOnClickListener(v -> {
            if (selectedTabIndex != 2) {
                animateTabChange(selectedTabIndex, 2, fragmentManager, StationMapsFragment.class);
                selectedTabIndex = 2;
            }
        });
    }

    private void animateTabChange(int oldIndex, int newIndex, FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass) {
        animateTab(oldIndex, false);
        animateTab(newIndex, true);
        replaceFragment(fragmentManager, fragmentClass, newIndex > oldIndex);
    }

    private void animateTab(int index, boolean expand) {
        View layout = null;
        View text = null;

        switch (index) {
            case 1:
                layout = listLayout;
                text = listText;
                break;
            case 2:
                layout = mapLayout;
                text = mapText;
                break;
        }

        if (layout != null) {
            layout.animate()
                    .scaleX(expand ? 1.0f : 0.8f)
                    .scaleY(expand ? 1.0f : 0.8f)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();

            layout.setBackgroundResource(expand ? R.drawable.menu_item_background : android.R.color.transparent);
        }

        if (text != null) {
            text.setVisibility(expand ? View.VISIBLE : View.GONE);
        }
    }

    private void replaceFragment(FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass, boolean isForward) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        if (isForward) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        transaction.replace(R.id.listContainer, fragmentClass, null);
        transaction.commit();
    }
}
