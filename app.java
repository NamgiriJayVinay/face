// Step 1: First, make sure to add a ProgressBar to your activity layout
// activity_main.xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Your other views here -->
    
    <!-- Loading Progress -->
    <ProgressBar
        android:id="@+id/loadingProgressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:visibility="gone" />

</RelativeLayout>

// Step 2: In your MainActivity.java
package com.example.yourapp;

import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize the ProgressBar
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        
        // Example button click to start a long process
        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show loading and execute long-running task
                new LongRunningTask().execute();
            }
        });
    }
    
    // Method to show the loading progress
    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }
    
    // Method to hide the loading progress
    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }
    
    // Method that takes a long time to execute (example)
    private void longRunningMethod() {
        // This is your long-running code
        try {
            // Simulate a long operation
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // AsyncTask to handle background processing
    private class LongRunningTask extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected void onPreExecute() {
            // This runs on the UI thread before background processing starts
            showLoading();
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            // This runs on a background thread
            longRunningMethod();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            // This runs on the UI thread after background processing completes
            hideLoading();
            // Update UI or show results here
        }
    }
}

// Step 3: Alternative implementation using modern approach with ExecutorService
// Modern implementation (replacing AsyncTask which is deprecated)
package com.example.yourapp.modern;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModernMainActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize the ProgressBar
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        
        // Create an executor service for background tasks
        executorService = Executors.newSingleThreadExecutor();
        
        // Handler for main/UI thread
        mainHandler = new Handler(Looper.getMainLooper());
        
        // Example button click to start a long process
        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show loading and execute long-running task
                executeTaskWithLoading();
            }
        });
    }
    
    private void executeTaskWithLoading() {
        // Show loading indicator
        showLoading();
        
        // Execute task in background
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Perform long running operation
                longRunningMethod();
                
                // Update UI on main thread when done
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        // Update UI or show results here
                    }
                });
            }
        });
    }
    
    // Show loading progress
    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }
    
    // Hide loading progress
    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }
    
    // Your long running method
    private void longRunningMethod() {
        // This is your long-running code
        try {
            // Simulate a long operation
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onDestroy() {
        // Clean up executor when activity is destroyed
        if (executorService != null) {
            executorService.shutdown();
        }
        super.onDestroy();
    }
}