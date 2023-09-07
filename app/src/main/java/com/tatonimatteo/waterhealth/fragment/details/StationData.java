package com.tatonimatteo.waterhealth.fragment.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tatonimatteo.waterhealth.R;
import com.tatonimatteo.waterhealth.fragment.StationDetailsViewModel;
import com.tatonimatteo.waterhealth.view.DateRangePicker;
import com.tatonimatteo.waterhealth.view.LiveDataItem;

public class StationData extends Fragment {

    private LinearLayout liveDataContainer;
    private StationDetailsViewModel viewModel;

    private TextView errorText;
    private ImageView errorIcon;
    private DateRangePicker picker;

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
        errorText = view.findViewById(R.id.liveDataError);
        errorIcon = view.findViewById(R.id.liveDataWarning);
        picker = view.findViewById(R.id.datePicker);

        picker.setFragmentManager(this.getChildFragmentManager());

        viewModel.getLiveData().observe(getViewLifecycleOwner(), data -> {
            liveDataContainer.removeAllViews();
            errorIcon.setVisibility(View.INVISIBLE);
            if (data.isEmpty()) errorText.setVisibility(View.VISIBLE);
            else errorText.setVisibility(View.GONE);
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
                if (triple.getThird()) errorIcon.setVisibility(View.VISIBLE);
            });
        });
    }
}