package com.example.ecabs.User_Authentication_Registration;

import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecabs.Activity.Once_Signup;
import com.example.ecabs.Activity.SettingActivity;
import com.example.ecabs.R;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.Utils.UserHelper;
import com.example.ecabs.databinding.ActivitySignUpBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private ConnectionManager connectionManager;

    FirebaseDatabase database;
    DatabaseReference reference;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME= "MyPreferences";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        String text =binding.loginRedirectText.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.loginRedirectText.setText(spannableString);

        String text2 =binding.termsConditionRedirect.getText().toString();
        SpannableString spannableString2 = new SpannableString(text2);
        spannableString2.setSpan(new UnderlineSpan(), 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.termsConditionRedirect.setText(spannableString2);

        String text3 =binding.privacyPolicyRedirect.getText().toString();
        SpannableString spannableString3 = new SpannableString(text3);
        spannableString3.setSpan(new UnderlineSpan(), 0, text3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.privacyPolicyRedirect.setText(spannableString3);

        CheckBox TermsConditionCbox = binding.TermsConditionCbox;
        CheckBox PrivacyPolicyCbox = binding.privacyPolicyCbox;

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String name = binding.signupName.getText().toString();
                String age = binding.signupAge.getText().toString();
                String contact = binding.signupConNo.getText().toString();
                String address = binding.signupAdd.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();


                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                if (NetworkUtils.isWifiConnected(getApplicationContext())) {
                    if (!TermsConditionCbox.isChecked() || !PrivacyPolicyCbox.isChecked() || email.equals("") ||name.equals("") || age.equals("") || contact.equals("") || address.equals("") || password.equals("") || confirmPassword.equals("")) {
                        Toast.makeText(SignUpActivity.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                    } else if (!confirmPassword.equals(password)) {
                        Toast.makeText(SignUpActivity.this, "Password not match!", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 8) {
                        Toast.makeText(SignUpActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                    } else if (password.equals(email)) {
                        Toast.makeText(SignUpActivity.this, "Password should not equal to username!", Toast.LENGTH_SHORT).show();
                    } else {
                        UserHelper userHelper = new UserHelper(email, name, age, contact, address, password);
                        reference.child(email).setValue(userHelper);
                        reference.child(email).child("history_id").setValue("0");



                        Toast.makeText(SignUpActivity.this, "Signup successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, Once_Signup.class);
                        startActivity(intent);

                    }
                }else {
                    connectionManager = new ConnectionManager(SignUpActivity.this, editor);
                    connectionManager.lostConnectionDialog(SignUpActivity.this);
                }

            }

        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        binding.eastBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean closeClicked = true;
                Intent intent = new Intent(SignUpActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });
        binding.termsConditionRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View alertCustomDialog = LayoutInflater.from(SignUpActivity.this).inflate(R.layout.dialog_terms_condition, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
                alertDialog.setView(alertCustomDialog);
                Button closeBtn = (Button) alertCustomDialog.findViewById(R.id.agreeBtn);
                final AlertDialog dialog = alertDialog.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
        binding.privacyPolicyRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View alertCustomDialog = LayoutInflater.from(SignUpActivity.this).inflate(R.layout.dialog_privacy_policy, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
                alertDialog.setView(alertCustomDialog);
                Button closeBtn = (Button) alertCustomDialog.findViewById(R.id.agreeBtn);
                final AlertDialog dialog = alertDialog.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

    }

}
