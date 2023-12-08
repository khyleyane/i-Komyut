package com.example.ecabs.Utils;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.example.ecabs.Fragments.Maps_Fragment.mapAPI;

import android.content.Context;

import com.example.ecabs.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapMarkerUtils {

Context context;
    public static void POSATODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.posa_toda,
                new LatLng(14.276702, 121.123878),
                "POSATODA",
                "Savemore Terminal"

        );

        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.posa_toda,
                new LatLng(14.271617,121.123517),
                "POSATODA",
                "City Hall Terminal"
        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.posa_toda,
                new LatLng(14.279598,121.126200),
                "POSATODA",
                "PNR Cabuyao Terminal"
        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.posa_toda,
                new LatLng(14.276103, 121.124146),
                "POSATODA",
                "DIY Cabuyao Terminal"
        );

    }
    public static void BBTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bb_toda,
                new LatLng(14.277724,121.123972),
                "BBTODA",
                "Cabuyao Retail Plaza Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bb_toda,
                new LatLng(14.295925,121.128305),
                "BBTODA",
                "Brgy. Bigaa/Butong Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bb_toda,
                new LatLng(14.287266,121.139044),
                "BBTODA",
                "St. Joseph Terminal"

        );
        //hindi pa ayos
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bb_toda,
                new LatLng(14.287266,121.139044),
                "BBTODA",
                "St. Joseph Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bb_toda,
                new LatLng(14.280048,121.126200),
                "BBTODA",
                "PNR Sub Terminal"

        );
    }
    public static void BTATODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bta_toda,
                new LatLng(14.277809,121.124189),
                "BTATODA",
                "Cabuyao Retail Plaza Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bta_toda,
                new LatLng(14.279990,121.123252),
                "BTATODA",
                "Cabuyao Town Plaza Brgy. Uno Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bta_toda,
                new LatLng(14.295925,121.128305),
                "BTATODA",
                "Gasoline Benz Terminal"

        );
    }
    public static void OSPOTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.ospo_toda,
                new LatLng(14.275612, 121.126494),
                "OSPOTODA",
                "Ospital ng Cabuyao"

        );
    }
    public static void CMSTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.cmsa_toda,
                new LatLng(14.277780, 121.124176),
                "CMSATODA",
                "Cabuyao Retail Plaza Main Terminal Special Trip"

        );
    }
    public static void MACATODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.maca_toda,
                new LatLng(14.276707, 121.123854),
                "MACATODA",
                "Savemore Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.maca_toda,
                new LatLng(14.274931, 121.124626),
                "MACATODA",
                "Ataw Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.maca_toda,
                new LatLng(14.275421, 121.150243),
                "MACATODA",
                "Celestine Home Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.maca_toda,
                new LatLng(14.275421, 121.150243),
                "MACATODA",
                "St. Joseph 7 Terminal"

        );
    }
    public static void BMBGTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bmbg_toda,
                new LatLng(14.243382, 121.170098),
                "BMBGTODA",
                "Mabuhay Phase 1 Terminal Baclaran"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bmbg_toda,
                new LatLng(14.264298, 121.162740),
                "BMBGTODA",
                "Brgy. Gulod Purok 2"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bmbg_toda,
                new LatLng(14.236778, 121.147374),
                "BMBGTODA",
                "Mabuhay Simbahan Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bmbg_toda,
                new LatLng(14.236761, 121.147383),
                "BMBGTODA",
                "Mabuhay Phase 6 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bmbg_toda,
                new LatLng(14.228720, 121.140448),
                "BMBGTODA",
                "Main Terminal Banlic"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bmbg_toda,
                new LatLng(14.232779, 121.135374),
                "BMBGTODA",
                "Waltermart Terminal"

        );
    }
    public static void CSVTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.csv_toda,
                new LatLng(14.276084, 121.124120),
                "CSVTODA",
                "Cabuyao Bayan Del Pilar Street"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.csv_toda,
                new LatLng(14.278720, 121.145587),
                "CSVTODA",
                "Southville Sunrise Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.csv_toda,
                new LatLng(14.275349, 121.143831),
                "CSVTODA",
                "Southville Palengke Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.csv_toda,
                new LatLng(14.274340, 121.142087),
                "CSVTODA",
                "South Ville Blk. 57 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.csv_toda,
                new LatLng(14.270396, 121.142882),
                "CSVTODA",
                "South Ville Blk. 21 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.csv_toda,
                new LatLng(14.257633, 121.135123),
                "CSVTODA",
                "Katapatan Main Terminal"

        );
    }
    public static void SJV7TODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.256082, 121.129092),
                "SJV7GTODA",
                "Katapatan Main Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.257633, 121.135123),
                "SJV7GTODA",
                "Katapatan Main Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.259543, 121.151256),
                "SJV7GTODA",
                "Homes 1 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.259505, 121.151268),
                "SJV7GTODA",
                "Homes 2 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.259937, 121.153783),
                "SJV7GTODA",
                "St. Joseph 7 Subdivision"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.264183, 121.154508),
                "SJV7GTODA",
                "Windfield Phase 5 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjv7_toda,
                new LatLng(14.268829, 121.159367),
                "SJV7GTODA",
                "St. Joseph 7 Subdivision"

        );
    }
    public static void SJBTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjb_toda,
                new LatLng(14.278255421022488, 121.12325987987101),
                "SJBTODA",
                "Variety Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjb_toda,
                new LatLng(14.280048,121.126200),
                "SJBTODA",
                "PNR Sub Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjb_toda,
                new LatLng(14.281452, 121.135656),
                "SJBTODA",
                "St. Joseph 6 Terminal(Gate)"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sjb_toda,
                new LatLng(14.287171, 121.139067),
                "SJBTODA",
                "St. Joseph 5 Terminal(Gate)"

        );
    }
    public static void SICALATODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sicala_toda,
                new LatLng(14.238222, 121.132909),
                "SICALATODA",
                "Centenial Plaza Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sicala_toda,
                new LatLng(14.236482, 121.123172),
                "SICALATODA",
                "Mahogany 2 & 3 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sicala_toda,
                new LatLng(14.238222, 121.132909),
                "SICALATODA",
                "San Isidro Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.sicala_toda,
                new LatLng(14.242148, 121.142605),
                "SICALATODA",
                "Canaan Homes Terminal"

        );
    }
    public static void PUDTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.pud_toda,
                new LatLng(14.246173, 121.129193),
                "PUDTODA",
                "Pulo Main Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.pud_toda,
                new LatLng(14.243326, 121.119247),
                "PUDTODA",
                "Adelina Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.pud_toda,
                new LatLng(14.229540, 121.092618),
                "PUDTODA",
                "Brgy. Diezmo Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.pud_toda,
                new LatLng(14.240453, 121.102056),
                "PUDTODA",
                "Lisp 1 Terminal"

        );
    }
    public static void HVTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.hv_toda,
                new LatLng(14.250751, 121.128843),
                "HVTODA",
                "Hongkong Village(Gate Terminal)"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.hv_toda,
                new LatLng(14.250183, 121.122781),
                "HVTODA",
                "Hongkong Village(Loob Terminal)"

        );
    }
    public static void DOVTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.dov_toda,
                new LatLng(14.252331, 121.128769),
                "DOVTODA",
                "Don Onofre (Gate Terminal))"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.dov_toda,
                new LatLng(14.253261, 121.132207),
                "DOVTODA",
                "Dona Juan Terminal"

        );
    }
    public static void KATODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.ka_toda,
                new LatLng(14.256082, 121.129092),
                "KATODA",
                "Katapatan Main Terminal(Entrance)"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.ka_toda,
                new LatLng(14.259248, 121.134116),
                "KATODA",
                "Katapatan PnC Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.ka_toda,
                new LatLng(14.257633, 121.135123),
                "KATODA",
                "Katapatan Main Terminal"

        );
    }
    public static void MCCHTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.235850157756996, 121.15781021603993),
                "MCCHTODAI",
                "Mabuhay Main Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.236834, 121.157501),
                "MCCHTODAI",
                "Mabuhay Phase 4 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.243377, 121.170096),
                "MCCHTODAI",
                "Mabuhay Phase 1 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.238624, 121.161767),
                "MCCHTODAI",
                "Mabuhay Phase 2 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.243210, 121.155586),
                "MCCHTODAI",
                "Mabuhay Phase 7 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.240562, 121.150097),
                "MCCHTODAI",
                "Mabuhay Phase 5 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.241867, 121.151388),
                "MCCHTODAI",
                "Mabuhay Phase 3 Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.mcch_toda,
                new LatLng(14.239765, 121.149942),
                "MCCHTODAI",
                "Mabuhay Phase 6 Terminal"

        );
    }
    public static void BOTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bo_toda,
                new LatLng(14.253454, 121.137532),
                "BOTODA",
                "Bamboo Subdivision"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bo_toda,
                new LatLng(14.255375, 121.138258),
                "BOTODA",
                "CABS/CITECH Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.bo_toda,
                new LatLng(14.256082, 121.129092),
                "BOTODA",
                "Katapatan Main Terminal"

        );
    }
    public static void MACOPASTR(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.macopastar,
                new LatLng(14.277789, 121.124167),
                "MACOPASTR",
                "Cabuyao Retail Plaza Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.macopastar,
                new LatLng(14.281071, 121.120751),
                "MACOPASTR",
                "Mariquita Home Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.macopastar,
                new LatLng(14.282963, 121.110905),
                "MACOPASTR",
                "Dita Santa Rosa Terminal"

        );
    }
    public static void LNSTODA(GoogleMap map, Context context) {
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.lns_toda,
                new LatLng(14.263888, 121.144337),
                "LNSTODA",
                "Lakeside Nest Subd. Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.lns_toda,
                new LatLng(14.258447, 121.145099),
                "LNSTODA",
                "Depante Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.lns_toda,
                new LatLng(14.255375, 121.138258),
                "LNSTODA",
                "CABS/CITECH Terminal"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.lns_toda,
                new LatLng(14.256082, 121.129092),
                "LNSTODA",
                "Katapatan Main Terminal(Entrance)"

        );
        MapUtils.addCustomMarkerToMap(
                mapAPI,
                context,
                R.drawable.lns_toda,
                new LatLng(14.257633, 121.135123),
                "LNSTODA",
                "Katapatan Main Terminal"

        );
    }
}
