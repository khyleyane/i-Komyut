package com.example.ecabs.Utils;

import static android.content.Context.MODE_PRIVATE;

import static com.example.ecabs.Fragments.SearchFragment.SHARED_PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecabs.Fragments.Maps_Fragment;
import com.example.ecabs.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {
    Context context;

    String title;



    //code here to set the bottom information using marker.getTitle
    //Use If statement to identify what toda and to set the info

    public InfoWindow(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoView = LayoutInflater.from(context).inflate(R.layout.info_window, null);
        TextView infoTitle = infoView.findViewById(R.id.infoTitle);
        TextView infoSubTitle = infoView.findViewById(R.id.infoSubTitle);
        infoTitle.setText(marker.getTitle());
        infoSubTitle.setText(marker.getSnippet());

        title = infoTitle.getText().toString();

        ImageView franchiseColor = infoView.findViewById(R.id.franchiseColor);
        LinearLayout franchiseColorContainer = infoView.findViewById(R.id.franchiseColorContainer);
        TextView noTerminal = infoView.findViewById(R.id.noTerminal);
        TextView noOfUnits = infoView.findViewById(R.id.noOfUnits);

        if (title.equals("BBTODA")){
            noTerminal.setText("No. of Terminal: 4");
            noOfUnits.setText("No. of Units: 0001 - 0400");
            franchiseColor.setBackgroundResource(R.color.bbtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }else if (title.equals("POSATODA")){
            noTerminal.setText("No. of Terminal: 4");
            noOfUnits.setText("No. of Units: 0001 - 0200");
            franchiseColor.setBackgroundResource(R.color.red);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("BTATODA")){
            noTerminal.setText("No. of Terminal: 3");
            noOfUnits.setText("No. of Units: 0001 - 0120");
            franchiseColor.setBackgroundResource(R.color.btatoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("OSPOTODA")){
            noTerminal.setText("No. of Terminal: 1");
            noOfUnits.setText("No. of Units: 0001 - 0073");
            franchiseColor.setBackgroundResource(R.color.ospotoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("CMSATODA")){
            noTerminal.setText("No. of Terminal: 1");
            noOfUnits.setText("No. of Units: 0001 - 0100");
            franchiseColor.setBackgroundResource(R.color.cmsatoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("MACATODA")){
            noTerminal.setText("No. of Terminal: 3");
            noOfUnits.setText("No. of Units: 0001 - 0300");
            franchiseColor.setBackgroundResource(R.color.macatoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("BMBGTODA")){
            noTerminal.setText("No. of Terminal: 5");
            noOfUnits.setText("No. of Units: 0001 - 0936");
            franchiseColor.setBackgroundResource(R.color.bmbgtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("CSVTODA")){
            noTerminal.setText("No. of Terminal: 6");
            noOfUnits.setText("No. of Units: 0001 - 0400");
            franchiseColor.setBackgroundResource(R.color.csvtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("SJV7TODA")){
            noTerminal.setText("No. of Terminal: 6");
            noOfUnits.setText("No. of Units: 0001 - 0130");
            franchiseColor.setBackgroundResource(R.color.sjv7toda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("SJV7TODA")){
            noTerminal.setText("No. of Terminal: 6");
            noOfUnits.setText("No. of Units: 0001 - 0130");
            franchiseColor.setBackgroundResource(R.color.sjv7toda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("SJBTODA")){
            noTerminal.setText("No. of Terminal: 4");
            noOfUnits.setText("No. of Units: 0001 - 0260");
            franchiseColor.setBackgroundResource(R.color.sjbtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("SICALATODA")){
            noTerminal.setText("No. of Terminal: 3");
            noOfUnits.setText("No. of Units: 0001 - 0270");
            franchiseColor.setBackgroundResource(R.color.sicalatoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("PUDTODA")){
            noTerminal.setText("No. of Terminal: 4");
            noOfUnits.setText("No. of Units: 0001 - 0350");
            franchiseColor.setBackgroundResource(R.color.pudtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("HVTODA")){
            noTerminal.setText("No. of Terminal: 2");
            noOfUnits.setText("No. of Units: 0001 - 0045");
            franchiseColor.setBackgroundResource(R.color.hvtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("DOVTODA")){
            noTerminal.setText("No. of Terminal: 2");
            noOfUnits.setText("No. of Units: 0001 - 0025");
            franchiseColor.setBackgroundResource(R.color.dovtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("KATODA")){
            noTerminal.setText("No. of Terminal: 3");
            noOfUnits.setText("No. of Units: 0001 - 0110");
            franchiseColor.setBackgroundResource(R.color.katoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("MCCHTODAI")){
            noTerminal.setText("No. of Terminal: 8");
            noOfUnits.setText("No. of Units: 0001 - 0350");
            franchiseColor.setBackgroundResource(R.color.mcchtoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("BOTODA")){
            noTerminal.setText("No. of Terminal: 3");
            noOfUnits.setText("No. of Units: 0001 - 0035");
            franchiseColor.setBackgroundResource(R.color.botoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("MACOPASTR")){
            noTerminal.setText("No. of Terminal: 3");
            noOfUnits.setText("No. of Units: 0001 - 0100");
            franchiseColor.setBackgroundResource(R.color.macopastr);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("LNSTODA")){
            noTerminal.setText("No. of Terminal: 5");
            noOfUnits.setText("No. of Units: 0001 - 0125");
            franchiseColor.setBackgroundResource(R.color.lnstoda);
            franchiseColorContainer.setVisibility(View.VISIBLE);
        }
        else if (title.equals("JEEPNEY")){
            noTerminal.setVisibility(View.GONE);
            noOfUnits.setVisibility(View.GONE);
            franchiseColorContainer.setVisibility(View.GONE);
        }





        return infoView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
