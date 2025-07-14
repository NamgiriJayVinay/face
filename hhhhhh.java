package com.example.recyclerviewapp;

public class PkgLocationType {
    
    public String pkgName;
    public String appLabel;
    public String locationAccess;
    
    public PkgLocationType(String pkgName, String appLabel, String locationAccess) {
        this.pkgName = pkgName;
        this.appLabel = appLabel;
        this.locationAccess = locationAccess;
    }
    
    // Getter methods
    public String getPkgName() {
        return pkgName;
    }
    
    public String getAppLabel() {
        return appLabel;
    }
    
    public String getLocationAccess() {
        return locationAccess;
    }
    
    // Setter methods
    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
    
    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }
    
    public void setLocationAccess(String locationAccess) {
        this.locationAccess = locationAccess;
    }
}



package com.example.recyclerviewapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<PkgLocationType> pkgLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list with dummy data
        pkgLocationList = new ArrayList<>();
        createDummyData();

        // Set up the adapter
        adapter = new CustomAdapter(pkgLocationList);
        recyclerView.setAdapter(adapter);
    }

    private void createDummyData() {
        pkgLocationList.add(new PkgLocationType("com.google.android.gms", "Google Play Services", "Granted"));
        pkgLocationList.add(new PkgLocationType("com.android.chrome", "Chrome Browser", "Denied"));
        pkgLocationList.add(new PkgLocationType("com.whatsapp", "WhatsApp Messenger", "Granted"));
        pkgLocationList.add(new PkgLocationType("com.facebook.katana", "Facebook", "Granted"));
        pkgLocationList.add(new PkgLocationType("com.instagram.android", "Instagram", "Denied"));
        pkgLocationList.add(new PkgLocationType("com.spotify.music", "Spotify Music", "Granted"));
        pkgLocationList.add(new PkgLocationType("com.netflix.mediaclient", "Netflix", "Denied"));
        pkgLocationList.add(new PkgLocationType("com.uber.app", "Uber", "Granted"));
        pkgLocationList.add(new PkgLocationType("com.amazon.mShop.android.shopping", "Amazon Shopping", "Granted"));
        pkgLocationList.add(new PkgLocationType("com.twitter.android", "Twitter", "Denied"));
    }

    // RecyclerView Adapter
    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private List<PkgLocationType> dataList;

        public CustomAdapter(List<PkgLocationType> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PkgLocationType item = dataList.get(position);
            holder.pkgNameTextView.setText(item.getPkgName());
            holder.appLabelTextView.setText(item.getAppLabel());
            holder.locationAccessTextView.setText(item.getLocationAccess());
            
            // Set different colors for granted/denied status
            if (item.getLocationAccess().equals("Granted")) {
                holder.locationAccessTextView.setTextColor(0xFF4CAF50); // Green
            } else {
                holder.locationAccessTextView.setTextColor(0xFFF44336); // Red
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView pkgNameTextView;
            TextView appLabelTextView;
            TextView locationAccessTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                pkgNameTextView = itemView.findViewById(R.id.pkgNameTextView);
                appLabelTextView = itemView.findViewById(R.id.appLabelTextView);
                locationAccessTextView = itemView.findViewById(R.id.locationAccessTextView);
            }
        }
    }
}




<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="4dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:id="@+id/appLabelTextView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="App Label"
            android:textSize="18sp"
            android:textStyle="bold"
            android:textColor="#333333"
            android:layout_marginBottom="4dp" />

        <TextView
            android:id="@+id/pkgNameTextView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Package Name"
            android:textSize="12sp"
            android:textColor="#666666"
            android:layout_marginBottom="4dp"
            android:fontFamily="monospace" />

        <TextView
            android:id="@+id/locationAccessTextView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Location Access"
            android:textSize="14sp"
            android:textStyle="bold"
            android:textColor="#888888" />

    </LinearLayout>

</androidx.cardview.widget.CardView>