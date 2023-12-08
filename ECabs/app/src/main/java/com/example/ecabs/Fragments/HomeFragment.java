package com.example.ecabs.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.ecabs.Activity.ProfileActivity.KEY_EMAIL;
import static com.example.ecabs.Fragments.SearchFragment.SHARED_PREF_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecabs.Activity.AboutAppActivity;
import com.example.ecabs.Activity.AboutUsActivity;
import com.example.ecabs.Activity.HowtoknowIfActivity;
import com.example.ecabs.Activity.ProfileActivity;
import com.example.ecabs.Activity.SuggestionFeedbackActivity;
import com.example.ecabs.R;
import com.example.ecabs.Utils.BottomNavigationUtils;

public class HomeFragment extends Fragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String getEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        getEmail = preferences.getString(KEY_EMAIL, null);

        TextView userName = view.findViewById(R.id.homeUserName);
        if (getEmail !=null){
            userName.setText("Hello, "+getEmail+"!");
        }

        final FrameLayout AboutUsBtn = view.findViewById(R.id.aboutUsBtn);
        final FrameLayout AccountBtn = view.findViewById(R.id.accountBtn);
        final FrameLayout AboutAppBtn = view.findViewById(R.id.AboutAppBtn);
        final FrameLayout HTKBtn = view.findViewById(R.id.HTKBtn);

        View.OnClickListener clickListener = v ->{

            if (v == AboutUsBtn){
                Intent intent = new Intent(requireActivity(), AboutUsActivity.class);
                startActivity(intent);
            } else if (v == AccountBtn) {

                if (getEmail !=null){
                      Intent intent = new Intent(requireContext(), ProfileActivity.class);
                      startActivity(intent);
                      requireActivity().overridePendingTransition(0,0);
                }else {
                    Intent intent = new Intent(requireContext(), SettingFragment.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(0,0);
                }


            } else if (v == AboutAppBtn) {
                Intent intent = new Intent(requireActivity(), AboutAppActivity.class);
                startActivity(intent);
            } else if (v == HTKBtn) {
                Intent intent = new Intent(requireActivity(), HowtoknowIfActivity.class);
                startActivity(intent);
            }


        };
        AboutUsBtn.setOnClickListener(clickListener);
        AccountBtn.setOnClickListener(clickListener);
        AboutAppBtn.setOnClickListener(clickListener);
        HTKBtn.setOnClickListener(clickListener);



        return view;

    }
    private void replaceFragment(Fragment fragment){
        SettingFragment newFragment = new SettingFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
