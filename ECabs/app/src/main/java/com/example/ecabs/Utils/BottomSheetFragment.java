package com.example.ecabs.Utils;

import static android.content.Context.MODE_PRIVATE;

import static com.example.ecabs.Activity.MainActivity.fareDiscount;
import static com.example.ecabs.Fragments.Maps_Fragment.SHARED_PREF_NAME;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecabs.Fragments.Maps_Fragment;
import com.example.ecabs.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private List<TodaItem> itemList;
    public MyAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String getFareDiscountStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_bottom_sheet, container, false);

        preferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        getFareDiscountStatus = preferences.getString(fareDiscount, null);

        TextView discountFareTxt = view.findViewById(R.id.DiscountedFareTxt);

        if (getFareDiscountStatus != null){
            if (getFareDiscountStatus.equals("none")){
                discountFareTxt.setText("Normal Fare");
            } else if (getFareDiscountStatus.equals("discounted")) {
                discountFareTxt.setText("Discounted Fare");
            }
        }
        recyclerView = view.findViewById(R.id.ViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new MyAdapter(itemList);

        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        return view;

    }
    public BottomSheetFragment(List<TodaItem> list) {
        this.itemList = list;
    }
}
