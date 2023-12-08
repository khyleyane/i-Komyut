package com.example.ecabs.User_Authentication_Registration;

import static com.example.ecabs.Activity.MainActivity.KEY_USERNAME;
import static com.example.ecabs.Activity.ProfileActivity.KEY_ADD;
import static com.example.ecabs.Activity.ProfileActivity.KEY_AGE;
import static com.example.ecabs.Activity.ProfileActivity.KEY_EMAIL;
import static com.example.ecabs.Activity.ProfileActivity.KEY_NAME;
import static com.example.ecabs.Activity.ProfileActivity.KEY_NO;
import static com.example.ecabs.Fragments.SearchFragment.SHARED_PREF_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecabs.Activity.FAQsActivity;
import com.example.ecabs.Activity.MainActivity;
import com.example.ecabs.Activity.Once_Login;
import com.example.ecabs.Activity.ProfileActivity;
import com.example.ecabs.Activity.SettingActivity;
import com.example.ecabs.R;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.DatabaseHelper;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper databaseHelper;
    private Handler networkCheckHandler = new Handler();
    private Runnable networkCheckRunnable;
    private ConnectionManager connectionManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private String email;
    private String password;

    private String getNewPass1;
    private String getNewPass2;
    private String getUserName;
    private String getContactNo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        String text = "Sign Up";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.signupRedirectText.setText(spannableString);

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.loginEmail.getText().toString();
                password = binding.loginPassword.getText().toString();

                if (NetworkUtils.isWifiConnected(getApplicationContext())) {
                    if (email.equals("") || password.equals("")) {
                        Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    } else {
                        checkUser();
                    }
                }else {
                    connectionManager = new ConnectionManager(LoginActivity.this, editor);
                    connectionManager.lostConnectionDialog(LoginActivity.this);
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.eastBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean closeClicked = true;
                // Rest of your code here...
                Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View alertCustomDialog = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_forget_password, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setView(alertCustomDialog);
                EditText newPass1 = (EditText) alertCustomDialog.findViewById(R.id.editPassword1);
                EditText newPass2 = (EditText) alertCustomDialog.findViewById(R.id.editPassword2);
                ImageView close = (ImageView) alertCustomDialog.findViewById(R.id.close);
                EditText contactNo = (EditText) alertCustomDialog.findViewById(R.id.getContact);
                EditText emailUsername = (EditText) alertCustomDialog.findViewById(R.id.getUser);
                ProgressBar progressBar = alertCustomDialog.findViewById(R.id.progress_bar);
                Button updateBtn = (Button) alertCustomDialog.findViewById(R.id.updateBtn);

                LinearLayout firstPage = (LinearLayout) alertCustomDialog.findViewById(R.id.firstPage);
                LinearLayout secondPage = (LinearLayout) alertCustomDialog.findViewById(R.id.secondPage);

                final AlertDialog dialog = alertDialog.create();

                newPass1.setEnabled(false);
                newPass2.setEnabled(false);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                View.OnClickListener click = v -> {
                    getUserName = emailUsername.getText().toString();
                    getContactNo = contactNo.getText().toString();
                    getNewPass1 = newPass1.getText().toString();
                    getNewPass2 = newPass2.getText().toString();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUserDatabase = reference.orderByChild("email").equalTo(getUserName);

                    if (NetworkUtils.isWifiConnected(getApplicationContext())) {

                        if (v == updateBtn){
                            if (updateBtn.getText().equals("Next")){
                                if (!getUserName.equals("") && !getContactNo.equals("")) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                String contactFromDB = snapshot.child(getUserName).child("contact").getValue(String.class);
                                                if (Objects.equals(contactFromDB, getContactNo)){
                                                        newPass1.setEnabled(true);
                                                        newPass2.setEnabled(true);
                                                        contactNo.setEnabled(false);
                                                        emailUsername.setEnabled(false);
                                                        firstPage.setAlpha(0);
                                                        firstPage.setElevation(0);
                                                        secondPage.setElevation(3);
                                                        secondPage.setAlpha(1);
                                                        updateBtn.setText("Submit");
                                                }else {
                                                    Toast.makeText(LoginActivity.this, "Invalid contact number!", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(LoginActivity.this, "Invalid Username!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }, 300);


                                }else {
                                    Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                                }
                            } else if (updateBtn.getText().equals("Submit")) {

                                View alertCustomDialog1 = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_confirmation_edit_profile, null);
                                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(LoginActivity.this);
                                alertDialog1.setView(alertCustomDialog1);

                                Button yesBtn = alertCustomDialog1.findViewById(R.id.yesBtn);
                                Button noBtn = alertCustomDialog1.findViewById(R.id.noBtn);
                                final AlertDialog dialog1 = alertDialog1.create();




                                if (!getNewPass1.equals("") && !getNewPass2.equals("")){
                                    if (getNewPass1.equals(getNewPass2)){
                                        if (getNewPass1.length() < 8){
                                            Toast.makeText(LoginActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog1.show();
                                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog1.cancel();
                                                    progressBar.setVisibility(View.VISIBLE);

                                                    reference.child(getUserName).child("password").setValue(getNewPass1);

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(LoginActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                        }
                                                    }, 300);
                                                }
                                            });
                                            noBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog1.cancel();
                                                }
                                            });
                                        }

                                    }else {
                                        Toast.makeText(LoginActivity.this, "Password not match!", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                                }


                            }

                        }
                    }else {
                        connectionManager = new ConnectionManager(LoginActivity.this, editor);
                        connectionManager.lostConnectionDialog(LoginActivity.this);
                    }
                };
                updateBtn.setOnClickListener(click);


                //for forget pass
                //create 2 dialog
                //1 for verifying contact no.
                //2 for new pass
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

    }
    public void checkUser(){
        String userUserName = binding.loginEmail.getText().toString().trim();
        String userUserPassword = binding.loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userUserName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                  String passwordFromDB = snapshot.child(userUserName).child("password").getValue(String.class);

                  if (Objects.equals(passwordFromDB, userUserPassword)){


                      String emailFromDB = snapshot.child(userUserName).child("email").getValue(String.class);
                      String nameFromDB = snapshot.child(userUserName).child("name").getValue(String.class);
                      String ageFromDB = snapshot.child(userUserName).child("age").getValue(String.class);
                      String contactFromDB = snapshot.child(userUserName).child("contact").getValue(String.class);
                      String addressFromDB = snapshot.child(userUserName).child("address").getValue(String.class);


                      //store the above to sharedPref
                      if (emailFromDB != null || ageFromDB != null || contactFromDB != null || addressFromDB != null){
                          editor.putString(KEY_EMAIL, userUserName);
                          editor.putString(KEY_NAME, nameFromDB);
                          editor.putString(KEY_AGE, ageFromDB);
                          editor.putString(KEY_ADD, addressFromDB);
                          editor.putString(KEY_NO, contactFromDB);
                          editor.putString(KEY_USERNAME, "save_username");
                          editor.apply();
                      }

                      Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(LoginActivity.this, Once_Login.class);
                      startActivity(intent);

                  }else {
                      Toast.makeText(LoginActivity.this, "Invalid password!", Toast.LENGTH_SHORT).show();
                  }
                }else {
                    Toast.makeText(LoginActivity.this, "Invalid username!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
