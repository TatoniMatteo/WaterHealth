package com.tatonimatteo.waterhealth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.view.DetailsFragmentAdapter;

public class StationDetails extends Fragment {

    private NavController navController;
    private StationDetailsViewModel viewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(StationDetailsViewModel.class);
        return inflater.inflate(R.layout.station_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            long stationId = bundle.getLong("stationId");
            viewModel.setSelectedStation(stationId);
        }

        ImageButton backButton = view.findViewById(R.id.backButton);
        tabLayout = view.findViewById(R.id.stationDetailsTabLayout);
        viewPager = view.findViewById(R.id.stationDetailsViewPager);
        progressBar = view.findViewById(R.id.stationDetailsProgressBar);

        FragmentActivity activity = requireActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        DetailsFragmentAdapter adapter = new DetailsFragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);

        backButton.setOnClickListener(view1 -> navController.navigateUp());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        viewPager.setUserInputEnabled(false);


        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getString(R.string.data_error))
                        .setMessage(error.getMessage())
                        .setPositiveButton(getString(R.string.retry), (dialog, which) -> {
                        })
                        .setNegativeButton(getString(R.string.exit), (dialog, which) -> requireActivity().finishAffinity())
                        .setCancelable(false)
                        .show();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}