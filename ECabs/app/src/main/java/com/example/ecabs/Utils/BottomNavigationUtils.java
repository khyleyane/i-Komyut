package com.example.ecabs.Utils;

import android.app.Activity;

import com.example.ecabs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationUtils {
    public static void selectBottomNavigationItem(Activity activity, int itemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(itemId);
    }
}
