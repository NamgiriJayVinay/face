// ================ AndroidManifest.xml (add inside application tag) ================
<receiver android:name=".SwimmingPoolWidget" 
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/swimming_pool_widget_info" />
</receiver>

// ================ res/xml/swimming_pool_widget_info.xml ================
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="250dp"
    android:minHeight="110dp"
    android:updatePeriodMillis="1800000"
    android:initialLayout="@layout/swimming_pool_widget"
    android:resizeMode="horizontal|vertical"
    android:widgetCategory="home_screen"
    android:previewLayout="@layout/swimming_pool_widget"/>

// ================ res/layout/swimming_pool_widget.xml ================
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/widget_background"
    android:padding="16dp">

    <LinearLayout
        android:id="@+id/headerLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical">

        <ImageView
            android:id="@+id/iconView"
            android:layout_width="24dp"
            android:layout_height="24dp"
            android:src="@drawable/ic_swimming"
            android:tint="#FFFFFF" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Swimming pool"
            android:textColor="#FFFFFF"
            android:textSize="16sp"
            android:layout_marginStart="8dp"/>
    </LinearLayout>

    <TextView
        android:id="@+id/sessionTitleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/headerLayout"
        android:layout_marginTop="12dp"
        android:text="Beginners session"
        android:textColor="#FFFFFF"
        android:textSize="20sp"/>

    <TextView
        android:id="@+id/timeText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/sessionTitleText"
        android:layout_marginTop="4dp"
        android:text="10:00 – 10:45"
        android:textColor="#FFFFFF"
        android:textSize="18sp"/>

</RelativeLayout>

// ================ res/drawable/widget_background.xml ================
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#4B75F2"/>
    <corners android:radius="16dp"/>
</shape>

// ================ res/drawable/ic_swimming.xml ================
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M22,21c-1.11,0-1.73,-0.37-2.18,-0.64-0.37,-0.22-0.6,-0.36-1.15,-0.36-0.56,0-0.78,0.13-1.15,0.36-0.46,0.27-1.07,0.64-2.18,0.64s-1.73,-0.37-2.18,-0.64c-0.37,-0.22-0.6,-0.36-1.15,-0.36-0.56,0-0.78,0.13-1.15,0.36-0.46,0.27-1.08,0.64-2.19,0.64-1.11,0-1.73,-0.37-2.18,-0.64-0.37,-0.23-0.6,-0.36-1.15,-0.36s-0.78,0.13-1.15,0.36c-0.46,0.27-1.08,0.64-2.19,0.64v-2c0.56,0,0.78,-0.13,1.15,-0.36,0.46,-0.27,1.08,-0.64,2.19,-0.64s1.73,0.37,2.18,0.64c0.37,0.23,0.59,0.36,1.15,0.36,0.56,0,0.78,-0.13,1.15,-0.36,0.46,-0.27,1.08,-0.64,2.19,-0.64,1.11,0,1.73,0.37,2.18,0.64,0.37,0.22,0.6,0.36,1.15,0.36s0.78,-0.13,1.15,-0.36c0.45,-0.27,1.07,-0.64,2.18,-0.64s1.73,0.37,2.18,0.64c0.37,0.23,0.59,0.36,1.15,0.36v2z"/>
</vector>

// ================ SwimmingPoolWidget.java ================
package com.example.yourapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class SwimmingPoolWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.swimming_pool_widget);
        
        // Update widget content
        views.setTextViewText(R.id.sessionTitleText, "Beginners session");
        views.setTextViewText(R.id.timeText, "10:00 – 10:45");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}