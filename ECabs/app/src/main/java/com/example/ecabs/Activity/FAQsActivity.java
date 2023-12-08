package com.example.ecabs.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecabs.R;

public class FAQsActivity extends AppCompatActivity {

    Boolean q1, q2, q3, q4, q5, q6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        q1 = false;
        q2 = false;
        q3 = false;
        q4 = false;
        q5 = false;
        q6 = false;

        ImageView back = findViewById(R.id.eastBack);
        //Question1
        LinearLayout Q_1Btn = findViewById(R.id.faqsBtn1);
        TextView Q_1Ans = findViewById(R.id.q1Ans);
        View arrow = findViewById(R.id.arrow1);
        View line1 = findViewById(R.id.line1);
        //Question1
        LinearLayout Q_2Btn = findViewById(R.id.faqsBtn2);
        TextView Q_2Ans = findViewById(R.id.q2Ans);
        View arrow2 = findViewById(R.id.arrow2);
        View line2 = findViewById(R.id.line2);
        //Question1
        LinearLayout Q_3Btn = findViewById(R.id.faqsBtn3);
        TextView Q_3Ans = findViewById(R.id.q3Ans);
        View arrow3 = findViewById(R.id.arrow3);
        View line3 = findViewById(R.id.line3);
        //Question1
        LinearLayout Q_4Btn = findViewById(R.id.faqsBtn4);
        TextView Q_4Ans = findViewById(R.id.q4Ans);
        View arrow4 = findViewById(R.id.arrow4);
        View line4 = findViewById(R.id.line4);
        //Question1
        LinearLayout Q_5Btn = findViewById(R.id.faqsBtn5);
        TextView Q_5Ans = findViewById(R.id.q5Ans);
        View arrow5 = findViewById(R.id.arrow5);
        View line5 = findViewById(R.id.line5);
        //Question6
        LinearLayout Q_6Btn = findViewById(R.id.faqsBtn6);
        TextView Q_6Ans = findViewById(R.id.q6Ans);
        View arrow6 = findViewById(R.id.arrow6);

        View.OnClickListener clickListener = v ->{
            if (Q_1Btn == v){
                if (q1.equals(false)){
                    Q_1Ans.setText(R.string.Ans1);
                    changeTextViewTopMargin(Q_1Ans, 15);
                    Q_1Btn.setBackgroundResource(R.drawable.fragment_setting_container);
                    arrow.setRotation(180.0f);
                    line1.setAlpha(0);
                    q1 = true;
                }else {
                    Q_1Ans.setText("");
                    Q_1Btn.setBackgroundResource(0);
                    changeTextViewTopMargin(Q_1Ans, -30);
                    arrow.setRotation(0.0f);
                    line1.setAlpha(1);
                    q1 = false;
                }
            }
            if (Q_2Btn == v){
                if (q2.equals(false)){
                    Q_2Ans.setText(R.string.Ans2);
                    changeTextViewTopMargin(Q_2Ans, 15);
                    Q_2Btn.setBackgroundResource(R.drawable.fragment_setting_container);
                    arrow2.setRotation(180.0f);
                    line2.setAlpha(0);
                    q2 = true;
                }else {
                    Q_2Ans.setText("");
                    Q_2Btn.setBackgroundResource(0);
                    changeTextViewTopMargin(Q_2Ans, -30);
                    arrow2.setRotation(0.0f);
                    line2.setAlpha(1);
                    q2 = false;
                }

            }
            if (Q_3Btn == v){
                if (q3.equals(false)){
                    Q_3Ans.setText(R.string.Ans3);
                    changeTextViewTopMargin(Q_3Ans, 15);
                    Q_3Btn.setBackgroundResource(R.drawable.fragment_setting_container);
                    arrow3.setRotation(180.0f);
                    line3.setAlpha(0);
                    q3 = true;
                }else {
                    Q_3Ans.setText("");
                    Q_3Btn.setBackgroundResource(0);
                    changeTextViewTopMargin(Q_3Ans, -30);
                    arrow3.setRotation(0.0f);
                    line3.setAlpha(1);
                    q3 = false;
                }
            }
            if (Q_4Btn == v){
                if (q4.equals(false)){
                    Q_4Ans.setText(R.string.Ans4);
                    changeTextViewTopMargin(Q_4Ans, 15);
                    Q_4Btn.setBackgroundResource(R.drawable.fragment_setting_container);
                    arrow4.setRotation(180.0f);
                    line4.setAlpha(0);
                    q4 = true;

                }else {
                    Q_4Ans.setText("");
                    Q_4Btn.setBackgroundResource(0);
                    changeTextViewTopMargin(Q_4Ans, -30);
                    arrow4.setRotation(0.0f);
                    line4.setAlpha(1);
                    q4 = false;
                }

            }
            if (Q_5Btn == v){
                if (q5.equals(false)){
                    Q_5Ans.setText(R.string.Ans5);
                    changeTextViewTopMargin(Q_5Ans, 15);
                    Q_5Btn.setBackgroundResource(R.drawable.fragment_setting_container);
                    arrow5.setRotation(180.0f);
                    line5.setAlpha(0);
                    q5 = true;
                }else {
                    Q_5Ans.setText("");
                    Q_5Btn.setBackgroundResource(0);
                    changeTextViewTopMargin(Q_5Ans, -30);
                    arrow5.setRotation(0.0f);
                    line5.setAlpha(1);
                    q5 = false;
                }

            }
            if (Q_6Btn == v){
                if (q6.equals(false)){
                    Q_6Ans.setText(R.string.Ans6);
                    changeTextViewTopMargin(Q_6Ans, 15);
                    Q_6Btn.setBackgroundResource(R.drawable.fragment_setting_container);
                    arrow6.setRotation(180.0f);
                    q6 = true;
                }else {
                    Q_6Ans.setText("");
                    Q_6Btn.setBackgroundResource(0);
                    changeTextViewTopMargin(Q_1Ans, -30);
                    arrow6.setRotation(0.0f);
                    q6 = false;
                }

            }

        };
        Q_1Btn.setOnClickListener(clickListener);
        Q_2Btn.setOnClickListener(clickListener);
        Q_3Btn.setOnClickListener(clickListener);
        Q_4Btn.setOnClickListener(clickListener);
        Q_5Btn.setOnClickListener(clickListener);
        Q_6Btn.setOnClickListener(clickListener);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FAQsActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void changeTextViewTopMargin(TextView textView, int topMargin) {
        // Get the existing layout parameters
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        // Modify the top margin
        params.topMargin = topMargin;
        // Apply the modified layout parameters
        textView.setLayoutParams(params);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FAQsActivity.this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}