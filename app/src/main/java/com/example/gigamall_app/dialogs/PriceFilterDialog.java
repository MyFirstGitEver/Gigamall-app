package com.example.gigamall_app.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gigamall_app.R;
import com.google.android.material.slider.RangeSlider;

public class PriceFilterDialog extends DialogFragment {
    private RangeSlider rangeSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.price_filter_dialog, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rangeSlider = view.findViewById(R.id.rangeSlider);

        rangeSlider.setValueFrom(getArguments().getFloat("min"));
        rangeSlider.setValueTo(getArguments().getFloat("max"));

        rangeSlider.setValues(getArguments().getFloat("min"), getArguments().getFloat("max"));

        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                float from = slider.getValues().get(0);
                float to = slider.getValues().get(1);

                Bundle bundle = new Bundle();
                bundle.putFloat("start", from);
                bundle.putFloat("end", to);
                getParentFragmentManager().setFragmentResult("filter", bundle);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}