package com.example.ecabs.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecabs.R;

public class Once_Signup extends AppCompatActivity {

    SharedPreferences preferences;

    private static final String SHARED_PREF_NAME= "MyPreferences";
    private static final String KEY_EMAIL= "email";

    Boolean clicked = true;
    int pageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once_signup);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = preferences.getString(KEY_EMAIL, null);

        TextView signname = findViewById(R.id.onceSignName);
        TextView skipped = findViewById(R.id.skip);
        Button nextbutton =  findViewById(R.id.Button);

        LinearLayout page_1 = findViewById(R.id.Page_1);
        LinearLayout page_1_1 = findViewById(R.id.Page_1_1);
        LinearLayout page_2 = findViewById(R.id.Page_2);
        LinearLayout page_3 = findViewById(R.id.Page_3);
        LinearLayout page_4 = findViewById(R.id.Page_4);
        LinearLayout page_5 = findViewById(R.id.Page_5);
        LinearLayout page_6 = findViewById(R.id.Page_6);
        ImageView close = findViewById(R.id.close);
        ImageView scroll1 = findViewById(R.id.scroll1);
        ImageView scroll1_1 = findViewById(R.id.scroll1_1);
        ImageView scroll2 = findViewById(R.id.scroll2);
        ImageView scroll3 = findViewById(R.id.scroll3);
        ImageView scroll4 = findViewById(R.id.scroll4);
        ImageView scroll5 = findViewById(R.id.scroll5);
        ImageView scroll6 = findViewById(R.id.scroll6);


        if (email !=null){
            signname.setText("Hi, "+email + "!");
        }


        View.OnClickListener clickListener = v ->{

            if (v == close) {
                // Rest of your code here...
                Intent intent = new Intent(Once_Signup.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
            if (v==nextbutton){

                if (pageCount == 0) {
                    pageCount =1;
                    page_1_1.setAlpha(1);
                    page_1_1.setElevation(2);
                    page_1.setAlpha(0);
                    page_1.setElevation(-1);
                    scroll1.setImageResource(R.drawable.once_scroll_light);
                    scroll1_1.setImageResource(R.drawable.once_scroll_dark);
                    nextbutton.setText("GET STARTED");

                }
                else if (pageCount == 1) {
                    pageCount = 2;
                    page_2.setAlpha(1);
                    page_2.setElevation(2);
                    page_1_1.setAlpha(0);
                    page_1_1.setElevation(-1);
                    scroll1_1.setImageResource(R.drawable.once_scroll_light);
                    scroll2.setImageResource(R.drawable.once_scroll_dark);
                    nextbutton.setText("NEXT");

                }
                else if (pageCount == 2){
                    pageCount = 3;
                    // Rest of your code here...
                    page_3.setAlpha(1);
                    page_3.setElevation(2);
                    page_2.setAlpha(0);
                    page_2.setElevation(-1);
                    scroll2.setImageResource(R.drawable.once_scroll_light);
                    scroll3.setImageResource(R.drawable.once_scroll_dark);

                }

                else if (pageCount == 3){
                    pageCount = 4;
                    // Rest of your code here...
                    page_4.setAlpha(1);
                    page_4.setElevation(2);
                    page_3.setAlpha(0);
                    page_3.setElevation(-1);
                    scroll3.setImageResource(R.drawable.once_scroll_light);
                    scroll4.setImageResource(R.drawable.once_scroll_dark);


                }else if (pageCount == 4){
                    pageCount = 5;
                    // Rest of your code here...
                    page_5.setAlpha(1);
                    page_5.setElevation(2);
                    page_4.setAlpha(0);
                    page_4.setElevation(-1);
                    scroll4.setImageResource(R.drawable.once_scroll_light);
                    scroll5.setImageResource(R.drawable.once_scroll_dark);


                }else if (pageCount == 5){
                    pageCount = 6;
                    // Rest of your code here...
                    page_6.setAlpha(1);
                    page_6.setElevation(2);
                    page_5.setAlpha(0);
                    page_5.setElevation(-1);
                    scroll5.setImageResource(R.drawable.once_scroll_light);
                    scroll6.setImageResource(R.drawable.once_scroll_dark);
                    nextbutton.setText("Close");


                }
                else if (pageCount == 6){
                    Intent intent = new Intent(Once_Signup.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

                }
                }if (v == skipped) {


                    Intent intent = new Intent(Once_Signup.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }


        };
        close.setOnClickListener(clickListener);
        nextbutton.setOnClickListener(clickListener);
        skipped.setOnClickListener(clickListener);
    }
}