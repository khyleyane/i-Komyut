package com.example.ecabs.Utils;
import static com.example.ecabs.Activity.MainActivity.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.ecabs.R;

public class ConnectionManager {

    private Context context;
    private SharedPreferences.Editor editor;

    public ConnectionManager(Context context, SharedPreferences.Editor editor) {
        this.context = context;
        this.editor = editor;
    }

    public void lostConnectionDialog(Activity activity) {
        View alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.dialog_connection_lost, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(alertCustomDialog);
        Button eastbackBtn = alertCustomDialog.findViewById(R.id.eastBackBtn);


        final AlertDialog dialog = alertDialog.create();

        eastbackBtn.setOnClickListener(view -> {
            editor.putString(connection, "Con");
            editor.apply();
            dialog.cancel();
            stopNetworkCheck();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(0, 0);
        }
    }

    private void stopNetworkCheck() {
        // Stop the periodic network check
        networkCheckHandler.removeCallbacks(networkCheckRunnable);
    }

    // Add the necessary imports for these classes
    private Handler networkCheckHandler = new Handler();
    private Runnable networkCheckRunnable = new Runnable() {
        @Override
        public void run() {
            // Implement your network check logic here

            // Schedule the next network check after a delay
            networkCheckHandler.postDelayed(this, NETWORK_CHECK_INTERVAL);
        }
    };

    private static final int NETWORK_CHECK_INTERVAL = 5000; // Set the interval for network checks
}
