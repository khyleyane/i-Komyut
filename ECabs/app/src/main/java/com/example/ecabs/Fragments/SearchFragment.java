package com.example.ecabs.Fragments;


import static android.content.Context.MODE_PRIVATE;
import static com.example.ecabs.Activity.ProfileActivity.KEY_EMAIL;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecabs.R;
import com.example.ecabs.Utils.BottomNavigationUtils;
import com.example.ecabs.Utils.BottomSheetFragment;
import com.example.ecabs.Utils.TodaItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends BottomSheetFragment {

    Boolean clicked = true;
    private double latitude;
    private double longitude;
    private GoogleMap mapAPI;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME= "MyPreferences";
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final String userLoc = "userLoc";
    public static final String userDes = "userDes";
    private static final String cost = "cost";
    private int counter = 0;

    FirebaseDatabase database;
    DatabaseReference reference;

    int Id = 1;

    public SearchFragment(List<TodaItem> list) {
        super(list);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        preferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());


        String Username = preferences.getString(KEY_EMAIL, null);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");


        TextView clearText = view.findViewById(R.id.clearText);
        TextView currentLocBtn = view.findViewById(R.id.currentLocBtn);
        final EditText userLocation = view.findViewById(R.id.editText1);
        final EditText userDestination = view.findViewById(R.id.editText2);
        Button searchButton = view.findViewById(R.id.searchButton);
        LinearLayout search_anim = view.findViewById(R.id.search_anim);

        String text = currentLocBtn.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        currentLocBtn.setText(spannableString);



        currentLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call getLocationNameAsync to fetch the location name
                getLocationNameAsync(new LocationCallback() {
                    @Override
                    public void onLocationNameReceived(String locationName) {
                        // Inside this callback, you can use the locationName
                        if (locationName != null) {
                            // Set the locationName in the EditText when touched
                            userLocation.setText(locationName);
                        }
                    }
                });
            }
        });



        clearText.setOnClickListener(v -> {
            userLocation.setText(""); // Clear the text in editText1
            userDestination.setText(""); // Clear the text in editText2
            Toast.makeText(getActivity(), "Text cleared", Toast.LENGTH_SHORT).show();

        });

        final FrameLayout message = view.findViewById(R.id.messageSearch);
        final FrameLayout search = view.findViewById(R.id.search1);
        RadioButton radioButton1 = view.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        ImageView locUnderline = view.findViewById(R.id.locUnderline);
        ImageView desUnderline = view.findViewById(R.id.desUnderline);
        CheckBox saveSearch = view.findViewById(R.id.savedSearchCbox);

        if (Username != null){
            saveSearch.setEnabled(true);
            saveSearch.setVisibility(View.VISIBLE);

                Query checkUserDatabase = reference.orderByChild("users");
                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String userIDFromDB = snapshot.child(Username).child("history_id").getValue(String.class);
                            if (!userIDFromDB.equals(1)){
                                Id = Integer.valueOf(userIDFromDB) + 1;

                            }else {

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        }else {
            saveSearch.setEnabled(false);
            saveSearch.setVisibility(View.GONE);
        }

        radioButton1.setEnabled(false);
        radioButton2.setEnabled(false);

        View.OnClickListener clickListener = v -> {
            // Assuming you have EditTexts named editText1 and editText2
            String location = userLocation.getText().toString().trim();
            String destination = userDestination.getText().toString().trim();



            String buttonText = searchButton.getText().toString();

            if (v == searchButton) {

                if (buttonText.equals("NEXT")) {
                    if (location.isEmpty()) {
                        locUnderline.setImageResource(R.drawable.fragment_transit_underline_red);
                    } else {
                        locUnderline.setImageResource(R.drawable.fragment_transit_underline);
                    }

                    if (destination.isEmpty()) {
                        desUnderline.setImageResource(R.drawable.fragment_transit_underline_red);
                    } else {
                        desUnderline.setImageResource(R.drawable.fragment_transit_underline);
                    }

                    if (location.isEmpty() == false && destination.isEmpty() == false) {
                        search.setAlpha(0);
                        search.setElevation(-1);
                        message.setAlpha(1);
                        message.setElevation(2);
                        searchButton.setText("SEARCH");
                        editor.putString(userLoc,location);
                        editor.putString(userDes,destination);
                        editor.apply();
                        userLocation.setEnabled(false);
                        userDestination.setEnabled(false);
                        radioButton1.setEnabled(true);
                        radioButton2.setEnabled(true);

                        String userID = String.valueOf(Id);

                        if (saveSearch.isChecked()){
                            if (Username != null){
                                reference.child(Username).child("histories").child(userID).child("location").setValue(location);
                                reference.child(Username).child("histories").child(userID).child("destination").setValue(destination);
                                reference.child(Username).child("histories").child(userID).child("date").setValue(currentDate);
                                reference.child(Username).child("history_id").setValue(userID);

                            }

                        }


                    }
                }
                    else if (buttonText.equals("SEARCH")){

                        if (radioButton1.isChecked()) {
                            BottomNavigationUtils.selectBottomNavigationItem(requireActivity(), R.id.map);
                            editor.putString(cost,"FAST");
                            editor.apply();

                            // Replace the current fragment with the MapFragment
                            replaceFragment(new Maps_Fragment());
                        }
                            else if (radioButton2.isChecked()) {
                                BottomNavigationUtils.selectBottomNavigationItem(requireActivity(), R.id.map);
                                // Low-Cost Trip Code Here

                                editor.putString(cost,"LOW");
                                editor.apply();

                                // Pass the bundle to the MapFragment

                                // Replace the current fragment with the MapFragment
                                replaceFragment(new Maps_Fragment());

                            }
                    }
            }
        };
        searchButton.setOnClickListener(clickListener);

        TextView radioText1 = view.findViewById(R.id.radioText1);
        TextView radioText2 = view.findViewById(R.id.radioText2);

        ColorStateList originalButtonTint = radioButton1.getButtonTintList();
        int originalTextColor = radioButton1.getCurrentTextColor();

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioButton1.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkblue)));
                    radioButton1.setTextColor(getResources().getColor(R.color.darkblue));
                    radioText1.setTextColor(getResources().getColor(R.color.darkblue));
                } else {
                    radioButton1.setButtonTintList(originalButtonTint);
                    radioButton1.setTextColor(originalTextColor);
                    radioText1.setTextColor(originalTextColor);
                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioButton2.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkblue)));
                    radioButton2.setTextColor(getResources().getColor(R.color.darkblue));
                    radioText2.setTextColor(getResources().getColor(R.color.darkblue));
                } else {
                    radioButton2.setButtonTintList(originalButtonTint);
                    radioButton2.setTextColor(originalTextColor);
                    radioText2.setTextColor(originalTextColor);
                }
            }
        });

        return view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public static String getLocationName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String locationName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                locationName = address.getAddressLine(0); // You can customize the address format as needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationName;
    }

    public void getLocationNameAsync(LocationCallback callback) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where permissions are not granted.
            // You might want to request permissions here if needed.
            callback.onLocationNameReceived(null);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Once you have the location, call getLocationName
                        String locationName = getLocationName(requireContext(), latitude, longitude);

                        // Use the callback to pass the locationName to the caller
                        callback.onLocationNameReceived(locationName);
                    }
                }
            }
        });
    }

    // Define a callback interface in your class:
    public interface LocationCallback {
        void onLocationNameReceived(String locationName);
    }

}

