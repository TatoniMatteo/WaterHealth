package com.tatonimatteo.waterhealth.fragment.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.fragment.StationDetailsViewModel;
import com.tatonimatteo.waterhealth.view.LiveDataItem;

public class StationData extends Fragment {

    private LinearLayout liveDataContainer;
    private StationDetailsViewModel viewModel;

    private TextView errorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(StationDetailsViewModel.class);
        return inflater.inflate(R.layout.station_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        liveDataContainer = view.findViewById(R.id.liveDataContainer);
        errorView = view.findViewById(R.id.liveDataError);

        viewModel.getLiveData().observe(getViewLifecycleOwner(), data -> {
            liveDataContainer.removeAllViews();
            if (data.isEmpty()) errorView.setVisibility(View.VISIBLE);
            else errorView.setVisibility(View.GONE);
            data.forEach(triple -> {
                LiveDataItem item = new LiveDataItem(requireContext());
                item.setComponent(
                        triple.getFirst().getSensorType().getName(),
                        triple.getFirst().getUnit(),
                        triple.getFirst().getDecimals(),
                        triple.getSecond().getValue(),
                        triple.getThird()
                );
                liveDataContainer.addView(item);
            });
        });
    }
}