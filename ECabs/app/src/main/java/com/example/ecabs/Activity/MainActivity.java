package com.example.ecabs.Activity;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecabs.DirectionHelper.TaskLoadedCallback;
import com.example.ecabs.Fragments.HomeFragment;
import com.example.ecabs.Fragments.Maps_Fragment;
import com.example.ecabs.Fragments.OnceLoginSettingFragment;
import com.example.ecabs.Fragments.SettingFragment;
import com.example.ecabs.Fragments.TransitFragment;
import com.example.ecabs.R;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.Utils.SQLHelper;
import com.example.ecabs.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.PolylineOptions;


public class MainActivity extends AppCompatActivity implements TaskLoadedCallback {

    ActivityMainBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final long NETWORK_CHECK_INTERVAL = 5000; // 5 seconds
    private static final String SHARED_PREF_NAME= "MyPreferences";
    private static final String userLoc = "userLoc";
    private static final String userDes = "userDes";
    private static final String cost = "cost";
    public static final String connection = "con";
    public static final String fareDiscount = "fareDiscount";
    public static final String KEY_USERNAME =  "save_username";
    String con;
    String getFareDiscount;
    private boolean doubleBackToExitPressedOnce = false;
    private ConnectionManager connectionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new Maps_Fragment());
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();
        setBottomNavigationSelectedItem(R.id.map);

        // create DB
        SQLHelper dbHelper = new SQLHelper(this);

        try {
            dbHelper.createDB();
        } catch (Exception ioe) {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }

        con = preferences.getString(connection, null);
        getFareDiscount = preferences.getString(fareDiscount, null);

        setFareDiscount();

        if (con == null){
            checkInternetConnection();
        }
            //Activity Redirect

            if (getIntent().hasExtra("CLOSE_CLICKED")) {
                boolean closeClicked = getIntent().getBooleanExtra("CLOSE_CLICKED", false);
                if (closeClicked) {
                    if (preferences.contains("save_username")) {
                        replaceFragment(new OnceLoginSettingFragment());
                    } else {
                        replaceFragment(new SettingFragment());

                    }


                }
            }

            if (getIntent().hasExtra("MAPS")) {
                boolean closeClicked = getIntent().getBooleanExtra("MAPS", false);
                if (closeClicked) {
                    replaceFragment(new Maps_Fragment());
                    binding.bottomNavigationView.setSelectedItemId(R.id.map);
                }
            }
            if (getIntent().hasExtra("TRANSIT")) {
                boolean closeClicked = getIntent().getBooleanExtra("TRANSIT", false);
                if (closeClicked) {
                    replaceFragment(new TransitFragment());
                    binding.bottomNavigationView.setSelectedItemId(R.id.transit);
                }
            }
            if (getIntent().hasExtra("SETTINGS")) {
                boolean closeClicked = getIntent().getBooleanExtra("SETTINGS", false);
                if (closeClicked) {
                    if (preferences.contains("save_username")) {
                        replaceFragment(new OnceLoginSettingFragment());
                    } else {
                        replaceFragment(new SettingFragment());

                    }

                }
            }
            if (getIntent().hasExtra("ONCE_LOGIN")) {
                boolean closeClicked = getIntent().getBooleanExtra("ONCE_LOGIN", false);
                if (closeClicked) {
                    // Perform the function to open the Home Fragment
                    replaceFragment(new OnceLoginSettingFragment());


                }
            }
            if (getIntent().hasExtra("CLOSE_LOGIN")) {
                boolean closeClicked = getIntent().getBooleanExtra("CLOSE_LOGIN", false);
                if (closeClicked) {
                    // Perform the function to open the Home Fragment
                    replaceFragment(new SettingFragment());

                }
            }
            if (getIntent().hasExtra("PROFILE")) {
                boolean closeClicked = getIntent().getBooleanExtra("PROFILE", false);
                if (closeClicked) {
                    // Perform the function to open the Home Fragment
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
            if (getIntent().hasExtra("LOG_OUT")) {
                boolean closeClicked = getIntent().getBooleanExtra("LOG_OUT", false);
                if (closeClicked) {
                    // Perform the function to open the Home Fragment
                    replaceFragment(new SettingFragment());
                }
            }
            if (getIntent().hasExtra("LOG_IN")) {
                String  clicked = getIntent().getStringExtra("LOG_IN");
                if (clicked != null) {
                    editor.putString(KEY_USERNAME, clicked);
                    editor.commit();
                    // Perform the function to open the Home Fragment
                    Intent intent = new Intent(MainActivity.this, Once_Login.class);
                    startActivity(intent);
                }
            }


            // Set animation for BottomNavigationView
            binding.bottomNavigationView.setItemHorizontalTranslationEnabled(false);
            binding.bottomNavigationView.setAnimationCacheEnabled(true);
            binding.bottomNavigationView.setItemTextAppearanceActive(R.style.BottomNavigationTextActive);
            binding.bottomNavigationView.setItemTextAppearanceInactive(R.style.BottomNavigationTextInactive);


            binding.bottomNavigationView.setOnItemSelectedListener(item -> {


                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    replaceFragment(new HomeFragment());
                    // Handle home item selected

                } else{

                    // Handle search item selected
                }  if (itemId == R.id.map) {
                    replaceFragment(new Maps_Fragment());


                    // Handle map item selected
                } else if (itemId == R.id.transit) {

                    replaceFragment(new TransitFragment());

                    // Handle transit item selected
                }
                return true;
            });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.remove(userLoc);
        editor.remove(userDes);
        editor.remove(cost);
        editor.remove(connection);
        editor.apply();

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void setBottomNavigationSelectedItem(int itemId) {
            binding.bottomNavigationView.setSelectedItemId(itemId);
        }

    @Override
    public void onTaskDone(Object... values) {
        if(Maps_Fragment.currentPolyline != null) {
            Maps_Fragment.currentPolyline.remove();
        }
        Maps_Fragment.currentPolyline = Maps_Fragment.mapAPI.addPolyline((PolylineOptions) values[0]);
    }

    private void setFareDiscount(){
        if (getFareDiscount != null){

        }else {
            View alertCustomDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_fare_discount, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setView(alertCustomDialog);
            Button submitFareBtn = (Button) alertCustomDialog.findViewById(R.id.submitFareBtn);
            CheckBox fareDiscounted1 = (CheckBox) alertCustomDialog.findViewById(R.id.checkBox1);
            CheckBox none = (CheckBox) alertCustomDialog.findViewById(R.id.fareDNone);
            final AlertDialog dialog = alertDialog.create();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            fareDiscounted1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    none.setChecked(false);
                }

            });
            none.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    fareDiscounted1.setChecked(false);
                }

            });



            submitFareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (none.isChecked() || fareDiscounted1.isChecked()){
                        if (none.isChecked()){
                            editor.putString(fareDiscount, "none");
                        }else if (fareDiscounted1.isChecked()){
                            editor.putString(fareDiscount, "discounted");
                        }
                        editor.apply();
                        dialog.cancel();
                        overridePendingTransition(0, 0);
                    }else {
                        Toast.makeText(MainActivity.this, "Choose one for discount!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }
    public void checkInternetConnection(){
        if (NetworkUtils.isWifiConnected(getApplicationContext())) {

        }else {
            connectionManager = new ConnectionManager(MainActivity.this, editor);
            connectionManager.lostConnectionDialog(MainActivity.this);
        }
    }

}
