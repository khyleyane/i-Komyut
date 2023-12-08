package com.example.ecabs.Activity;

import static com.example.ecabs.Activity.MainActivity.NETWORK_CHECK_INTERVAL;
import static com.example.ecabs.Activity.MainActivity.connection;

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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecabs.Fragments.OnceLoginSettingFragment;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.DatabaseHelper;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.Utils.PasswordUtils;
import com.example.ecabs.R;
import com.example.ecabs.databinding.ActivityProfileBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ConnectionManager connectionManager;

    FirebaseDatabase database;
    DatabaseReference reference;

    private Handler networkCheckHandler = new Handler();
    private Runnable networkCheckRunnable;

    private TextView    profemail;
    private TextView    profName;
    private TextView    profage;
    private TextView    profcontactNo;
    private TextView    profaddress;
    private TextView    profpass;



    private static final String SHARED_PREF_NAME= "MyPreferences";
    public static final String KEY_EMAIL= "email";
    public static final String KEY_NAME= "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_NO = "contact";
    public static final String KEY_ADD = "Address";
    public static final String KEY_PASS = "pass";

    public static final String Avatar = "Avatar";

    private static final String KEY_USERNAME =  "save_username";

    String getAvatar;

    String getKeyEmail;

    String getKeyName;
    String getKeyAge;
    String getKeyNo;
    String getKeyAdd;

    String con;
    int avatar = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
        ImageView back = binding.eastBack;
        LinearLayout logoutBtn = binding.logoutBtn;

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        con = preferences.getString(connection, null);


        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        getAvatar = preferences.getString(Avatar, null);
        getKeyEmail = preferences.getString(KEY_EMAIL, null);
        getKeyName = preferences.getString(KEY_NAME, null);
        getKeyAge = preferences.getString(KEY_AGE, null);
        getKeyNo = preferences.getString(KEY_NO, null);
        getKeyAdd = preferences.getString(KEY_ADD, null);

        final FrameLayout profilePic = binding.profilePic;

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
        }

        ImageView editProfileBtn = findViewById(R.id.editProfilePic);



        profemail = findViewById(R.id.profileEmail);
        profName = findViewById(R.id.profileName);
        profage = findViewById(R.id.profileAge);
        profcontactNo = findViewById(R.id.profileNo);
        profaddress = findViewById(R.id.profileAddress);

       /* profpass = findViewById(R.id.profilePass);*/

        String email = preferences.getString(KEY_EMAIL, null);
        String name = preferences.getString(KEY_NAME, null);
        String age = preferences.getString(KEY_AGE, null);
        String contact = preferences.getString(KEY_NO, null);
        String address = preferences.getString(KEY_ADD, null);
        String pass = preferences.getString(KEY_PASS, null);

        reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(email);


        if ( email != null ||name != null || age != null|| contact != null || address != null || pass != null){

            profemail.setText(email);
            profName.setText(name);
            profage.setText(String.valueOf(age) + " years old");
            profcontactNo.setText(String.valueOf(contact));
            profaddress.setText(address);
           /* String maskedPassword = PasswordUtils.getAsteriskString(pass.length());
            profpass.setText(maskedPassword);*/
        }

        Handler handler = new Handler();

        View.OnClickListener clickListener = v -> {

            if (v == logoutBtn){
                logoutBtn.setBackgroundResource(R.drawable.button_hover_5radius);
            }


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (v == logoutBtn){
                        logoutBtn.setBackgroundResource(R.drawable.button_5radius);
                        editor.remove(KEY_USERNAME);
                        editor.remove(KEY_EMAIL);
                        editor.remove(Avatar);
                        editor.commit();
                        Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }
            }, 100);
        };
        logoutBtn.setOnClickListener(clickListener);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvatar();
                editProfileBtn.setBackgroundResource(R.drawable.circle);

                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editProfileBtn.setBackgroundResource(R.drawable.circle_container);
                    }
                }, 200);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        binding.EditUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertCustomDialog = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_editprofile_details, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
                alertDialog.setView(alertCustomDialog);
                EditText editName = (EditText) alertCustomDialog.findViewById(R.id.editName);
                EditText editAge = (EditText) alertCustomDialog.findViewById(R.id.editAge);
                EditText editNo = (EditText) alertCustomDialog.findViewById(R.id.editNo);
                EditText editAddress = (EditText) alertCustomDialog.findViewById(R.id.editAddress);
                ImageView close = (ImageView) alertCustomDialog.findViewById(R.id.close);
                Button savedBtn = (Button) alertCustomDialog.findViewById(R.id.saveBtn);
                Button cancelBtn = (Button) alertCustomDialog.findViewById(R.id.cancelBtn);
                LinearLayout passContainer = (LinearLayout) alertCustomDialog.findViewById(R.id.passwordContainer);
                EditText passwordAuthField = (EditText) alertCustomDialog.findViewById(R.id.passwordAuth);



                ProgressBar progressBar = (ProgressBar) alertCustomDialog.findViewById(R.id.progress_bar);
                final AlertDialog dialog = alertDialog.create();

                if (getKeyEmail != null || getKeyNo != null || getKeyAge != null || getKeyAdd != null){
                    editName.setText(getKeyName);
                    editAge.setText(getKeyAge);
                    editNo.setText(getKeyNo);
                    editAddress.setText(getKeyAdd);
                }

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



                savedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* if (savedBtn.getText().toString().equals(""))*/


                        View alertCustomDialog = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_confirmation_edit_profile, null);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialog.setView(alertCustomDialog);

                        Button yesBtn = alertCustomDialog.findViewById(R.id.yesBtn);
                        Button noBtn = alertCustomDialog.findViewById(R.id.noBtn);
                        final AlertDialog dialog1 = alertDialog.create();

                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();

                        noBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.cancel();
                            }
                        });

                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.cancel();
                                progressBar.setVisibility(View.VISIBLE);
                                String newName = editName.getText().toString();
                                String newAge = editAge.getText().toString();
                                String newNo = editNo.getText().toString();
                                String newAddress = editAddress.getText().toString();

                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users");

                                Handler handler = new Handler();
                                if (NetworkUtils.isWifiConnected(getApplicationContext())) {
                                    reference.child(getKeyEmail).child("name").setValue(newName);
                                    reference.child(getKeyEmail).child("age").setValue(newAge);
                                    reference.child(getKeyEmail).child("contact").setValue(newNo);
                                    reference.child(getKeyEmail).child("address").setValue(newAddress);
                                    editor.putString(KEY_NAME, newName);
                                    editor.putString(KEY_AGE, newAge);
                                    editor.putString(KEY_ADD, newAddress);
                                    editor.putString(KEY_NO, newNo);
                                    editor.apply();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            overridePendingTransition(0, 0);
                                            Toast.makeText(ProfileActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            dialog.cancel();
                                        }
                                    }, 500);

                                } else {
                                    connectionManager = new ConnectionManager(ProfileActivity.this, editor);
                                    connectionManager.lostConnectionDialog(ProfileActivity.this);
                                    dialog.cancel();
                                }
                            }
                        });
                            }
                        });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

    }

    public void setAvatar(){
        View alertCustomDialog = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_choose_avatar, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
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

        ImageView close = (ImageView) alertCustomDialog.findViewById(R.id.close);

        close.setVisibility(View.VISIBLE);

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
            } else if (v == close) {
                dialog.cancel();
            }
        };
        avatar1.setOnClickListener(clickListener);
        avatar2.setOnClickListener(clickListener);
        avatar3.setOnClickListener(clickListener);
        avatar4.setOnClickListener(clickListener);
        avatar5.setOnClickListener(clickListener);
        avatar6.setOnClickListener(clickListener);
        close.setOnClickListener(clickListener);

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
                    Toast.makeText(ProfileActivity.this, "Choose Avatar", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }


}