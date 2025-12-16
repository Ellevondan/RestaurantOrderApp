package com.miun.restaurantorderapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miun.restaurantorderapp.models.ModifiedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomizationFragment extends Fragment {

    public static final String REQUEST_KEY = "customization_request";
    public static final String RESULT_MODIFIED_ITEM = "result_modified_item";

    public static final String ARG_MENU_ID = "arg_menu_id";
    public static final String ARG_MENU_NAME = "arg_menu_name";
    public static final String ARG_MENU_PRICE = "arg_menu_price";

    private long itemId;
    private String itemName = "";
    private double itemPrice = 0.0;
    private int quantity = 1;


    private TextView tvDishName;
    private TextView tvPrice;
    private TextView tvQuantity;
    private EditText etComment;
    private CheckBox cbGluten;
    private CheckBox cbLaktos;

    private Button btnIncrease;
    private Button btnDecrease;
    private Button btnConfirm;
    private Button btnCancel;
    private Button btnClose;

    public CustomizationFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) { closeFragment(); return; }

        itemId = args.getLong(ARG_MENU_ID, -1L);
        itemName = safe(args.getString(ARG_MENU_NAME, ""));
        itemPrice = args.getDouble(ARG_MENU_PRICE, 0.0);

        if (itemId <= 0 || itemName.isEmpty()) { closeFragment(); return; }


        tvDishName = view.findViewById(R.id.tvDishName);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        etComment = view.findViewById(R.id.etComment);

        cbGluten = view.findViewById(R.id.cbGluten);
        cbLaktos = view.findViewById(R.id.cbLaktos);

        btnIncrease = view.findViewById(R.id.btnIncrease);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnClose = view.findViewById(R.id.btnCloseFragment);

        tvDishName.setText(itemName);
        updatePriceDisplay();

        btnClose.setOnClickListener(v -> closeFragment());
        btnCancel.setOnClickListener(v -> closeFragment());

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            updatePriceDisplay();
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updatePriceDisplay();
            }
        });

        btnConfirm.setOnClickListener(v -> saveAndClose());
    }

    private void updatePriceDisplay() {
        tvQuantity.setText(String.valueOf(quantity));
        double total = itemPrice * quantity;
        tvPrice.setText(String.format(Locale.getDefault(), "%.0f SEK", total));
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static String nowIsoUtc() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    private void saveAndClose() {
        List<String> allergens = new ArrayList<>();
        if (cbGluten.isChecked()) allergens.add("Gluten");
        if (cbLaktos.isChecked()) allergens.add("Laktos");


        String allergenString = TextUtils.join(",", allergens);
        String comments = safe(etComment.getText().toString());

        ModifiedItem newItem = new ModifiedItem(
                itemId,
                itemName,
                itemPrice,
                quantity,
                comments,
                allergenString
        );
        newItem.setOrderedAt(nowIsoUtc());

        Bundle result = new Bundle();
        result.putParcelable(RESULT_MODIFIED_ITEM, newItem);
        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);

        closeFragment();
    }

    private void closeFragment() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        }
    }
}
