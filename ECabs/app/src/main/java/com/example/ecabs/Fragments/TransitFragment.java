package com.example.ecabs.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecabs.Utils.BottomNavigationUtils;
import com.example.ecabs.R;
import com.example.ecabs.databinding.FragmentTransitBinding;

public class TransitFragment extends Fragment {
    private FragmentTransitBinding binding;


    int blue ;
    int darkblue;
    boolean Clicked = false;

    int check = 0;
    boolean clicked;
    float elevation = 2;
    float elevation2 = -1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transit, container, false);


        /*Custom Alert Dialog*/
        /*Mode of Transportation Menu show in map or close*/
        View jeepneyDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_jeepney, null);
        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(requireContext());
        alertDialog1.setView(jeepneyDialog);
        Button jeepMapBtn = (Button) jeepneyDialog.findViewById(R.id.jeepMapBtn);
        Button jeepCloseBtn = (Button) jeepneyDialog.findViewById(R.id.jeepCloseBtn);
        final AlertDialog jeepdialog = alertDialog1.create();

        View tricycleDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_tricycle, null);
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(requireContext());
        alertDialog2.setView(tricycleDialog);
        Button tricMapBtn = (Button) tricycleDialog.findViewById(R.id.triMapBtn);
        Button tricCloseBtn = (Button) tricycleDialog.findViewById(R.id.triCloseBtn);
        TextView todaName = (TextView) tricycleDialog.findViewById(R.id.todaName);
        TextView todaLoc = (TextView) tricycleDialog.findViewById(R.id.todaLoc);

        final AlertDialog tricycledialog = alertDialog2.create();


        blue = ContextCompat.getColor(requireContext(), R.color.blue);
        darkblue = ContextCompat.getColor(requireContext(), R.color.darkblue);

          TextView jeepneyBtn = rootView.findViewById(R.id.jeepney);
          TextView tricycleBtn = rootView.findViewById(R.id.tricycle);
          LinearLayout tricList = rootView.findViewById(R.id.tricList);
          LinearLayout jeepList = rootView.findViewById(R.id.jeepneyList);
          LinearLayout jeepRoute = rootView.findViewById(R.id.jeep_route);
          TextView jeepUnderline = rootView.findViewById(R.id.jeepUnderline);
          TextView tricUnderline = rootView.findViewById(R.id.tricUnderline);





        /*List of TODA Terminals*/
        LinearLayout posatoda = rootView.findViewById(R.id.posatoda);
        LinearLayout bbtoda = rootView.findViewById(R.id.bbtoda);
        LinearLayout btatoda = rootView.findViewById(R.id.btatoda);
        LinearLayout ospotoda = rootView.findViewById(R.id.ospotoda);
        LinearLayout cmsatoda = rootView.findViewById(R.id.cmsatoda);
        LinearLayout macatoda = rootView.findViewById(R.id.macatoda);
        LinearLayout bmbgtoda = rootView.findViewById(R.id.bmbgtoda);
        LinearLayout csvtoda = rootView.findViewById(R.id.csvtoda);
        LinearLayout sjv7toda = rootView.findViewById(R.id.sjv7gtoda);
        LinearLayout  sjbtoda= rootView.findViewById(R.id.sjbtoda);
        LinearLayout sicalatoda = rootView.findViewById(R.id.sicalatoda);
        LinearLayout pudtoda = rootView.findViewById(R.id.pudtoda);
        LinearLayout hvtoda = rootView.findViewById(R.id.hvtoda);
        LinearLayout dovtoda = rootView.findViewById(R.id.dovtoda);
        LinearLayout katoda = rootView.findViewById(R.id.katoda);
        LinearLayout mcchtoda = rootView.findViewById(R.id.mcchtoda);
        LinearLayout botoda = rootView.findViewById(R.id.botoda);
        LinearLayout macopastar = rootView.findViewById(R.id.macopastar);
        LinearLayout lnstoda = rootView.findViewById(R.id.lnstoda);

        LinearLayout[] linearLayouts = new LinearLayout[] {
                rootView.findViewById(R.id.posatoda),
                rootView.findViewById(R.id.bbtoda),
                rootView.findViewById(R.id.btatoda),
                rootView.findViewById(R.id.ospotoda),
                rootView.findViewById(R.id.cmsatoda),
                rootView.findViewById(R.id.macatoda),
                rootView.findViewById(R.id.bmbgtoda),
                rootView.findViewById(R.id.csvtoda),
                rootView.findViewById(R.id.sjv7gtoda),
                rootView.findViewById(R.id.sjbtoda),
                rootView.findViewById(R.id.sicalatoda),
                rootView.findViewById(R.id.pudtoda),
                rootView.findViewById(R.id.hvtoda),
                rootView.findViewById(R.id.dovtoda),
                rootView.findViewById(R.id.katoda),
                rootView.findViewById(R.id.mcchtoda),
                rootView.findViewById(R.id.botoda),
                rootView.findViewById(R.id.macopastar),
                rootView.findViewById(R.id.lnstoda)
        };


        setLinearLayoutsEnabled(linearLayouts, false);


