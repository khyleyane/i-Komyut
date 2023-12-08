package com.example.ecabs.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecabs.Activity.MainActivity;
import com.example.ecabs.R;

import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.MyViewHolder> {
    Context context;
   ArrayList<Search_History> list;

   public History_Adapter(Context context, ArrayList<Search_History> list){
       this.context = context;
       this.list = list;
   }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_location_item_layout, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       Search_History history = list.get(position);
       holder.itemLocation.setText(history.getLocation());
       holder.itemDestination.setText(history.getDestination());
       holder.itemCurrentDate.setText(history.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

       TextView itemLocation;
       TextView itemDestination;

       TextView itemCurrentDate;

       public MyViewHolder(@NonNull View itemView) {
           super(itemView);

           itemLocation = itemView.findViewById(R.id.itemLocation);
           itemDestination = itemView.findViewById(R.id.itemDestination);
           itemCurrentDate = itemView.findViewById(R.id.Date);
       }
   }
}
