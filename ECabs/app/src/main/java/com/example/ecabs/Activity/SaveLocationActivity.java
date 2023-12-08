package com.example.ecabs.Activity;

import static com.example.ecabs.Activity.ProfileActivity.KEY_EMAIL;
import static com.example.ecabs.Fragments.SearchFragment.SHARED_PREF_NAME;
import static com.example.ecabs.Fragments.SearchFragment.userDes;
import static com.example.ecabs.Fragments.SearchFragment.userLoc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecabs.R;
import com.example.ecabs.Utils.History_Adapter;
import com.example.ecabs.Utils.Search_History;
import com.example.ecabs.databinding.ActivitySaveLocationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SaveLocationActivity extends AppCompatActivity {

    ActivitySaveLocationBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DatabaseReference database;

    RecyclerView recyclerView;

    History_Adapter myAdapter;

    ArrayList<Search_History> list;

    String location;
    String destination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveLocationBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        location = preferences.getString(userLoc, null);
        destination = preferences.getString(userDes, null);

        String username = preferences.getString(KEY_EMAIL, null);

        ImageView back = binding.eastBack;

        recyclerView = findViewById(R.id.savedLocationList);

        database = FirebaseDatabase.getInstance().getReference("users").child(username).child("histories");
        recyclerView.setLayoutManager(new LinearLayoutManager(SaveLocationActivity.this));

        list = new ArrayList<>();
        myAdapter = new History_Adapter(SaveLocationActivity.this, list);
        recyclerView.setAdapter(myAdapter);

       database.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   Search_History history = dataSnapshot.getValue(Search_History.class);
                   list.add(history);
               }
               myAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}