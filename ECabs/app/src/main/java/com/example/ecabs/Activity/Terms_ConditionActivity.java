package com.example.ecabs.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ecabs.Fragments.OnceLoginSettingFragment;
import com.example.ecabs.R;

public class Terms_ConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        ImageView backBtn = findViewById(R.id.eastBack);
        Button agreeBtn = findViewById(R.id.agreeBtn);



        Handler handler = new Handler();
        View.OnClickListener clickListener = v -> {

            if (v == agreeBtn){
                agreeBtn.setBackgroundResource(R.drawable.button_hover_5radius);

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (v == agreeBtn){
                        agreeBtn.setBackgroundResource(R.drawable.button_5radius);
                        Intent intent = new Intent(Terms_ConditionActivity.this, SettingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        agreeBtn.setEnabled(false);

                    }

                }
            }, 100);
        };

        agreeBtn.setOnClickListener(clickListener);



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Terms_ConditionActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Terms_ConditionActivity.this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    }