// Create a handler
        Handler handler = new Handler();

        View.OnClickListener clickListener = v -> {
            if (v == jeepneyBtn) {
               jeepUnderline.setAlpha(1);
                tricUnderline.setAlpha(0);
                jeepRoute.setEnabled(true);
                setLinearLayoutsEnabled(linearLayouts, false);



            } else if (v == tricycleBtn) {
                jeepUnderline.setAlpha(0);
                tricUnderline.setAlpha(1);
                jeepRoute.setEnabled(false);
                setLinearLayoutsEnabled(linearLayouts, true);


            }else if (v == jeepMapBtn) {
                jeepMapBtn.setBackgroundResource(R.drawable.button_hover);


            } else if (v == jeepCloseBtn) {
                jeepCloseBtn.setBackgroundResource(R.drawable.button_hover);



            } else if (v == tricMapBtn) {
                tricMapBtn.setBackgroundResource(R.drawable.button_hover);
                setLinearLayoutsEnabled(linearLayouts, false);
                tricList.setAlpha(0);


            } else if (v == tricCloseBtn) {
                tricCloseBtn.setBackgroundResource(R.drawable.button_hover);

            }


            // Post a delayed runnable to change the image resource back after 100 milisecond
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (v == jeepneyBtn) {
                        jeepList.setAlpha(1);
                        jeepList.setElevation(2);
                        tricList.setAlpha(0);
                        tricList.setElevation(-1);



                    } else if (v == tricycleBtn) {
                        tricList.setAlpha(1);
                        tricList.setElevation(2);
                        jeepList.setAlpha(0);
                        jeepList.setElevation(-1);
                        setLinearLayoutsEnabled(linearLayouts, true);


                    }else if (v == jeepMapBtn) {
                        jeepMapBtn.setBackgroundResource(R.drawable.button);
                        Clicked = true;
                        jeepdialog.cancel();
                        // put the setNavs first before the condition
                        //so it will not interact with the condition
                        setNavs();
                        Maps_Fragment mapsFragment1 = new Maps_Fragment();
                        Bundle args = new Bundle();
                        args.putBoolean("JEEP", Clicked);
                        mapsFragment1.setArguments(args);
                        replaceFragment(mapsFragment1);


                        } else if (v == jeepCloseBtn) {
                        jeepCloseBtn.setBackgroundResource(R.drawable.button);
                        jeepdialog.cancel();

                    }else if (v == tricMapBtn) {
                        tricMapBtn.setBackgroundResource(R.drawable.button);
                        if (check ==1 ){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("POSATODA", Clicked);
                            mapsFragment.setArguments(args);

                            replaceFragment(mapsFragment);



                        }if (check ==2 ){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("BBTODA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 3 ){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("BTATODA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }

                        if (check == 4 ){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("OSPOTODA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 5 ){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("CMS", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 6){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("MACA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 7){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("BMBG", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 8){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("CSV", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 9){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("SJV7", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 10){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("SJB", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 11){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("SICALA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 12){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("PUD", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 13){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("HV", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 14){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("DOV", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 15){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("KA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 16){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("MCCH", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 17){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("BO", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 18){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("MACOPA", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }
                        if (check == 19){
                            Clicked = true;
                            tricycledialog.cancel();
                            // put the setNavs first before the condition
                            //so it will not interact with the condition
                            setNavs();
                            Maps_Fragment mapsFragment = new Maps_Fragment();
                            Bundle args = new Bundle();
                            args.putBoolean("LNS", Clicked);
                            mapsFragment.setArguments(args);
                            replaceFragment(mapsFragment);

                        }


                    } else if (v == tricCloseBtn) {
                        tricCloseBtn.setBackgroundResource(R.drawable.button);
                        tricycledialog.cancel();


                    }
                }
            }, 100); // Delay of 1 second (1000 milliseconds)

        };
        jeepMapBtn.setOnClickListener(clickListener);
        jeepCloseBtn.setOnClickListener(clickListener);
        jeepneyBtn.setOnClickListener(clickListener);
        tricycleBtn.setOnClickListener(clickListener);
        tricMapBtn.setOnClickListener(clickListener);
        tricCloseBtn.setOnClickListener(clickListener);





        View.OnClickListener clickListener2 = v -> {

            if (v == posatoda){
                check = 1;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("POSATODA");
                todaLoc.setText("Poblacion and Sala");



            } else if (v == bbtoda) {
                check = 2;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("BBTODA");
                todaLoc.setText("Bigaa and Butong");

            }else if (v == btatoda) {
                check = 3;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("BTATODA");
                todaLoc.setText("Burgos");

             }else if (v == ospotoda) {
                check = 4;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("OSPOTODA");
                todaLoc.setText("Ospital ng Cabuyao");

            }else if (v == cmsatoda) {
                check = 5;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("CMSTODA");
                todaLoc.setText("Cabuyao Market Site");

            }else if (v == macatoda) {
                check = 6;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("MACATODA");
                todaLoc.setText("Marinig");

            } else if (v == bmbgtoda) {
                check = 7;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("BMBGTODA");
                todaLoc.setText("Baclaran, Mamatid, Banlic, Gulod");

            }else if (v == csvtoda) {
                check = 8;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("CSVTODA");
                todaLoc.setText("Cabuyao and Southville");

            }else if (v == sjv7toda) {
                check = 9;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("CSVTODA");
                todaLoc.setText("St. Joseph Gulod");

            }else if (v == sjbtoda) {
                check = 10;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("SJBTODA");
                todaLoc.setText("St. Joseph Butong");

            }else if (v == sicalatoda) {
                check = 11;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("SICALATODA");
                todaLoc.setText("San Isidro");

            }
            else if (v == pudtoda) {
                check = 12;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("PUDTODA");
                todaLoc.setText("Pulo Diezmo");

            }else if (v == hvtoda) {
                check = 13;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("HVTODA");
                todaLoc.setText("Hongkong Village");

            }else if (v == dovtoda) {
                check = 14;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("DOVTODA");
                todaLoc.setText("Don Onofre");

            }else if (v == katoda) {
                check = 15;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("KATODA");
                todaLoc.setText("Katapatan");

            }else if (v == mcchtoda) {
                check = 16;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("MCCHTODA");
                todaLoc.setText("Mabuhay City");

            }else if (v == botoda) {
                check = 17;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("BOTODA");
                todaLoc.setText("Bamboo Orchard");

            }else if (v == macopastar) {
                check = 18;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("MACOPASTR");
                todaLoc.setText("Mariquita, Console, Anahaw, Sta. Rosa");

            }else if (v == lnstoda) {
                check = 19;
                tricycledialog.show();
                tricycledialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                todaName.setText("LNSTODA");
                todaLoc.setText("Lakesidenest");

            }

        };
        posatoda.setOnClickListener(clickListener2);
        bbtoda.setOnClickListener(clickListener2);
        btatoda.setOnClickListener(clickListener2);
        ospotoda.setOnClickListener(clickListener2);
        cmsatoda.setOnClickListener(clickListener2);
        macatoda.setOnClickListener(clickListener2);
        bmbgtoda.setOnClickListener(clickListener2);
        csvtoda.setOnClickListener(clickListener2);
        sjv7toda.setOnClickListener(clickListener2);
        sjbtoda.setOnClickListener(clickListener2);
        sicalatoda.setOnClickListener(clickListener2);
        pudtoda.setOnClickListener(clickListener2);
        hvtoda.setOnClickListener(clickListener2);
        dovtoda.setOnClickListener(clickListener2);
        katoda.setOnClickListener(clickListener2);
        mcchtoda.setOnClickListener(clickListener2);
        botoda.setOnClickListener(clickListener2);
        macopastar.setOnClickListener(clickListener2);
        lnstoda.setOnClickListener(clickListener2);

        jeepRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jeepdialog.show();
                jeepdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });


       /* profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked = true;
                // Rest of your code here...
                Intent intent = new Intent(requireContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });*/

        return rootView;
    }
    public void setLinearLayoutsEnabled(LinearLayout[] linearLayouts, boolean isEnabled) {
        for (LinearLayout layout : linearLayouts) {
            layout.setEnabled(isEnabled);
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }
    public void setNavs(){
        BottomNavigationUtils.selectBottomNavigationItem(requireActivity(), R.id.map);
    }


}



