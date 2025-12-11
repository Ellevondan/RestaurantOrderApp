package com.miun.restaurantorderapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CustomizationFragment extends Fragment {
    public CustomizationFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up close button (X button in top-right)
        Button btnClose = view.findViewById(R.id.btnCloseFragment);
        btnClose.setOnClickListener(v -> {
            closeFragment();
        });

        // Set up cancel button
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            closeFragment();
        });

        // Set up confirm button
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            // TODO: Add item to order summary
            // For now, just close the fragment
            closeFragment();
        });
    }

    /**
     * Close the fragment by popping it from the back stack
     */
    private void closeFragment() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        }
    }
}