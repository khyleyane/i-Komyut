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
import com.example.ecabs.User_Authentication_Registration.LoginActivity;

public class Once_Login extends AppCompatActivity {

    private boolean closeClicked = true;
    SharedPreferences preferences;

    private static final String SHARED_PREF_NAME= "MyPreferences";
    private static final String KEY_EMAIL= "email";
    int pageCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once_login);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = preferences.getString(KEY_EMAIL, null);

        LinearLayout page_1 = findViewById(R.id.Page_1);
        LinearLayout page_2 = findViewById(R.id.Page_2);
        LinearLayout page_3 = findViewById(R.id.Page_3);
        LinearLayout page_4 = findViewById(R.id.Page_4);
        LinearLayout page_5 = findViewById(R.id.Page_5);
        ImageView close = findViewById(R.id.close);
        ImageView scroll1 = findViewById(R.id.scroll1);
        ImageView scroll2 = findViewById(R.id.scroll2);
        ImageView scroll3 = findViewById(R.id.scroll3);
        ImageView scroll4 = findViewById(R.id.scroll4);
        ImageView scroll5 = findViewById(R.id.scroll5);
        TextView skip = findViewById(R.id.skip);
        Button button =  findViewById(R.id.Button);

        TextView name = findViewById(R.id.onceLoginName);

        if (email !=null){
            name.setText(email + "!");
        }

        View.OnClickListener clickListener = v -> {

            if (v == close) {
                // Rest of your code here...
                Intent intent = new Intent(Once_Login.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

            if (v==button){
                if (pageCount == 0) {
                    pageCount = 1;
                    page_2.setAlpha(1);
                    page_2.setElevation(2);
                    page_1.setAlpha(0);
                    page_1.setElevation(-1);
                    scroll1.setImageResource(R.drawable.once_scroll_light);
                    scroll2.setImageResource(R.drawable.once_scroll_dark);
                    button.setText("NEXT");
                }if (pageCount == 1) {
                    pageCount = 2;
                    page_2.setAlpha(1);
                    page_2.setElevation(2);
                    page_1.setAlpha(0);
                    page_1.setElevation(-1);
                    scroll1.setImageResource(R.drawable.once_scroll_light);
                    scroll2.setImageResource(R.drawable.once_scroll_dark);

                }else if (pageCount == 2){
                    page_3.setAlpha(1);
                    page_3.setElevation(2);
                    page_2.setAlpha(0);
                    page_2.setElevation(-1);
                    scroll2.setImageResource(R.drawable.once_scroll_light);
                    scroll3.setImageResource(R.drawable.once_scroll_dark);
                    pageCount = 3;

                }else if (pageCount == 3){
                    page_4.setAlpha(1);
                    page_4.setElevation(2);
                    page_3.setAlpha(0);
                    page_3.setElevation(-1);
                    scroll3.setImageResource(R.drawable.once_scroll_light);
                    scroll4.setImageResource(R.drawable.once_scroll_dark);
                    pageCount = 4;

                }else if (pageCount == 4){
                    page_5.setAlpha(1);
                    page_5.setElevation(2);
                    page_4.setAlpha(0);
                    page_4.setElevation(-1);
                    scroll4.setImageResource(R.drawable.once_scroll_light);
                    scroll5.setImageResource(R.drawable.once_scroll_dark);
                    button.setText("Close");
                    skip.setAlpha(0);
                    pageCount = 5;

                }else if (pageCount == 5){
                    Intent intent = new Intent(Once_Login.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);


                }
            }if (v == skip){
                Intent intent = new Intent(Once_Login.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }

        };

        close.setOnClickListener(clickListener);
        button.setOnClickListener(clickListener);
        skip.setOnClickListener(clickListener);

    }
}
