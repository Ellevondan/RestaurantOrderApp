package com.miun.restaurantorderapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.ModifiedItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomizationFragment extends DialogFragment {

    private static final String ARG_MENU_ITEM = "menu_item";
    private static final String TAG = "CustomizationFragment";

    private MenuItem menuItem;
    private CustomizationListener listener;

    // UI Components
    private TextView tvDishName;
    private TextView tvDishPrice;
    private TextView tvDishDescription;
    private NumberPicker numberPickerQuantity;
    private LinearLayout layoutAllergens;
    private EditText etSpecialInstructions;
    private EditText etComments;
    private List<CheckBox> allergenCheckBoxes = new ArrayList<>();

    public CustomizationFragment() {
        // Required empty constructor
    }

    public static CustomizationFragment newInstance(MenuItem menuItem) {
        CustomizationFragment fragment = new CustomizationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MENU_ITEM, menuItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CustomizationListener) {
            listener = (CustomizationListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement CustomizationListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hämta MenuItem
        if (getArguments() != null) {
            menuItem = (MenuItem) getArguments().getSerializable(ARG_MENU_ITEM);
        }
        if (menuItem == null) {
            Log.e(TAG, "MenuItem is null!");
            dismiss();
            return;
        }

        // Init UI components
        initViews(view);

        // Populate data
        populateMenuItemInfo();
        setupQuantityPicker();
        setupAllergenCheckboxes();

        // Setup buttons
        setupButtons(view);
    }

    private void initViews(View view) {
        tvDishName = view.findViewById(R.id.tvDishName);
        tvDishPrice = view.findViewById(R.id.tvDishPrice);
        tvDishDescription = view.findViewById(R.id.tvDishDescription);
        numberPickerQuantity = view.findViewById(R.id.numberPickerQuantity);
        layoutAllergens = view.findViewById(R.id.layoutAllergens);
        etSpecialInstructions = view.findViewById(R.id.etSpecialInstructions);
        etComments = view.findViewById(R.id.etComments);
    }

    private void populateMenuItemInfo() {
        tvDishName.setText(menuItem.getName());
        tvDishPrice.setText(menuItem.getPrice() + " kr");

        String description = menuItem.getDescription();
        if (description != null && !description.isEmpty()) {
            tvDishDescription.setText(description);
            tvDishDescription.setVisibility(View.VISIBLE);
        } else {
            tvDishDescription.setVisibility(View.GONE);
        }
    }

    private void setupQuantityPicker() {
        numberPickerQuantity.setMinValue(1);
        numberPickerQuantity.setMaxValue(10);
        numberPickerQuantity.setValue(1);
        numberPickerQuantity.setWrapSelectorWheel(false);
    }

    private void setupAllergenCheckboxes() {
        List<String> allergens = menuItem.getAllergensList();

        if (allergens == null || allergens.isEmpty()) {
            layoutAllergens.setVisibility(View.GONE);
            View allergensLabel = getView().findViewById(R.id.tvAllergensLabel);
            if (allergensLabel != null) {
                allergensLabel.setVisibility(View.GONE);
            }
            return;
        }

        // Create checkbox for each allergen
        for (String allergen : allergens) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(allergen.trim());
            checkBox.setTextSize(14);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            checkBox.setLayoutParams(params);

            layoutAllergens.addView(checkBox);
            allergenCheckBoxes.add(checkBox);
        }
    }

    private void setupButtons(View view) {
        Button btnClose = view.findViewById(R.id.btnCloseFragment);
        btnClose.setOnClickListener(v -> closeFragment());

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> closeFragment());

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> confirmOrder());
    }

    private void confirmOrder() {
        // Samla data från UI
        int quantity = numberPickerQuantity.getValue();
        String selectedAllergens = getSelectedAllergens();
        String specialInstructions = etSpecialInstructions.getText().toString().trim();
        String comments = etComments.getText().toString().trim();

        // Skapa ModifiedItem
        ModifiedItem modifiedItem = new ModifiedItem(menuItem);
        modifiedItem.setQuantity(quantity);
        modifiedItem.setSelectedAllergens(selectedAllergens);
        modifiedItem.setSpecialInstructions(specialInstructions);
        modifiedItem.setComments(comments);
        modifiedItem.setOrderedAt(getCurrentTimestamp());

        Log.d(TAG, "Created ModifiedItem: " + modifiedItem.getName() +
                " (qty: " + quantity + ", allergens: " + selectedAllergens + ")");

        // Skicka tillbaka till OrderActivity
        listener.onItemCustomized(modifiedItem);

        closeFragment();
    }

    private String getSelectedAllergens() {
        List<String> selected = new ArrayList<>();

        for (CheckBox checkBox : allergenCheckBoxes) {
            if (checkBox.isChecked()) {
                selected.add(checkBox.getText().toString());
            }
        }

        // Join with comma
        return String.join(",", selected);
    }

    private String getCurrentTimestamp() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return now.format(formatter);
        } else {
            // Fallback för äldre Android versioner
            return "2025-12-16T12:00:00";
        }
    }

    private void closeFragment() {
        dismiss();
    }

    public interface CustomizationListener {
        void onItemCustomized(ModifiedItem item);
    }
}