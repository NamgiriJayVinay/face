    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);

        initialiseDataBase();

        initialUIComponents();

        runOnUiThread(this::getCardData);
    }


nnn
// First, modify your layout file (home_activity.xml) to include a loading view

/* home_activity.xml - Add this to your existing layout */
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Your existing layout content here -->
    
    <!-- Loading overlay -->
    <RelativeLayout
        android:id="@+id/loadingLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#80000000"
        android:visibility="visible">
        
        <pl.droidsonroids.gif.GifImageView
            android:id="@+id/loadingGIF"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_centerInParent="true"
            android:src="@drawable/loading_gif" />
            
    </RelativeLayout>
</RelativeLayout>

// Then, update your MainActivity.java

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout loadingLayout;
    private boolean isInitialized = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        
        // Get the loading layout reference
        loadingLayout = findViewById(R.id.loadingLayout);
        
        // Show loading
        showLoading();
        
        // Start initialization process
        startInitialization();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // If we're already initialized but returning to the activity, 
        // we may need to update UI components
        if (isInitialized) {
            // Show loading during UI refresh
            showLoading();
            
            // Update UI components
            refreshUIComponents();
            
            // After a short delay, hide loading
            handler.postDelayed(this::hideLoading, 300);
        }
    }
    
    private void showLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.VISIBLE);
        }
    }
    
    private void hideLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }
    
    private void startInitialization() {
        // Run initialization on a background thread
        new Thread(() -> {
            // Do initialization work
            initialiseDataBase();
            
            // Switch to UI thread for UI components initialization
            runOnUiThread(() -> {
                initialUIComponents();
                
                // Get card data 
                getCardData();
                
                // Mark as initialized
                isInitialized = true;
                
                // Hide loading when everything is done
                hideLoading();
            });
        }).start();
    }
    
    private void refreshUIComponents() {
        // Update any UI components that need refreshing in onResume
        // Add your UI refresh code here
    }
    
    private void initialiseDataBase() {
        // Your existing database initialization code
    }
    
    private void initialUIComponents() {
        // Your existing UI initialization code
    }
    
    private void getCardData() {
        // Your existing card data retrieval code
  mmmmm

// Option 1: Using Android's built-in ProgressBar or AnimationDrawable

/* res/drawable/loading_animation.xml - Create an animated drawable */
<?xml version="1.0" encoding="utf-8"?>
<animated-rotate xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/loading_circle"
    android:pivotX="50%"
    android:pivotY="50%" />

/* res/drawable/loading_circle.xml - Create the circle to be rotated */
<?xml version="1.0" encoding="utf-8"?>
<shape
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="ring"
    android:innerRadiusRatio="3"
    android:thicknessRatio="8"
    android:useLevel="false">
    <gradient
        android:type="sweep"
        android:startColor="#00000000"
        android:endColor="#6200EE"
        android:useLevel="false" />
</shape>

/* home_activity.xml - Add this to your existing layout */
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Your existing layout content here -->
    
    <!-- Loading overlay -->
    <RelativeLayout
        android:id="@+id/loadingLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#80000000"
        android:visibility="visible">
        
        <!-- Option 1: Custom AnimationDrawable -->
        <ImageView
            android:id="@+id/loadingAnimation"
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:layout_centerInParent="true"
            android:src="@drawable/loading_animation" />
            
        <!-- Option 2: Standard Android ProgressBar -->
        <!-- 
        <ProgressBar
            android:id="@+id/loadingProgressBar"
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:layout_centerInParent="true" 
            android:indeterminate="true"/>
        -->
        
    </RelativeLayout>
</RelativeLayout>

// Updated MainActivity.java

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout loadingLayout;
    private boolean isInitialized = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        
        // Get the loading layout reference
        loadingLayout = findViewById(R.id.loadingLayout);
        
        // Show loading
        showLoading();
        
        // Start initialization process
        startInitialization();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // If we're already initialized but returning to the activity, 
        // we may need to update UI components
        if (isInitialized) {
            // Show loading during UI refresh
            showLoading();
            
            // Update UI components
            refreshUIComponents();
            
            // After a short delay, hide loading
            handler.postDelayed(this::hideLoading, 300);
        }
    }
    
    private void showLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.VISIBLE);
        }
    }
    
    private void hideLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }
    
    private void startInitialization() {
        // Run initialization on a background thread
        new Thread(() -> {
            // Do initialization work
            initialiseDataBase();
            
            // Switch to UI thread for UI components initialization
            runOnUiThread(() -> {
                initialUIComponents();
                
                // Get card data 
                getCardData();
                
                // Mark as initialized
                isInitialized = true;
                
                // Hide loading when everything is done
                hideLoading();
            });
        }).start();
    }
    
    private void refreshUIComponents() {
        // Update any UI components that need refreshing in onResume
        // Add your UI refresh code here
    }
    
    private void initialiseDataBase() {
        // Your existing database initialization code
    }
    
    private void initialUIComponents() {
        // Your existing UI initialization code
    }
    
    private void getCardData() {
        // Your existing card data retrieval code
    }
}

// Option 2 - Using Lottie Animation (requires Lottie dependency but is built into many Android apps now)
/*
Add to build.gradle:
implementation 'com.airbnb.android:lottie:5.2.0'

Replace the loading view in XML with:
<com.airbnb.lottie.LottieAnimationView
    android:id="@+id/loadingAnimation"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_centerInParent="true"
    app:lottie_autoPlay="true"
    app:lottie_loop="true"
    app:lottie_rawRes="@raw/loading_animation" />

Place your .json Lottie animation file in res/raw/loading_animation.json
*/

// Option 3 - Using WebView to display GIF
/*
<WebView
    android:id="@+id/gifWebView" 
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_centerInParent="true"
    android:background="@android:color/transparent" />

// In your activity:
WebView webView = findViewById(R.id.gifWebView);
webView.setBackgroundColor(Color.TRANSPARENT);
webView.loadUrl("file:///android_asset/loading.gif");
// Put loading.gif in your assets folder
*/