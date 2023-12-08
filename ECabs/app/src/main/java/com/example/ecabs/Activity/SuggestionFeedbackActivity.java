package com.example.ecabs.Activity;

import static com.example.ecabs.Activity.RateUsActivity.UserIDKey;
import static com.example.ecabs.Fragments.SearchFragment.SHARED_PREF_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecabs.R;
import com.example.ecabs.Utils.ConnectionManager;
import com.example.ecabs.Utils.NetworkUtils;
import com.example.ecabs.databinding.ActivitySuggestionFeedbacksBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SuggestionFeedbackActivity extends AppCompatActivity {


    private ActivitySuggestionFeedbacksBinding binding;

    int userID = 1;
    private ConnectionManager connectionManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String userIdKey;
    private String satisfaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuggestionFeedbacksBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        userIdKey = preferences.getString(UserIDKey, null);

        ImageView feed1 = view.findViewById(R.id.feed1);
        ImageView feed2 = view.findViewById(R.id.feed2);
        ImageView feed3 = view.findViewById(R.id.feed3);
        ImageView feed4 = view.findViewById(R.id.feed4);
        ImageView feed5 = view.findViewById(R.id.feed5);


        View.OnClickListener click = v ->{

            if (v == feed1 ){
                feed1.setImageResource(R.drawable.satisfaction1_selected);
                feed2.setImageResource(R.drawable.satisfaction2_unselect);
                feed3.setImageResource(R.drawable.satisfaction3_unselect);
                feed4.setImageResource(R.drawable.satisfaction4_unselect);
                feed5.setImageResource(R.drawable.satisfaction5_unselect);
                satisfaction = "Very Dissatisfied";
            }
            if (v == feed2){
                feed1.setImageResource(R.drawable.satisfaction1_unselect);
                feed2.setImageResource(R.drawable.satisfaction2_selected);
                feed3.setImageResource(R.drawable.satisfaction3_unselect);
                feed4.setImageResource(R.drawable.satisfaction4_unselect);
                feed5.setImageResource(R.drawable.satisfaction5_unselect);
                satisfaction = "Dissatisfied";
            }
            if (v == feed3){
                feed1.setImageResource(R.drawable.satisfaction1_unselect);
                feed2.setImageResource(R.drawable.satisfaction2_unselect);
                feed3.setImageResource(R.drawable.satisfaction3_selected);
                feed4.setImageResource(R.drawable.satisfaction4_unselect);
                feed5.setImageResource(R.drawable.satisfaction5_unselect);
                satisfaction = "Ok";
            }
            if (v == feed4){
                feed1.setImageResource(R.drawable.satisfaction1_unselect);
                feed2.setImageResource(R.drawable.satisfaction2_unselect);
                feed3.setImageResource(R.drawable.satisfaction3_unselect);
                feed4.setImageResource(R.drawable.satisfaction4_selected);
                feed5.setImageResource(R.drawable.satisfaction5_unselect);
                satisfaction = "Satisfied";
            }
            if (v == feed5){
                feed1.setImageResource(R.drawable.satisfaction1_unselect);
                feed2.setImageResource(R.drawable.satisfaction2_unselect);
                feed3.setImageResource(R.drawable.satisfaction3_unselect);
                feed4.setImageResource(R.drawable.satisfaction4_unselect);
                feed5.setImageResource(R.drawable.satisfaction5_selected);
                satisfaction = "Very Satisfied";
            }
        };
        feed1.setOnClickListener(click);
        feed2.setOnClickListener(click);
        feed3.setOnClickListener(click);
        feed4.setOnClickListener(click);
        feed5.setOnClickListener(click);


        /*Custom Alert Dialog*/
        View alertCustomDialog = LayoutInflater.from(SuggestionFeedbackActivity.this).inflate(R.layout.dialog_suggestion_feedbacks, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SuggestionFeedbackActivity.this);
        alertDialog.setView(alertCustomDialog);
        Button closeBtn = (Button) alertCustomDialog.findViewById(R.id.successDone);
        final AlertDialog dialog = alertDialog.create();


        ImageView close = view.findViewById(R.id.close);
        TextView skip = view.findViewById(R.id.skip);
        Button submitFeedBtn = view.findViewById(R.id.submitFeedBtn);

        EditText suggestionTxt = binding.suggestionTxt;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users_feedbacks");
        Query checkUserDatabase = reference.orderByChild("UserID");

        if (userIdKey != null){
            userID = Integer.valueOf(userIdKey);
        }else{
            checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String userIDFromDB = snapshot.child("UserID").getValue(String.class);
                        if (!userIDFromDB.equals(-1)){
                            userID = Integer.valueOf(userIDFromDB) + 1;


                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Handler handler = new Handler();

        View.OnClickListener clickListener = v ->{

            if (v == submitFeedBtn){
                submitFeedBtn.setBackgroundResource(R.drawable.button_hover_5radius);
            }

            if (v == closeBtn){
                closeBtn.setBackgroundResource(R.drawable.button);

            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    String suggestTxt = suggestionTxt.getText().toString();
                    String getUserID = String.valueOf(userID);

                    if (v==submitFeedBtn){
                        submitFeedBtn.setBackgroundResource(R.drawable.button_5radius);

                        if (NetworkUtils.isWifiConnected(getApplicationContext())) {
                            if (!suggestTxt.equals("") && !satisfaction.equals(null)){
                                reference.child("users_suggestion_feedbacks").child("Satisfaction_Level").child("UserID").child(getUserID).setValue(satisfaction);
                                reference.child("users_suggestion_feedbacks").child("Suggestion").child("UserID").child(getUserID).setValue(suggestTxt);
                                reference.child("UserID").setValue(getUserID);
                                if (userIdKey != null){

                                }else {
                                    editor.putString(UserIDKey, getUserID);
                                    editor.apply();
                                }
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                            }else {
                                Toast.makeText(SuggestionFeedbackActivity.this, "All Requirements are required", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            connectionManager = new ConnectionManager(SuggestionFeedbackActivity.this, editor);
                            connectionManager.lostConnectionDialog(SuggestionFeedbackActivity.this);
                        }



                    }
                    if (v == closeBtn){
                        closeBtn.setBackgroundResource(R.drawable.fragment_transit_button_boxs);
                        dialog.cancel();
                        Toast.makeText(SuggestionFeedbackActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SuggestionFeedbackActivity.this, SettingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }

                }
            },100);

        };
        submitFeedBtn.setOnClickListener(clickListener);
        closeBtn.setOnClickListener(clickListener);




       close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionFeedbackActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SuggestionFeedbackActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SuggestionFeedbackActivity.this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}