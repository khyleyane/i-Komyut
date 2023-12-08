package com.example.ecabs.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecabs.R;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<TodaItem> itemList;

    public MyAdapter(List<TodaItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.toda_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodaItem item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView todaName, todaLoc, todaFare;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            todaName = itemView.findViewById(R.id.itemTodaName);
            todaLoc = itemView.findViewById(R.id.itemTodaLoc);
            todaFare = itemView.findViewById(R.id.itemTodaFare);
        }

        public void bind(TodaItem item) {
            // Update views with data for each item
            todaName.setText(item.getTodaName());
            todaLoc.setText(item.getTodaLoc());
            todaFare.setText("₱" + String.valueOf(item.getTodaFare()));
            // You can set image or other views as per your new layout
        }
    }
}
