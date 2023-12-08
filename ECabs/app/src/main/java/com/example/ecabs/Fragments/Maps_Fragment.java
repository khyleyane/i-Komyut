package com.example.ecabs.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.example.ecabs.Activity.MainActivity.connection;
import static com.example.ecabs.Activity.MainActivity.fareDiscount;
import static com.example.ecabs.Activity.ProfileActivity.KEY_EMAIL;

import static io.realm.Realm.getApplicationContext;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecabs.Activity.MainActivity;
import com.example.ecabs.Activity.SaveLocationActivity;
import com.example.ecabs.Activity.SettingActivity;
import com.example.ecabs.Dijkstra.AddNode;
import com.example.ecabs.Dijkstra.Dijkstra;
import com.example.ecabs.Dijkstra.GetInitialAndFinalCoordinates;
import com.example.ecabs.Dijkstra.GraphToArray;
import com.example.ecabs.R;
import com.example.ecabs.Utils.BottomSheetFragment;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.InfoWindow;
import com.example.ecabs.Utils.MapMarkerUtils;
import com.example.ecabs.Utils.MapUtils;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.Utils.SQLHelper;
import com.example.ecabs.Utils.TODAUtils;
import com.example.ecabs.Utils.TODAUtils_2;
import com.example.ecabs.Utils.TodaItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Maps_Fragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    public static GoogleMap mapAPI;
    private boolean clicked;
    
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME= "MyPreferences";
    private static final String userLoc = "userLoc";
    private static final String userDes = "userDes";
    private static final String cost = "cost";
    private ConnectionManager connectionManager;
    private String location;
    private String destination;
    private String Cost;
    public static Polyline currentPolyline;
    public String TODA;
    private int zoom = 13;
    Cursor cursor = null;
    public int __global_start_node;
    public int __global_end_node;
    public String __global_old_start_node;
    public String __global_old_end_node;
    public int __global_maxRow0;
    public int __global_maxRow1;
    private String[][] __global_graphArray;
    SQLHelper dbHelper;
    SQLiteDatabase db;
    List<TodaItem> list = new ArrayList<>();
    double totalAmountOfFare;
    double totalDistance;
    TextView time;
    TextView totalFare;
    TextView bestInMode;
    String getEmail;
    String getFareDiscountStatus;

    int mode = 0;
    String con;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_, container, false);
        preferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        location = preferences.getString(userLoc, null);
        destination = preferences.getString(userDes, null);
        Cost = preferences.getString(cost, null);

        bestInMode = view.findViewById(R.id.bestInMode);
        con = preferences.getString(connection, null);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        getEmail = preferences.getString(KEY_EMAIL, null);
        getFareDiscountStatus = preferences.getString(fareDiscount, null);

        LinearLayout fareContainer = view.findViewById(R.id.fareContainer);

        if (location != null){
            fareContainer.setVisibility(View.VISIBLE);
        }else {
            fareContainer.setVisibility(View.GONE);
        }
        requestLocationPermission();

        //Bago to pre
        //Eto kung Best in Fare ba or Time and Distane
        LinearLayout searchContainer = view.findViewById(R.id.searchContainer);
        LinearLayout timedDisCon = view.findViewById(R.id.timeDistanceCon);

        searchContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        AutoCompleteTextView locationTxtField = view.findViewById(R.id.locationTxtField);
        AutoCompleteTextView destinationTxtField = view.findViewById(R.id.destinationTxtField);
        ImageView eastBackBtn = view.findViewById(R.id.eastBack);
        TextView clearBtn = view.findViewById(R.id.clearText);
        Button searchBtn = view.findViewById(R.id.searchButton);
        Button settingBtn = view.findViewById(R.id.settingBtn);
        LinearLayout searchContainer2 = view.findViewById(R.id.searchContainer1);

        String[] itemArray = getResources().getStringArray(R.array.LocationList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.custom_recent_item_layout, itemArray);
        destinationTxtField.setAdapter(arrayAdapter);
        locationTxtField.setAdapter(arrayAdapter);


        CheckBox saveSearchBtn = view.findViewById(R.id.savedSearchCbox);
        Button fastBtn = view.findViewById(R.id.fastestBtn);
        Button lowBtn = view.findViewById(R.id.lowBtn);
        TextView currentLocBtn = view.findViewById(R.id.currentLocBtn);

        clearBtn.setOnClickListener(v -> {
            locationTxtField.setText(""); // Clear the text in editText1
            destinationTxtField.setText(""); // Clear the text in editText2
            Toast.makeText(getActivity(), "Text cleared", Toast.LENGTH_SHORT).show();
        });

        if (location != null){
            locationTxtField.setText(location);
            destinationTxtField.setText(destination);
        }else {

        }
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
                            locationTxtField.setText(locationName);
                        }
                    }
                });
            }
        });


        int white = ContextCompat.getColor(requireContext(), R.color.white);
        int blue = ContextCompat.getColor(requireContext(), R.color.blue);

        if (getEmail != null){
            saveSearchBtn.setVisibility(View.VISIBLE);
        }else {
            saveSearchBtn.setVisibility(View.GONE);
        }

        fastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fastBtn.setTextColor(white);
                fastBtn.setBackgroundResource(R.drawable.fragment_search_search_button);
                lowBtn.setTextColor(blue);
                lowBtn.setBackgroundResource(R.drawable.fragment_setting_button_box);
                mode = 1;
            }
        });
        lowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lowBtn.setTextColor(white);
                lowBtn.setBackgroundResource(R.drawable.fragment_search_search_button);
                fastBtn.setTextColor(blue);
                fastBtn.setBackgroundResource(R.drawable.fragment_setting_button_box);
                mode = 2;
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        if (location == null){
            timedDisCon.setVisibility(View.GONE);
        }else {
            timedDisCon.setVisibility(View.VISIBLE);
        }
        Drawable pindrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_pin);
        Drawable searchdrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search);

        locationTxtField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){

                    searchContainer.setBackgroundResource(R.drawable.search_container);
                    timedDisCon.setVisibility(View.GONE);
                    settingBtn.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            locationTxtField.setHint("Type your Location");
                            locationTxtField.setCompoundDrawablesWithIntrinsicBounds(pindrawable, null, null, null);
                            searchContainer2.setVisibility(View.VISIBLE);
                            eastBackBtn.setVisibility(View.VISIBLE);

                        }
                    }, 10);
                }
            }
        });
        eastBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchContainer.setBackgroundResource(0);
                locationTxtField.setHint("Search");
                locationTxtField.setCompoundDrawablesWithIntrinsicBounds(searchdrawable, null, null, null);
                searchContainer2.setVisibility(View.GONE);
                eastBackBtn.setVisibility(View.GONE);
                hideKeyboard(view);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (location == null){
                            timedDisCon.setVisibility(View.GONE);
                        }else {
                            timedDisCon.setVisibility(View.VISIBLE);
                        }
                    }
                }, 10);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingBtn.setVisibility(View.VISIBLE);
                    }
                }, 200);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location1 = locationTxtField.getText().toString();
                String destination1 = destinationTxtField.getText().toString();

                if (location1.isEmpty() || destination1.isEmpty() || mode == 0){
                    Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }else {
                    searchContainer.setBackgroundResource(0);
                    locationTxtField.setCompoundDrawablesWithIntrinsicBounds(searchdrawable, null, null, null);
                    searchContainer2.setVisibility(View.GONE);
                    eastBackBtn.setVisibility(View.GONE);
                    hideKeyboard(view);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timedDisCon.setVisibility(View.VISIBLE);
                        }
                    }, 10);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingBtn.setVisibility(View.VISIBLE);
                        }
                    }, 200);


                    editor.putString(userLoc,location1);
                    editor.putString(userDes,destination1);
                    editor.apply();

                    if (mode == 1){
                        editor.putString(cost,"FAST");
                        editor.apply();
                        mode=0;
                    } else if (mode == 2) {
                        editor.putString(cost,"LOW");
                        editor.apply();
                        mode=0;
                    }

                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(0, 0);

                }

            }
        });

        //Eto yung time na bago makarating sa destination
        time = view.findViewById(R.id.timeAndDistance);

        //Eto yung distance from location to destination
        /*distance = view.findViewById(R.id.distance);*/

        totalFare = view.findViewById(R.id.totalFare);
        // Dito mo lalagay yung total fare pri setText mo lang

        //Eto yung para lumabas yung details
        //Sa BottomSheetFragment mo maseset yung nakakalagay dun sa see details
        Button seeDetailsBtn = view.findViewById(R.id.seeDetailsBtn);
        seeDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
        //hanggang dito lang

        return view;
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showBottomSheet() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(list);
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
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

    public void getLocationNameAsync(Maps_Fragment.LocationCallback callback) {
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapAPI = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();

        try {
        //Search result here

        if (Cost != null){
            if (!location.isEmpty() && !destination.isEmpty()){
                if (Cost.equals("FAST")){
                    searchLocation(requireContext(), location);
                    searchLocation(requireContext(), destination);

                    try {
                        startingScript(getLatitude(requireContext(), location), getLongitude(requireContext(), location), getLatitude(requireContext(), destination), getLongitude(requireContext(), destination));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    totalFare.setText("₱" + String.valueOf(totalAmountOfFare));
                    time.setText(estimatedTimeDuration(totalDistance) + " min " + "(" + String.valueOf(convertToKm(totalDistance)) + "km" + ")");
                    bestInMode.setText("Fastest Route");

                    closedatabase();

                } else if (Cost.equals("LOW")) {
                    searchLocation(requireContext(), location);
                    searchLocation(requireContext(), destination);

                    try {
                        startingScript(getLatitude(requireContext(), location), getLongitude(requireContext(), location), getLatitude(requireContext(), destination), getLongitude(requireContext(), destination));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    closedatabase();

                    totalFare.setText("₱" + String.valueOf(totalAmountOfFare));
                    time.setText(estimatedTimeDuration(totalDistance) + " min " + "(" + String.valueOf(convertToKm(totalDistance)) + "km" + ")");
                    bestInMode.setText("Low-cost Route");
                }
            }else {
                showLocationNotFoundErrorDialog();
            }
        }else {
           /* showLocationNotFoundErrorDialog();*/
        }

        if (getArguments() != null) {
            clicked = getArguments().getBoolean("JEEP", false);
            if (clicked) {
                try {
                    double latitude1 = 14.277022; // Replace with the second latitude value
                    double longitude1 = 121.123570; // Replace with the second longitude value
                    String title1 = "JEEP - Cabuyao Terminal";
                    pinLocationOnMap(latitude1, longitude1, title1);

                    List<LatLng> JeepTodaLL = TODAUtils.getInstance().getJeepLatLngList();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(JeepTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.blue));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception or log it to understand the problem better.
            }
            setInfoView();

            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("POSATODA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.POSATODA(mapAPI, requireContext());
                    //Bago to pre, meron sa lahat ng TODA nito
                    List<LatLng> PosaTodaLL = TODAUtils_2.getInstance().getPosaLatLngList();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(PosaTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.red));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);

                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }

                setInfoView();

            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("BBTODA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.BBTODA(mapAPI, requireContext());
                    List<LatLng> bbTodaLL = TODAUtils.getInstance().getBbtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(bbTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.bbtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);

                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }

                //setting the infoview

                TODA = "BBTODA";

                setInfoView();

            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("BTATODA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.BTATODA(mapAPI, requireContext());
                    List<LatLng> btaTodaLL = TODAUtils.getInstance().getBtatodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(btaTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.btatoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }


                //setting the infoview
                setInfoView();

            }

        }

        if (getArguments() != null) {
            clicked = getArguments().getBoolean("OSPOTODA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.OSPOTODA(mapAPI, requireContext());
                    List<LatLng> OspoTodaLL = TODAUtils_2.getInstance().getOspotodaLL();
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(OspoTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.ospotoda));
                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }
                //setting the infoview
                setInfoView();
            }
        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("CMS", false);
            if (clicked) {
                try {
                    MapMarkerUtils.CMSTODA(mapAPI, requireContext());
                    List<LatLng> cmsatodaLL = TODAUtils_2.getInstance().getCmsatodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(cmsatodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.cmsatoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }
                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("MACA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.MACATODA(mapAPI, requireContext());
                    List<LatLng> MAcatodaLL = TODAUtils.getInstance().getMacaLatLngList();
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(MAcatodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.macatoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }
                //setting the infoview
                setInfoView();
            }
        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("BMBG", false);
            if (clicked) {
                try {
                    MapMarkerUtils.BMBGTODA(mapAPI, requireContext());
                    List<LatLng> bmbgTodaLL = TODAUtils.getInstance().getBmbgtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(bmbgTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.bmbgtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);


                }catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("CSV", false);
            if (clicked) {
                try {
                    MapMarkerUtils.CSVTODA(mapAPI, requireContext());
                    List<LatLng> csvTodaLL = TODAUtils_2.getInstance().getCsvtodaLL();
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(csvTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.csvtoda));
                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception or log it to understand the problem better.
                }
                //setting the infoview
                setInfoView();
            }
        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("SJV7", false);
            if (clicked) {
                try {
                    MapMarkerUtils.SJV7TODA(mapAPI, requireContext());
                    List<LatLng> sjv7TodaLL = TODAUtils.getInstance().getSjv7todaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(sjv7TodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.sjv7toda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("SJB", false);
            if (clicked) {
                try {
                    MapMarkerUtils.SJBTODA(mapAPI, requireContext());
                    List<LatLng> sjbTodaLL = TODAUtils.getInstance().getSjbtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(sjbTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.sjbtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("SICALA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.SICALATODA(mapAPI, requireContext());
                    List<LatLng> sicalaTodaLL = TODAUtils.getInstance().getSicalatodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(sicalaTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.sicalatoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);

                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("PUD", false);
            if (clicked) {
                try {
                    MapMarkerUtils.PUDTODA(mapAPI, requireContext());
                    List<LatLng> pudTodaLL = TODAUtils.getInstance().getPudtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(pudTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.pudtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);

                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("HV", false);
            if (clicked) {
                try {
                    MapMarkerUtils.HVTODA(mapAPI, requireContext());
                    List<LatLng> hvtodaLL = TODAUtils.getInstance().getHvLatLngList();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(hvtodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.hvtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("DOV", false);
            if (clicked) {
                try {
                    MapMarkerUtils.DOVTODA(mapAPI, requireContext());
                    List<LatLng> dovTodaLL = TODAUtils.getInstance().getDovtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(dovTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.dovtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("KA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.KATODA(mapAPI, requireContext());

                    List<LatLng> katodaLL = TODAUtils_2.getInstance().getKatodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(katodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.katoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);

                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("MCCH", false);
            if (clicked) {
                try {
                    MapMarkerUtils.MCCHTODA(mapAPI, requireContext());
                    List<LatLng> mcchTodaLL = TODAUtils.getInstance().getMcchtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(mcchTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.mcchtoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("BO", false);
            if (clicked) {
                try {
                    MapMarkerUtils.BOTODA(mapAPI, requireContext());
                    List<LatLng> boTodaLL = TODAUtils.getInstance().getBotodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(boTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.botoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("MACOPA", false);
            if (clicked) {
                try {
                    MapMarkerUtils.MACOPASTR(mapAPI, requireContext());
                    List<LatLng> macopaTodaLL = TODAUtils.getInstance().getMacopastrtodaLL();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(macopaTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.macopastr));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }


                //setting the infoview
                setInfoView();
            }

        }
        if (getArguments() != null) {
            clicked = getArguments().getBoolean("LNS", false);
            if (clicked) {
                try {
                    MapMarkerUtils.LNSTODA(mapAPI, requireContext());
                    List<LatLng> lnsTodaLL = TODAUtils.getInstance().getLnsLatLngList();

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(lnsTodaLL) // Add the list of LatLng coordinates
                            .width(15) // Set the width of the polyline in pixels
                            .color(ContextCompat.getColor(requireContext(), R.color.lnstoda));

                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                //setting the infoview
                setInfoView();
            }

        }
        } catch (Exception e) {
            // Handle exceptions here
            e.printStackTrace();
          Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT);
        }
    }
    public void closedatabase() {
        // Close the database after all operations
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Close the database
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
    public void startingScript(double latUser, double lngUser, double lat_endposition, double lng_endposition) throws JSONException {

        try {
            // delete temporary record DB
            deleteTemporaryRecord();

            // reset google map
            mapAPI.clear();

            // convert graph from DB to an Array; graph[][]
            GraphToArray DBGraph = new GraphToArray();
            __global_graphArray = DBGraph.convertToArray(requireContext()); // return graph[][] Array

            // get the maximum row in the temporary DB
            maxRowDB();

            // GET THE STARTING COORDINATE AROUND THE NODE
            // the starting coordinate is then converted to the starting node
            // return __global_simpul_awal, __global_graphArray[][]
            // ==========================================
            GetInitialAndFinalCoordinates start_coordinate_route = new GetInitialAndFinalCoordinates();
            getStartEndNodesRoute(start_coordinate_route, latUser, lngUser, "start");

            // GET THE ENDING COORDINATE AROUND THE NODE
            // the ending coordinate is then converted to the ending node
            // return __global_simpul_akhir, __global_graphArray[][]
            // ==========================================
            GetInitialAndFinalCoordinates destination_coordinate_route = new GetInitialAndFinalCoordinates();
            getStartEndNodesRoute(destination_coordinate_route, lat_endposition, lng_endposition, "end");

            // DIJKSTRA'S ALGORITHM
            // ==========================================
            Dijkstra algo = new Dijkstra();
            algo.shortestPath(__global_graphArray, __global_start_node, __global_end_node);

            // no result for Dijkstra's algorithm
            if (algo.status == "die") {
                showLocationNotFoundErrorDialog();
            } else {
                // return the shortest path; example 1->5->6->7
                String[] exp = algo.shortestPath1.split("->");

                // DRAW PUBLIC TRANSPORTATION ROUTE
                // =========================================
                drawRoute(algo.shortestPath1, exp);
            }
        }catch (Exception e) {
            e.printStackTrace();
            // Check if the exception is an ANR issue
            if (isANRIssue(e)) {
                // Show the location not found dialog or handle the ANR issue
                showLocationNotFoundErrorDialog();
            }
        }
    }
    public void drawRoute(String algo, String[] exp) throws JSONException {

            int start = 0;

            // DRAW THE ROUTE
            // ======================
            //dbHelper = new SQLHelper(requireContext());
            db = dbHelper.getReadableDatabase();

            try {
                for (int i = 0; i < exp.length - 1; i++) {

                    ArrayList<LatLng> lat_lng = new ArrayList<LatLng>();

                    cursor = db.rawQuery("SELECT route, starting FROM graph where starting =" + exp[start] + " and ending =" + exp[(++start)], null);
                    cursor.moveToFirst();

                    // get Lat, Lng coordinates from the coordinate field (3)
                    String json = cursor.getString(0).toString();

                    // get JSON
                    JSONObject jObject = new JSONObject(json);
                    JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");

                    // get coordinate JSON
                    for (int w = 0; w < jArrCoordinates.length(); w++) {

                        JSONArray latlngs = jArrCoordinates.getJSONArray(w);
                        Double lats = latlngs.getDouble(0);
                        Double lngs = latlngs.getDouble(1);

                        lat_lng.add(new LatLng(lats, lngs));
                    }

                    //node.add(Integer.valueOf(exp[start]));
                    PolylineOptions normalRoute = new PolylineOptions();

                    // create a route
                    for (int p = 0; p < lat_lng.size(); p++) {
                        normalRoute.add(lat_lng.get(p));
                    }

                    normalRoute.width(10)
                            .color(getColor(cursor.getString(1)))
                            .geodesic(true);

                    mapAPI.addPolyline(normalRoute);
                }

                // CREATE MARKERS FOR YOUR POSITION AND DESTINATION POSITION
                // ======================
                // your position

                LatLng pos = new LatLng(getLatitude(requireContext(), location), getLongitude(requireContext(), location));
                mapAPI.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(location)
                        .snippet("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                LatLng endx = new LatLng(getLatitude(requireContext(), destination), getLongitude(requireContext(), destination));

                // destination position
                mapAPI.addMarker(new MarkerOptions()
                        .position(endx)
                        .title(destination)
                        .snippet("Destination position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                // DETERMINE THE TYPE OF PUBLIC TRANSPORTATION PASSING THROUGH THE ROUTE
                // ==========================================================
                // for example, exp[] = 1->5->6->7
                int m = 0;

                String[] start1 = __global_old_start_node.split("-"); // for example, 4-5
                String[] end = __global_old_end_node.split("-"); // for example, 8-7

                int change_a = 0;
                int change_b = 0;
                int dijkstra_start_node = Integer.parseInt(exp[0]); // Default value if parsing fails

                String mergedAllNodes = "";
                Map<String, ArrayList> publicTransportList = new HashMap<String, ArrayList>();
                ArrayList<Integer> transportNodesList = new ArrayList<Integer>();
                ArrayList<Integer> starting = new ArrayList<Integer>();
                ArrayList<Integer> ending = new ArrayList<Integer>();

                // find the old nodes before coordinates are split
                // for example, 4-5 is split into 4-6-5, so the old starting node = 5, old ending node = 4
                for (int e = 0; e < (exp.length - 1); e++) {

                    if (e == 0) { // starting

                        // executed if the algo result has only 2 nodes, example: 4->5
                        if (exp.length == 2 /* 2 nodes (4-5)*/) {

                            // there are new nodes at the beginning (10) and at the end (11), example 10->11
                            if (exp[0].equals(String.valueOf(__global_maxRow0)) && exp[1].equals(String.valueOf(__global_maxRow1))) {

                                if (String.valueOf(__global_maxRow0).equals(end[0])) {
                                    change_b = Integer.parseInt(end[1]);
                                } else {
                                    change_b = Integer.parseInt(end[0]);
                                }

                                if (String.valueOf(change_b).equals(start1[0])) {
                                    change_a = Integer.parseInt(start1[1]);
                                } else {
                                    change_a = Integer.parseInt(start1[0]);
                                }
                            } else {
                                // there are new nodes at the beginning (10), example 10->5
                                // then find the old starting node
                                if (exp[0].equals(String.valueOf(__global_maxRow0))) {

                                    if (exp[1].equals(start1[1])) {
                                        change_a = Integer.parseInt(start1[0]);
                                    } else {
                                        change_a = Integer.parseInt(start1[1]);
                                    }
                                    change_b = Integer.parseInt(exp[1]);
                                }
                                // there are new nodes at the end (10), example 5->10
                                // then find the old ending node
                                else if (exp[1].equals(String.valueOf(__global_maxRow0))) {

                                    if (exp[0].equals(end[0])) {
                                        change_b = Integer.parseInt(end[1]);
                                    } else {
                                        change_b = Integer.parseInt(end[0]);
                                    }
                                    change_a = Integer.parseInt(exp[0]);
                                }
                                // no addition of nodes at all
                                else {
                                    change_a = Integer.parseInt(exp[0]);
                                    change_b = Integer.parseInt(exp[1]);
                                }
                            }
                        }
                        // algo result has more than 2 nodes: 4->5->8->7-> etc ..
                        else {
                            if (exp[1].equals(start1[1])) { // 5 == 5
                                change_a = Integer.parseInt(start1[0]); // result 4
                            } else {
                                change_a = Integer.parseInt(start1[1]); // result 5
                            }

                            change_b = Integer.parseInt(exp[++m]);
                        }
                    } else if (e == (exp.length - 2)) { // ending

                        if (exp[(exp.length - 2)].equals(end[1])) { // 7 == 7
                            change_b = Integer.parseInt(end[0]); // result 8
                        } else {
                            change_b = Integer.parseInt(end[1]); // result 7
                        }

                        change_a = Integer.parseInt(exp[m]);

                    } else { // middle nodes
                        change_a = Integer.parseInt(exp[m]);
                        change_b = Integer.parseInt(exp[++m]);
                    }

                    mergedAllNodes += "," + change_a + "-" + change_b + ","; // ,1-5,
                    String mergedNodes = "," + change_a + "-" + change_b + ","; // ,1-5,

                    starting.add(change_a);
                    ending.add(change_b);

                    cursor = db.rawQuery("SELECT no_route FROM public_transit where id = '" + change_a + "'", null);
                    cursor.moveToFirst();

                    ArrayList<String> transportList = new ArrayList<String>();

                    for (int ae = 0; ae < cursor.getCount(); ae++) {
                        cursor.moveToPosition(ae);
                        transportList.add(cursor.getString(0).toString());
                    }

                    publicTransportList.put("transport" + e, transportList);

                    // add transport nodes
                    transportNodesList.add(Integer.parseInt(exp[e]));

                }

                String replace_route = mergedAllNodes.replace(",,", ",");

                try (Cursor cursor1 = db.rawQuery("SELECT * FROM public_transit where string like '%" + replace_route + "%'", null)) {
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        if (cursor1.getCount() > 0) {
                            String transport = cursor1.getString(1);

                            // Get coordinates
                            try (Cursor coordinatesCursor = db.rawQuery("SELECT route, weight_distance, weight_fare FROM graph where starting = '" + dijkstra_start_node + "'", null)) {
                                if (coordinatesCursor != null && coordinatesCursor.moveToFirst()) {
                                    String json_coordinates = coordinatesCursor.getString(0);

                                    // Manipulate JSON
                                    try {
                                        JSONObject jObject = new JSONObject(json_coordinates);
                                        JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");
                                        JSONArray latlngs = jArrCoordinates.getJSONArray(0);

                                        // First coordinates
                                        Double lats = latlngs.getDouble(0);
                                        Double lngs = latlngs.getDouble(1);

                                        // Add marker to the map
                                        MapUtils.addCustomMarkerToMap(
                                                mapAPI,
                                                requireContext(),
                                                getMarkerIcon(String.valueOf(starting.get(0))),
                                                new LatLng(lats, lngs),
                                                getTodaName(String.valueOf(starting.get(0))),
                                                transport.replace("[", "").replace("]", "")
                                        );
                                        TodaItem item = new TodaItem(getTodaName(String.valueOf(starting.get(0))), transport.replace("[", "").replace("]", ""), discountFare(coordinatesCursor.getDouble(2)));
                                        list.add(item);

                                        return;
                                    } catch (JSONException e) {
                                        e.printStackTrace(); // Handle JSON parsing exception
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace(); // Handle cursor exception for coordinates
                            }
                        } else {
                            // Handle the case where there are no rows in the cursor
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Handle cursor exception for public transit
                }

                // there are 2 or more public transports passing through the route from start to end
                int transportCount = 0;
                int indexOrder = 0;
                int transportNodesIndex = 1;
                int transportListLength = publicTransportList.size();
                Map<String, ArrayList> fixedTransport = new HashMap<String, ArrayList>();

                for (int en = 0; en < transportListLength; en++) {

                    // temporary before retainAll()
                    ArrayList<String> temps = new ArrayList<String>();
                    for (int u = 0; u < publicTransportList.get("transport0").size(); u++) {
                        temps.add(publicTransportList.get("transport0").get(u).toString());
                    }

                    if (en > 0) {
                        ArrayList currentList1 = publicTransportList.get("transport0");
                        ArrayList nextList1 = publicTransportList.get("transport" + en);

                        // intersection
                        currentList1.retainAll(nextList1);

                        if (currentList1.size() > 0) {

                            transportNodesList.remove(transportNodesIndex);
                            --transportNodesIndex;

                            publicTransportList.remove("transport" + en);

                            if (en == (transportListLength - 1)) {

                                ArrayList<String> tempInside = new ArrayList<String>();
                                for (int es = 0; es < currentList1.size(); es++) {
                                    tempInside.add(currentList1.get(es).toString());
                                }

                                fixedTransport.put("transportFix" + indexOrder, tempInside);
                                ++indexOrder;
                            }
                        } else if (currentList1.size() == 0) {

                            fixedTransport.put("transportFix" + indexOrder, temps);

                            ArrayList<String> tempInside = new ArrayList<String>();
                            for (int es = 0; es < nextList1.size(); es++) {
                                tempInside.add(nextList1.get(es).toString());
                            }

                            publicTransportList.get("transport0").clear();
                            publicTransportList.put("transport0", tempInside);

                            publicTransportList.remove("transport" + en);

                            ++indexOrder;

                            if (en == (transportListLength - 1)) {

                                ArrayList<String> tempInside2 = new ArrayList<String>();
                                for (int es = 0; es < nextList1.size(); es++) {
                                    tempInside2.add(nextList1.get(es).toString());
                                }

                                fixedTransport.put("transportFix" + indexOrder, tempInside2);
                                ++indexOrder;
                            }
                        }

                        ++transportNodesIndex;
                    }
                }

                List<Integer> visibleNode = new ArrayList<>();
                boolean firstIdInRangeProcessed = false;
                for (int i = 0; i < starting.size(); i++) {
                    Cursor coordinatesCursor = db.rawQuery("SELECT route, weight_distance FROM graph where starting = '" + starting.get(i) + "' and ending = '" + ending.get(i) + "'", null);
                    coordinatesCursor.moveToPosition(0);
                    totalDistance += coordinatesCursor.getDouble(1);

                    int idNum = starting.get(i);
                    if (idNum >= 66 && idNum <= 80 && !firstIdInRangeProcessed) {
                        visibleNode.add(idNum);
                        firstIdInRangeProcessed = true;
                    } else if (firstIdInRangeProcessed && idNum >= 66 && idNum <= 80) {
                        visibleNode.add(null);
                    } else if (idNum < 66 || idNum > 80) {
                        visibleNode.add(idNum);
                    }
                }

                for (int r = 0; r < visibleNode.size(); r++) {
                    Cursor transitCursor = db.rawQuery("SELECT no_route FROM public_transit where id = '" + visibleNode.get(r) + "'", null);

                    if (transitCursor.moveToFirst()) {
                        // Check if the cursor has at least one row
                        // Assuming no_route is a String column
                        String noRoute = transitCursor.getString(0);
                        // get coordinates of transport nodes
                        Cursor coordinatesCursor = db.rawQuery("SELECT route, weight_fare FROM graph where starting = '" + visibleNode.get(r) + "' and ending = '" + ending.get(r) + "'", null);

                        if (coordinatesCursor.moveToFirst()) {
                            // Check if the cursor has at least one row
                            // Assuming route is a String column
                            String json = coordinatesCursor.getString(0);

                            // get JSON
                            JSONObject jObject = new JSONObject(json);
                            JSONArray jArrCoordinates = jObject.getJSONArray("coordinates");

                            // Check if the JSON array is not empty
                            if (jArrCoordinates.length() > 0) {
                                // get first coordinate from JSON
                                JSONArray latlngs = jArrCoordinates.getJSONArray(0);
                                Double lats = latlngs.getDouble(0);
                                Double lngs = latlngs.getDouble(1);

                                LatLng transportNode = new LatLng(lats, lngs);

                                MapUtils.addCustomMarkerToMap(
                                        mapAPI,
                                        requireContext(),
                                        getMarkerIcon(String.valueOf(visibleNode.get(r))),
                                        transportNode,
                                        getTodaName(String.valueOf(visibleNode.get(r))),
                                        noRoute
                                );

                                if(coordinatesCursor.getDouble(1) != 0.0) {
                                    TodaItem item = new TodaItem(getTodaName(String.valueOf(visibleNode.get(r))), noRoute, discountFare(coordinatesCursor.getDouble(1)));
                                    list.add(item);
                                }
                                totalAmountOfFare += discountFare(coordinatesCursor.getDouble(1));
                            } else {
                                // Handle the case when the coordinates JSON array is empty
                                Log.w("Empty Coordinates", "Coordinates JSON array is empty.");

                            }
                        } else {
                            // Handle the case when the coordinatesCursor is empty
                            Log.w("Empty Cursor", "Coordinates cursor is empty.");
                        }
                        //coordinatesCursor.close(); // Close the coordinatesCursor when you're done with it
                    } else {
                        // Handle the case when the transitCursor is empty
                        Log.w("Empty Cursor", "Transit cursor is empty.");
                    }
                    //transitCursor.close(); // Close the transitCursor when you're done with it
                }
                setInfoView();
            }catch (Exception e) {
                e.printStackTrace();
                showLocationNotFoundErrorDialog();
                // Check if the exception is an ANR issue
                if (isANRIssue(e)) {
                    // Show the location not found dialog or handle the ANR issue
                    showLocationNotFoundErrorDialog();
                }
            }finally {
                // Close the Cursor in a finally block to ensure it gets closed
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    public void getStartEndNodesRoute(GetInitialAndFinalCoordinates objects, double latx, double lngx, String statusObject) throws JSONException{

        // return JSON index of coordinate position, nodes0, nodes1
        JSONObject jStart = objects.GetNodes(latx, lngx, requireContext());

        // JSON index
        String status = jStart.getString("status");
        int node_start0 = jStart.getInt("node_start0");
        int node_start1 = jStart.getInt("node_start1");
        int coordinate_json_index = jStart.getInt("index_coordinate_json");


        int fixed_start_node = 0;

        // if the coordinates are right above the node position
        // then there is no need to add new nodes
        if(status.equals("no_path")){

            // determine the starting or ending node closest to the user's position
            if(coordinate_json_index == 0){ // starting
                fixed_start_node = node_start0;
            }else{ // ending
                fixed_start_node = node_start1;
            }

            if(statusObject == "start"){

                // return
                __global_old_start_node = node_start0 + "-" + node_start1;
                __global_start_node = fixed_start_node; // for example, 0
            }else{

                // return
                __global_old_end_node = node_start0 + "-" + node_start1;
                __global_end_node = fixed_start_node; // for example, 0
            }

        }
        // if the coordinates are between node 5 and node 4 or node 4 and node 5
        // then new nodes need to be added
        else if(status.equals("double_path")){

            // return
            if(statusObject == "start"){

                // find nodes (5,4) and (4-5) in Add_node.java
                AddNode obj_add = new AddNode();
                obj_add.doubleNode(node_start0, node_start1, coordinate_json_index,
                        requireContext(), __global_graphArray, 401
                ); // 401: new row id

                // return
                __global_old_start_node = obj_add.old_node;
                __global_start_node = obj_add.new_node; // for example, 6
                __global_graphArray = obj_add.modified_graph; // graph[][]

            }else{

                // find nodes (5,4) and (4-5) in Add_node.java
                AddNode obj_add = new AddNode();
                obj_add.doubleNode(node_start0, node_start1, coordinate_json_index,
                        requireContext(), __global_graphArray, 501
                ); // 501: new row id

                // return
                __global_old_end_node = obj_add.old_node;
                __global_end_node = obj_add.new_node; // for example, 4
                __global_graphArray = obj_add.modified_graph; // graph[][]

            }

        }
        // if the coordinates are only between node 5 and node 4
        // then new nodes need to be added
        else if(status.equals("single_path")){

            if(statusObject == "start"){

                // find nodes (5,4) in Add_node.java
                AddNode obj_add1 = new AddNode();
                obj_add1.singleNode(node_start0, node_start1, coordinate_json_index,
                        requireContext(), __global_graphArray, 401
                ); // 401: new row id

                // return
                __global_old_start_node = obj_add1.old_node;
                __global_start_node = obj_add1.new_node; // for example, 6
                __global_graphArray = obj_add1.modified_graph; // graph[][]

            }else{

                // find nodes (5,4) in Add_node.java
                AddNode obj_add1 = new AddNode();
                obj_add1.singleNode(node_start0, node_start1, coordinate_json_index,
                        requireContext(), __global_graphArray, 501
                ); // 501: new row id

                // return
                __global_old_end_node = obj_add1.old_node;
                __global_end_node = obj_add1.new_node; // for example, 4
                __global_graphArray = obj_add1.modified_graph; // graph[][]
            }

        }
    }
    public void deleteTemporaryRecord(){

        try {
            //Check whether the variable dbHelper is null
            if (dbHelper == null) {
                dbHelper = new SQLHelper(requireContext()); // Initialize dbHelper if not already done
            }

            // delete DB
            //final SQLiteDatabase dbDelete = dbHelper.getWritableDatabase();
            db = dbHelper.getWritableDatabase();

            // Perform operations on the database
            // delete temporary DB records
            for(int i = 0; i < 4; i++){
                // delete additional starting nodes, starting from id 401,402,403,404
                String deleteQuery_ = "DELETE FROM graph where id ='"+ (401+i) +"'";
                db.execSQL(deleteQuery_);

                // delete additional destination nodes, starting from id 501,502,503,504
                String deleteQuery = "DELETE FROM graph where id ='"+ (501+i) +"'";
                db.execSQL(deleteQuery);
            }

            // Don't forget to close the database when you're done
            //db.close();
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }finally {
            //Close the database connection in a finally block to ensure it happens even if an exception occurs.
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    public void maxRowDB(){
        //SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        db = dbHelper.getReadableDatabase();

        try {
            cursor = db.rawQuery("SELECT max(starting), max(ending) FROM graph", null);
            cursor.moveToFirst();
            int max_node_db = 0;
            int max_start_node_db = Integer.parseInt(cursor.getString(0).toString());
            int max_destination_node_db = Integer.parseInt(cursor.getString(1).toString());

            if (max_start_node_db >= max_destination_node_db) {
                max_node_db = max_start_node_db;
            } else {
                max_node_db = max_destination_node_db;
            }

            // return
            __global_maxRow0 = (max_node_db + 1);
            __global_maxRow1 = (max_node_db + 2);

        } finally {
            // Close the Cursor in a finally block to ensure it gets closed
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
    public int getColor(String id) {
        int todaColor = 0;

        if("0".equals(id) || "1".equals(id) || "2".equals(id)) {
            todaColor = Color.parseColor("#FF993c1d");
        }
        if("3".equals(id) || "4".equals(id) || "5".equals(id)) {
            todaColor = Color.parseColor("#9dd089");
        }
        if("6".equals(id) || "7".equals(id) || "8".equals(id) || "9".equals(id) || "10".equals(id) || "11".equals(id)) {
            todaColor = Color.parseColor("#a3d393");
        }
        if("12".equals(id) || "13".equals(id)) {
            todaColor = Color.parseColor("#ef5da2");
        }
        if("14".equals(id) || "15".equals(id)) {
            todaColor = Color.parseColor("#640723");
        }
        if("16".equals(id)) {
            todaColor = Color.parseColor("#d1c0a8");
        }
        if("17".equals(id) || "18".equals(id) || "19".equals(id) || "20".equals(id)) {
            todaColor = Color.parseColor("#ababab");
        }
        if("21".equals(id) || "22".equals(id) || "23".equals(id) || "24".equals(id) || "27".equals(id)) {
            todaColor = Color.parseColor("#073b3a");
        }
        if("28".equals(id) || "29".equals(id) || "30".equals(id) || "32".equals(id) || "33".equals(id)) {
            todaColor = Color.parseColor("#252466");
        }
        if("33".equals(id) || "34".equals(id) || "35".equals(id) || "36".equals(id) || "37".equals(id) || "38".equals(id)) {
            todaColor = Color.parseColor("#fed700");
        }
        if("39".equals(id) || "40".equals(id) || "41".equals(id) || "42".equals(id)) {
            todaColor = Color.parseColor("#cd3a7b");
        }
        if("43".equals(id) || "44".equals(id) || "45".equals(id) || "46".equals(id)) {
            todaColor = Color.parseColor("#592e90");
        }
        if("47".equals(id) || "48".equals(id) || "49".equals(id) || "50".equals(id) || "51".equals(id) || "52".equals(id)) {
            todaColor = Color.parseColor("#81c9ef");
        }
        if("53".equals(id) || "54".equals(id) || "55".equals(id)) {
            todaColor = Color.parseColor("#3953a4");
        }
        if("56".equals(id) || "57".equals(id) || "58".equals(id) || "59".equals(id)) {
            todaColor = Color.parseColor("#b9529f");
        }
        if("60".equals(id) || "61".equals(id)) {
            todaColor = Color.parseColor("#f58220");
        }
        if("62".equals(id)) {
            todaColor = Color.parseColor("#fa0202");
        }
        if("63".equals(id) || "64".equals(id) || "65".equals(id)) {
            todaColor = Color.parseColor("#f7ec13");
        }
        if("66".equals(id) || "67".equals(id) || "68".equals(id) || "69".equals(id) || "70".equals(id) || "71".equals(id) || "72".equals(id) || "73".equals(id) || "74".equals(id) || "75".equals(id) || "76".equals(id) || "77".equals(id) || "78".equals(id) || "79".equals(id) || "80".equals(id)) {
            todaColor = Color.parseColor("#69a1ff");
        }
        return todaColor;
    }
    public String getTodaName(String id) {
        String todaName = null;

        if("0".equals(id) || "1".equals(id) || "2".equals(id)) {
            todaName = "KATODA";
        }
        if("3".equals(id) || "4".equals(id) || "5".equals(id)) {
            todaName = "LNSTODA";
        }
        if("6".equals(id) || "7".equals(id) || "8".equals(id) || "9".equals(id) || "10".equals(id) || "11".equals(id)) {
            todaName = "CSVTODA";
        }
        if("12".equals(id) || "13".equals(id)) {
            todaName = "DOVTODA";
        }
        if("14".equals(id) || "15".equals(id)) {
            todaName = "HVTODA";
        }
        if("16".equals(id)) {
            todaName = "BOTODA";
        }
        if("17".equals(id) || "18".equals(id) || "19".equals(id) || "20".equals(id)) {
            todaName = "PUDTODA";
        }
        if("21".equals(id) || "22".equals(id) || "23".equals(id) || "24".equals(id) || "27".equals(id)) {
            todaName = "SICALATODA";
        }
        if("28".equals(id) || "29".equals(id) || "30".equals(id) || "32".equals(id) || "33".equals(id)) {
            todaName = "BMBGTODA";
        }
        if("33".equals(id) || "34".equals(id) || "35".equals(id) || "36".equals(id) || "37".equals(id) || "38".equals(id)) {
            todaName = "MCCHTODAI";
        }
        if("39".equals(id) || "40".equals(id) || "41".equals(id) || "42".equals(id)) {
            todaName = "SJV7TODA";
        }
        if("43".equals(id) || "44".equals(id) || "45".equals(id) || "46".equals(id)) {
            todaName = "MACATODA";
        }
        if("47".equals(id) || "48".equals(id) || "49".equals(id) || "50".equals(id) || "51".equals(id) || "52".equals(id)) {
            todaName = "BBTODA";
        }
        if("53".equals(id) || "54".equals(id) || "55".equals(id)) {
            todaName = "BTATODA";
        }
        if("56".equals(id) || "57".equals(id) || "58".equals(id) || "59".equals(id)) {
            todaName = "SJBTODA";
        }
        if("60".equals(id) || "61".equals(id)) {
            todaName = "MACOPASTRTODA";
        }
        if("62".equals(id)) {
            todaName = "POSATODA";
        }
        if("63".equals(id) || "64".equals(id) || "65".equals(id)) {
            todaName = "OSPOTODA";;
        }
        if("66".equals(id) || "67".equals(id) || "68".equals(id) || "69".equals(id) || "70".equals(id) || "71".equals(id) || "72".equals(id) || "73".equals(id) || "74".equals(id) || "75".equals(id) || "76".equals(id) || "77".equals(id) || "78".equals(id) || "79".equals(id) || "80".equals(id)) {
            todaName = "JEEPNEY";
        }
        return todaName;
    }
    public int getMarkerIcon(String id) {
        int markerIcon = 0;

        if("0".equals(id) || "1".equals(id) || "2".equals(id)) {
            markerIcon = R.drawable.ka_toda;
        }
        if("3".equals(id) || "4".equals(id) || "5".equals(id)) {
            markerIcon = R.drawable.lns_toda;
        }
        if("6".equals(id) || "7".equals(id) || "8".equals(id) || "9".equals(id) || "10".equals(id) || "11".equals(id)) {
            markerIcon = R.drawable.csv_toda;
        }
        if("6".equals(id) || "7".equals(id) || "8".equals(id) || "9".equals(id) || "10".equals(id) || "11".equals(id)) {
            markerIcon = R.drawable.csv_toda;
        }
        if("12".equals(id) || "13".equals(id)) {
            markerIcon = R.drawable.dov_toda;
        }
        if("14".equals(id) || "15".equals(id)) {
            markerIcon = R.drawable.hv_toda;
        }
        if("16".equals(id)) {
            markerIcon = R.drawable.bo_toda;
        }
        if("17".equals(id) || "18".equals(id) || "19".equals(id) || "20".equals(id)) {
            markerIcon = R.drawable.pud_toda;
        }
        if("21".equals(id) || "22".equals(id) || "23".equals(id) || "24".equals(id) || "27".equals(id)) {
            markerIcon = R.drawable.sicala_toda;
        }
        if("28".equals(id) || "29".equals(id) || "30".equals(id) || "32".equals(id) || "33".equals(id)) {
            markerIcon = R.drawable.bmbg_toda;
        }
        if("33".equals(id) || "34".equals(id) || "35".equals(id) || "36".equals(id) || "37".equals(id) || "38".equals(id)) {
            markerIcon = R.drawable.mcch_toda;
        }
        if("39".equals(id) || "40".equals(id) || "41".equals(id) || "42".equals(id)) {
            markerIcon = R.drawable.sjv7_toda;
        }
        if("43".equals(id) || "44".equals(id) || "45".equals(id) || "46".equals(id)) {
            markerIcon = R.drawable.maca_toda;
        }
        if("47".equals(id) || "48".equals(id) || "49".equals(id) || "50".equals(id) || "51".equals(id) || "52".equals(id)) {
            markerIcon = R.drawable.bb_toda;
        }
        if("53".equals(id) || "54".equals(id) || "55".equals(id)) {
            markerIcon = R.drawable.bta_toda;
        }
        if("56".equals(id) || "57".equals(id) || "58".equals(id) || "59".equals(id)) {
            markerIcon = R.drawable.sjb_toda;
        }
        if("60".equals(id) || "61".equals(id)) {
            markerIcon = R.drawable.macopastar;
        }
        if("62".equals(id)) {
            markerIcon = R.drawable.posa_toda;
        }
        if("63".equals(id) || "64".equals(id) || "65".equals(id)) {
            markerIcon = R.drawable.ospo_toda;
        }
        if("66".equals(id) || "67".equals(id) || "68".equals(id) || "69".equals(id) || "70".equals(id) || "71".equals(id) || "72".equals(id) || "73".equals(id) || "74".equals(id) || "75".equals(id) || "76".equals(id) || "77".equals(id) || "78".equals(id) || "769".equals(id) || "80".equals(id)) {
            markerIcon = R.drawable.jeep_pin_icon;
        }
        return markerIcon;
    }
    public double convertToKm(double meters){
        double km = meters/1000;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double format = Double.parseDouble(decimalFormat.format(km));
        return format;
    }
    public String estimatedTimeDuration(double totalDistance) {
        String time;
        DecimalFormat decimalFormat = new DecimalFormat("#");
        double cal = (convertToKm(totalDistance)/10) * 60;
        time = String.valueOf(decimalFormat.format(cal));

        return time;
    }
    public double discountFare(double fare) {
        double discountedFare = 0;

        if(getFareDiscountStatus.equals("none")) {
            discountedFare = fare;
        }

        if (getFareDiscountStatus.equals("discounted")) {
            discountedFare = fare - (fare * 0.20);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return discountedFare;
    }
    private void showLocationNotFoundErrorDialog() {
        View alertCustomDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_location_not_found, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setView(alertCustomDialog);
        Button goBackBtn = alertCustomDialog.findViewById(R.id.locatioNotFoundBtn);
        final AlertDialog dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove(userLoc);
                editor.remove(userDes);
                editor.remove(cost);
                editor.commit();
                dialog.cancel();

            }
        });
    }

    // Helper method to check if an exception is an ANR issue
    private boolean isANRIssue(Exception e) {
        // Add your logic to check if the exception is related to ANR
        // You may inspect the exception type or other characteristics

        // For example, checking if the exception message contains "ANR"
        return e.getMessage() != null && e.getMessage().toLowerCase().contains("anr");
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_key);
        return url;
    }
    public double getLatitude(Context context, String loc) {
        Geocoder geocoder = new Geocoder(context);
        double latitude = 0;

        try {
            List<Address> addresses = geocoder.getFromLocationName(loc, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                latitude = address.getLatitude();
            }
        } catch (IOException e) {
            // Handle the exception more gracefully in a production environment
            e.printStackTrace();
            editor.remove(userLoc);
            editor.remove(userDes);
            editor.remove(Cost);
            editor.commit();
            if (con == null){
                checkInternetConnection();
            }
            // Log the error for debugging purposes
            Log.e("Geocoding", "Error getting latitude for location: " + loc, e);
        }

        return latitude;
    }

    public double getLongitude(Context context, String loc) {
        Geocoder geocoder = new Geocoder(context);
        double longitude = 0;

        try {
            List<Address> addresses = geocoder.getFromLocationName(loc, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                longitude = address.getLongitude();
            }
        } catch (IOException e) {
            // Handle the exception more gracefully in a production environment
            e.printStackTrace();
            if (con == null){
                checkInternetConnection();
            }
            editor.remove(userLoc);
            editor.remove(userDes);
            editor.remove(Cost);
            editor.commit();
            // Log the error for debugging purposes
            Log.e("Geocoding", "Error getting longitude for location: " + loc, e);
        }

        return longitude;
    }

    public void searchLocation(Context context, String searchLocation) {
        Geocoder geocoder = new Geocoder(context);

        try {
            List<Address> addresses = geocoder.getFromLocationName(searchLocation, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                mapAPI.addMarker(new MarkerOptions().position(location).title(searchLocation));
                mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        } catch (IOException e) {
            // Handle the exception more gracefully in a production environment
            e.printStackTrace();
            e.printStackTrace();
            editor.remove(userLoc);
            editor.remove(userDes);
            editor.remove(Cost);
            editor.commit();
            // Log the error for debugging purposes
            Log.e("Geocoding", "Error searching location: " + searchLocation, e);
            showLocationNotFoundErrorDialog();
        }
    }

    private void pinLocationOnMap(double latitude, double longitude, String title) {
        LatLng location = new LatLng(latitude, longitude);
        mapAPI.addMarker(new MarkerOptions().position(location).title(title));
        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
    private void requestLastKnownLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mapAPI.addMarker(new MarkerOptions().position(latLng).title("My Location"));
                        mapAPI.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                    }
                }
            }
        });
    }
    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    // ... other methods ...

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            double bottom =  14.217955 - 0.1f;
            double left = 121.093565 - 0.1f;
            double top = 14.297272 + 0.1f;
            double right = 121.130053 + 0.1f;


            // Define the LatLngBounds for Cabuyao, Laguna
            LatLngBounds cabuyaoBounds = new LatLngBounds(
                    new LatLng(bottom, left), // Minimum latitude and longitude for Cabuyao
                    new LatLng(top, right)  // Maximum latitude and longitude for Cabuyao
            );

            // Set the LatLngBounds for camera target
            mapAPI.setLatLngBoundsForCameraTarget(cabuyaoBounds);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cabuyaoBounds.getCenter(), zoom); // Adjust the zoom level as needed
            mapAPI.animateCamera(cameraUpdate);
        } else {
            // Permission denied, show custom explanation and prompt to grant permission again
            // ...
        }
    }
    public void checkInternetConnection(){
        if (NetworkUtils.isWifiConnected(getApplicationContext())) {

        }else {
            connectionManager = new ConnectionManager(requireContext(), editor);
            connectionManager.lostConnectionDialog(requireActivity());
        }
    }
    
    //This is to get the users' current location

    public void setInfoView(){
        mapAPI.setInfoWindowAdapter(new InfoWindow(requireContext()));
    }

}

