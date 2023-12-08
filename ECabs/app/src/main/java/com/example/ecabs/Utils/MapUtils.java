package com.example.ecabs.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapUtils {
    public static void addCustomMarkerToMap(
            GoogleMap mapAPI,
            Context context,
            int drawableResource,
            LatLng position,
            String title,
            String snippet) {

        BitmapDescriptor customIcon = getBitmapDescriptorFromVector(context, drawableResource);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(customIcon);

        mapAPI.addMarker(markerOptions);
    }

    public static BitmapDescriptor getBitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = context.getResources().getDrawable(vectorResId);
        int width = vectorDrawable.getIntrinsicWidth();
        int height = vectorDrawable.getIntrinsicHeight();
        vectorDrawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
