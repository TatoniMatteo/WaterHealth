package com.tatonimatteo.waterhealth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.view.HomeFragmentAdapter;
import com.tatonimatteo.waterhealth.view.Loader;

public class Stations extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Loader loader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupViewPager();
        setupTabLayout();
        observeViewModel();
    }

    private void initializeViews(View view) {
        tabLayout = view.findViewById(R.id.stationsTabLayout);
        viewPager = view.findViewById(R.id.stationsViewPager);
        loader = view.findViewById(R.id.stationsLoader);
    }

    private void setupViewPager() {
        FragmentManager fragmentManager = getChildFragmentManager();
        HomeFragmentAdapter adapter = new HomeFragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.setUserInputEnabled(tab.getPosition() != 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void observeViewModel() {
        StationsViewModel viewModel = new ViewModelProvider(this).get(StationsViewModel.class);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loader.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && isAdded()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getString(R.string.data_error))
                        .setMessage(error.getMessage())
                        .setPositiveButton(getString(R.string.retry), (dialog, which) -> error.retry())
                        .setNegativeButton(getString(R.string.exit), (dialog, which) -> requireActivity().finishAffinity())
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
