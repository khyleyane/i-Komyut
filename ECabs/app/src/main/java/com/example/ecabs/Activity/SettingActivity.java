package com.example.ecabs.Activity;

import static com.example.ecabs.Activity.ProfileActivity.Avatar;
import static com.example.ecabs.Activity.ProfileActivity.KEY_EMAIL;
import static com.example.ecabs.Fragments.SearchFragment.SHARED_PREF_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecabs.Fragments.OnceLoginSettingFragment;
import com.example.ecabs.R;
import com.example.ecabs.User_Authentication_Registration.LoginActivity;
import com.example.ecabs.User_Authentication_Registration.SignUpActivity;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.databinding.ActivityRateUsBinding;
import com.example.ecabs.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ConnectionManager connectionManager;
    String getAvatar;
    int avatar = 0;

    String getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        getAvatar = preferences.getString(Avatar, null);
        getEmail = preferences.getString(KEY_EMAIL, null);

        ImageView profilePic = view.findViewById(R.id.settingProfilePic);
        LinearLayout onceLoginContainer = view.findViewById(R.id.onceLoginContainer);
        LinearLayout notLoginCOntainer = view.findViewById(R.id.notLogInContainer);

        ImageView eastback = view.findViewById(R.id.eastBack);
        eastback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

if (getEmail != null){

    if (getAvatar != null){
        if (getAvatar.equals("1")){
            profilePic.setBackgroundResource(R.drawable.avatar1);
        }else if (getAvatar.equals("2")){
            profilePic.setBackgroundResource(R.drawable.avatar2);
        }else if (getAvatar.equals("3")){
            profilePic.setBackgroundResource(R.drawable.avatar3);
        }else if (getAvatar.equals("4")){
            profilePic.setBackgroundResource(R.drawable.avatar4);
        }else if (getAvatar.equals("5")){
            profilePic.setBackgroundResource(R.drawable.avatar5);
        }else if (getAvatar.equals("6")){
            profilePic.setBackgroundResource(R.drawable.avatar6);
        }
    }else {
        setAvatar();
    }

}
        TextView signupRedirectText = view.findViewById(R.id.signupRedirectText);


        Button loginButton = view.findViewById(R.id.loginButton1);
        LinearLayout settingForm = view.findViewById(R.id.settingForm);

        String text = "Sign Up";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupRedirectText.setText(spannableString);


        View.OnClickListener clickListener1 = v -> {

            if (v==loginButton){
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);

            }else if (v == signupRedirectText) {
                Intent intent = new Intent(SettingActivity.this, SignUpActivity.class);
                startActivity(intent);


            }
        };
        loginButton.setOnClickListener(clickListener1);
        signupRedirectText.setOnClickListener(clickListener1);

        /*Logging in*/



        LinearLayout savedLocationBtn = view.findViewById(R.id.savedLocationBtn);
        LinearLayout faqsBtn = view.findViewById(R.id.FAQs);
        LinearLayout contactBtn = view.findViewById(R.id.contactUs);
        LinearLayout emailBtn = view.findViewById(R.id.emailUs);
        LinearLayout rateBtn = view.findViewById(R.id.rateBtn);
        LinearLayout TCBtn = view.findViewById(R.id.TCBtn);
        LinearLayout PPBtn = view.findViewById(R.id.PPBtn);
        TextView userName = view.findViewById(R.id.userName);
        ImageView topLogo = view.findViewById(R.id.topLogo);
        TextView saveLoctTxt = view.findViewById(R.id.saveLocTxt);
        if (getEmail !=null){
            userName.setText("Hello, "+getEmail+"!");
            onceLoginContainer.setVisibility(View.VISIBLE);
            notLoginCOntainer.setVisibility(View.GONE);
            savedLocationBtn.setVisibility(View.VISIBLE);
            saveLoctTxt.setVisibility(View.VISIBLE);
            topLogo.setVisibility(View.GONE);
        } else if (getEmail == null) {
            saveLoctTxt.setVisibility(View.GONE);
            onceLoginContainer.setVisibility(View.GONE);
            notLoginCOntainer.setVisibility(View.VISIBLE);
            topLogo.setVisibility(View.VISIBLE);
            savedLocationBtn.setVisibility(View.GONE);
        }

        Button editProfile = findViewById(R.id.editProfile);

        View.OnClickListener clickListener = v -> {

            if (v == faqsBtn){
                Intent intent = new Intent(SettingActivity.this, FAQsActivity.class);
                startActivity(intent);
                finish();
            }
            if (v == contactBtn){
                Intent intent = new Intent(SettingActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
            if (v == emailBtn){
                // Rest of your code here...
                Intent intent = new Intent(SettingActivity.this, SuggestionFeedbackActivity.class);
                startActivity(intent);
            }
            if (v == rateBtn){
                Intent intent = new Intent(SettingActivity.this, RateUsActivity.class);
                startActivity(intent);
            }
            if (v == TCBtn){
                Intent intent = new Intent(SettingActivity.this, Terms_ConditionActivity.class);
                intent.putExtra("SETTINGS", true);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            if (v == PPBtn){
                Intent intent = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                intent.putExtra("SETTINGS", true);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            if (v == editProfile){
                // Rest of your code here...
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
            if (v == savedLocationBtn){
                if (NetworkUtils.isWifiConnected(SettingActivity.this)) {
                    Intent intent = new Intent(SettingActivity.this, SaveLocationActivity.class);
                    startActivity(intent);
                }else {
                    connectionManager = new ConnectionManager(SettingActivity.this, editor);
                    connectionManager.lostConnectionDialog(SettingActivity.this);
                }

            }

        };
        faqsBtn.setOnClickListener(clickListener);
        contactBtn.setOnClickListener(clickListener);
        emailBtn.setOnClickListener(clickListener);
        rateBtn.setOnClickListener(clickListener);
        editProfile.setOnClickListener(clickListener);
        rateBtn.setOnClickListener(clickListener);
        TCBtn.setOnClickListener(clickListener);
        PPBtn.setOnClickListener(clickListener);
        savedLocationBtn.setOnClickListener(clickListener);


    }


    public void setAvatar(){
        View alertCustomDialog = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_choose_avatar, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
        alertDialog.setView(alertCustomDialog);
        Button submit = (Button) alertCustomDialog.findViewById(R.id.avatarSubmitBtn);
        Button avatar1 = (Button) alertCustomDialog.findViewById(R.id.avatarBtn1);
        Button avatar2 = (Button) alertCustomDialog.findViewById(R.id.avatarBtn2);
        Button avatar3 = (Button) alertCustomDialog.findViewById(R.id.avatarBtn3);
        Button avatar4 = (Button) alertCustomDialog.findViewById(R.id.avatarBtn4);
        Button avatar5 = (Button) alertCustomDialog.findViewById(R.id.avatarBtn5);
        Button avatar6 = (Button) alertCustomDialog.findViewById(R.id.avatarBtn6);

        final FrameLayout con1 = (FrameLayout) alertCustomDialog.findViewById(R.id.container1);
        final FrameLayout con2 = (FrameLayout) alertCustomDialog.findViewById(R.id.container2);
        final FrameLayout con3 = (FrameLayout) alertCustomDialog.findViewById(R.id.container3);
        final FrameLayout con4 = (FrameLayout) alertCustomDialog.findViewById(R.id.container4);
        final FrameLayout con5 = (FrameLayout) alertCustomDialog.findViewById(R.id.container5);
        final FrameLayout con6 = (FrameLayout) alertCustomDialog.findViewById(R.id.container6);

        final AlertDialog dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View.OnClickListener clickListener = v ->{
            if (v==avatar1){
                con1.setBackgroundResource(R.drawable.circle);
                con2.setBackgroundResource(0);
                con3.setBackgroundResource(0);
                con4.setBackgroundResource(0);
                con5.setBackgroundResource(0);
                con6.setBackgroundResource(0);
                avatar = 1;
            } else if (v==avatar2) {
                con1.setBackgroundResource(0);
                con2.setBackgroundResource(R.drawable.circle);
                con3.setBackgroundResource(0);
                con4.setBackgroundResource(0);
                con5.setBackgroundResource(0);
                con6.setBackgroundResource(0);
                avatar = 2;
            } else if (v==avatar3) {
                con1.setBackgroundResource(0);
                con2.setBackgroundResource(0);
                con3.setBackgroundResource(R.drawable.circle);
                con4.setBackgroundResource(0);
                con5.setBackgroundResource(0);
                con6.setBackgroundResource(0);
                avatar = 3;
            } else if (v==avatar4) {
                con1.setBackgroundResource(0);
                con2.setBackgroundResource(0);
                con3.setBackgroundResource(0);
                con4.setBackgroundResource(R.drawable.circle);
                con5.setBackgroundResource(0);
                con6.setBackgroundResource(0);
                avatar = 4;
            } else if (v==avatar5) {
                con1.setBackgroundResource(0);
                con2.setBackgroundResource(0);
                con3.setBackgroundResource(0);
                con4.setBackgroundResource(0);
                con5.setBackgroundResource(R.drawable.circle);
                con6.setBackgroundResource(0);
                avatar = 5;
            } else if (v==avatar6) {
                con1.setBackgroundResource(0);
                con2.setBackgroundResource(0);
                con3.setBackgroundResource(0);
                con4.setBackgroundResource(0);
                con5.setBackgroundResource(0);
                con6.setBackgroundResource(R.drawable.circle);
                avatar = 6;
            }
        };
        avatar1.setOnClickListener(clickListener);
        avatar2.setOnClickListener(clickListener);
        avatar3.setOnClickListener(clickListener);
        avatar4.setOnClickListener(clickListener);
        avatar5.setOnClickListener(clickListener);
        avatar6.setOnClickListener(clickListener);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (avatar !=0){
                    if (avatar == 1){
                        editor.putString(Avatar, "1");
                        editor.apply();
                    } else if (avatar == 2) {
                        editor.putString(Avatar, "2");
                        editor.apply();
                    } else if (avatar == 3) {
                        editor.putString(Avatar, "3");
                        editor.apply();
                    } else if (avatar == 4) {
                        editor.putString(Avatar, "4");
                        editor.apply();
                    } else if (avatar == 5) {
                        editor.putString(Avatar, "5");
                        editor.apply();
                    }
                    else if (avatar == 6) {
                        editor.putString(Avatar, "6");
                        editor.apply();
                    }
                }else {
                    Toast.makeText(SettingActivity.this, "Choose Avatar", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                dialog.cancel();
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    }
