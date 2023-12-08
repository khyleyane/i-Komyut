package com.example.ecabs.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecabs.Activity.ContactUsActivity;
import com.example.ecabs.Activity.SuggestionFeedbackActivity;
import com.example.ecabs.Activity.FAQsActivity;
import com.example.ecabs.User_Authentication_Registration.LoginActivity;
import com.example.ecabs.Activity.PrivacyPolicyActivity;
import com.example.ecabs.Activity.RateUsActivity;
import com.example.ecabs.User_Authentication_Registration.SignUpActivity;
import com.example.ecabs.Activity.Terms_ConditionActivity;
import com.example.ecabs.R;


public class SettingFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView signupRedirectText = view.findViewById(R.id.signupRedirectText);


        Button loginButton = view.findViewById(R.id.loginButton1);
        LinearLayout settingForm = view.findViewById(R.id.settingForm);

        String text = "Sign Up";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupRedirectText.setText(spannableString);


        View.OnClickListener clickListener = v -> {

            if (v==loginButton){
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(intent);

            }else if (v == signupRedirectText) {
                Intent intent = new Intent(requireContext(), SignUpActivity.class);
                startActivity(intent);


            }
        };
        loginButton.setOnClickListener(clickListener);
        signupRedirectText.setOnClickListener(clickListener);

        /*Logging in*/


        LinearLayout faqsBtn = view.findViewById(R.id.faqsBtn);
        LinearLayout contactUsBtn = view.findViewById(R.id.contactUsBtn);
        LinearLayout emailBtn = view.findViewById(R.id.emailBtn);
        LinearLayout rateBtn = view.findViewById(R.id.rateBtn);
        LinearLayout PPBtn = view.findViewById(R.id.PPBtn);
        LinearLayout TermConBtn = view.findViewById(R.id.TermConBtn);




        View.OnClickListener clickListener2 = v -> {
            if (v== faqsBtn){
                Intent intent = new Intent(requireContext(), FAQsActivity.class);
                startActivity(intent);
                requireActivity().finish();


            }if (v== contactUsBtn){
                Intent intent = new Intent(requireContext(), ContactUsActivity.class);
                startActivity(intent);

            }if (v== emailBtn){
                Intent intent = new Intent(requireContext(), SuggestionFeedbackActivity.class);
                startActivity(intent);
                requireActivity().finish();

            }
            if (v== rateBtn){
                Intent intent = new Intent(requireContext(), RateUsActivity.class);
                startActivity(intent);

            }
            if (v== PPBtn) {
                Intent intent = new Intent(requireContext(), PrivacyPolicyActivity.class);
                intent.putExtra("SETTINGS", true);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            if (v== TermConBtn) {
                Intent intent = new Intent(requireContext(), Terms_ConditionActivity.class);
                intent.putExtra("SETTINGS", true);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        };
        faqsBtn.setOnClickListener(clickListener2);
        contactUsBtn.setOnClickListener(clickListener2);
        emailBtn.setOnClickListener(clickListener2);
        rateBtn.setOnClickListener(clickListener2);
        PPBtn.setOnClickListener(clickListener2);
        TermConBtn.setOnClickListener(clickListener2);

        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}