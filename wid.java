<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ffffff">

    <!-- ViewFlipper for switching widgets -->
    <ViewFlipper
        android:id="@+id/widgetViewFlipper"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp">

        <!-- First Widget -->
        <TextView
            android:id="@+id/textView1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="Text 1"
            android:textSize="18sp"
            android:textColor="#000000" />

        <!-- Second Widget -->
        <TextView
            android:id="@+id/textView2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="Text 2"
            android:textSize="18sp"
            android:textColor="#000000" />
    </ViewFlipper>

    <!-- Dot indicators -->
    <LinearLayout
        android:id="@+id/dotIndicatorLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:orientation="horizontal"
        android:layout_marginBottom="16dp">
        <ImageView
            android:id="@+id/dot1"
            android:layout_width="8dp"
            android:layout_height="8dp"
            android:layout_margin="4dp"
            android:background="@drawable/dot_active" />
        <ImageView
            android:id="@+id/dot2"
            android:layout_width="8dp"
            android:layout_height="8dp"
            android:layout_margin="4dp"
            android:background="@drawable/dot_inactive" />
    </LinearLayout>
</RelativeLayout>


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    private static int currentIndex = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Handle ViewFlipper state
        views.setDisplayedChild(R.id.widgetViewFlipper, currentIndex);

        // Handle dot indicators
        if (currentIndex == 0) {
            views.setImageViewResource(R.id.dot1, R.drawable.dot_active);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_inactive);
        } else {
            views.setImageViewResource(R.id.dot1, R.drawable.dot_inactive);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_active);
        }

        // Intent to switch to the next widget
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction("SLIDE_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetViewFlipper, pendingIntent);

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("SLIDE_WIDGET".equals(intent.getAction())) {
            currentIndex = (currentIndex + 1) % 2; // Toggle between 0 and 1
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));
            for (int appWidgetId : appWidgetIds) {
                updateWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }
}





<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:initialLayout="@layout/widget_layout"
    android:minWidth="200dp"
    android:minHeight="100dp"
    android:updatePeriodMillis="0"
    android:resizeMode="horizontal|vertical"
    android:widgetCategory="home_screen" />




new code image refernece 

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/widget_background"
    android:padding="16dp">

    <!-- ViewFlipper for sliding content -->
    <ViewFlipper
        android:id="@+id/widgetViewFlipper"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp">

        <!-- First widget view -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:id="@+id/title1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Swimming pool"
                android:textSize="16sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="8dp" />

            <TextView
                android:id="@+id/subtitle1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Beginners session"
                android:textSize="14sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="4dp" />

            <TextView
                android:id="@+id/time1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="10:00 – 10:45"
                android:textSize="14sp"
                android:textColor="#FFFFFF" />
        </LinearLayout>

        <!-- Second widget view -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:id="@+id/title2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Swimming pool"
                android:textSize="16sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="8dp" />

            <TextView
                android:id="@+id/subtitle2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Senior session"
                android:textSize="14sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="4dp" />

            <TextView
                android:id="@+id/time2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="11:00 – 11:45"
                android:textSize="14sp"
                android:textColor="#FFFFFF" />
        </LinearLayout>
    </ViewFlipper>

    <!-- Dot indicators -->
    <LinearLayout
        android:id="@+id/dotIndicatorLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:orientation="horizontal">

        <ImageView
            android:id="@+id/dot1"
            android:layout_width="8dp"
            android:layout_height="8dp"
            android:layout_margin="4dp"
            android:background="@drawable/dot_active" />

        <ImageView
            android:id="@+id/dot2"
            android:layout_width="8dp"
            android:layout_height="8dp"
            android:layout_margin="4dp"
            android:background="@drawable/dot_inactive" />
    </LinearLayout>
</RelativeLayout>



import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    private static int currentIndex = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Update ViewFlipper
        views.setDisplayedChild(R.id.widgetViewFlipper, currentIndex);

        // Update dot indicators
        if (currentIndex == 0) {
            views.setImageViewResource(R.id.dot1, R.drawable.dot_active);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_inactive);
        } else {
            views.setImageViewResource(R.id.dot1, R.drawable.dot_inactive);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_active);
        }

        // Handle sliding action
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction("FLIP_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetViewFlipper, pendingIntent);

        // Update widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("FLIP_WIDGET".equals(intent.getAction())) {
            currentIndex = (currentIndex + 1) % 2; // Toggle between views
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));
            for (int appWidgetId : appWidgetIds) {
                updateWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }
}

swap behavior 
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    private static int currentIndex = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Show or hide views based on currentIndex
        if (currentIndex == 0) {
            views.setViewVisibility(R.id.firstView, View.VISIBLE);
            views.setViewVisibility(R.id.secondView, View.GONE);
            views.setImageViewResource(R.id.dot1, R.drawable.dot_active);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_inactive);
        } else {
            views.setViewVisibility(R.id.firstView, View.GONE);
            views.setViewVisibility(R.id.secondView, View.VISIBLE);
            views.setImageViewResource(R.id.dot1, R.drawable.dot_inactive);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_active);
        }

        // Set up left swipe intent
        Intent leftIntent = new Intent(context, MyWidgetProvider.class);
        leftIntent.setAction("SWIPE_LEFT");
        PendingIntent leftPendingIntent = PendingIntent.getBroadcast(context, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.firstView, leftPendingIntent);

        // Set up right swipe intent
        Intent rightIntent = new Intent(context, MyWidgetProvider.class);
        rightIntent.setAction("SWIPE_RIGHT");
        PendingIntent rightPendingIntent = PendingIntent.getBroadcast(context, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.secondView, rightPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("SWIPE_LEFT".equals(intent.getAction())) {
            currentIndex = 0; // Swipe to first view
        } else if ("SWIPE_RIGHT".equals(intent.getAction())) {
            currentIndex = 1; // Swipe to second view
        }

        // Update all widget instances
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }
}



<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/widget_background"
    android:padding="16dp">

    <!-- FrameLayout to handle swiping -->
    <FrameLayout
        android:id="@+id/widgetFrame"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp">

        <!-- First widget view -->
        <LinearLayout
            android:id="@+id/firstView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:visibility="visible">

            <TextView
                android:id="@+id/title1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Swimming pool"
                android:textSize="16sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="8dp" />

            <TextView
                android:id="@+id/subtitle1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Beginners session"
                android:textSize="14sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="4dp" />

            <TextView
                android:id="@+id/time1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="10:00 – 10:45"
                android:textSize="14sp"
                android:textColor="#FFFFFF" />
        </LinearLayout>

        <!-- Second widget view -->
        <LinearLayout
            android:id="@+id/secondView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:visibility="gone">

            <TextView
                android:id="@+id/title2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Swimming pool"
                android:textSize="16sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="8dp" />

            <TextView
                android:id="@+id/subtitle2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Senior session"
                android:textSize="14sp"
                android:textColor="#FFFFFF"
                android:layout_marginBottom="4dp" />

            <TextView
                android:id="@+id/time2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="11:00 – 11:45"
                android:textSize="14sp"
                android:textColor="#FFFFFF" />
        </LinearLayout>
    </FrameLayout>

    <!-- Dot indicators -->
    <LinearLayout
        android:id="@+id/dotIndicatorLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:orientation="horizontal">

        <ImageView
            android:id="@+id/dot1"
            android:layout_width="8dp"
            android:layout_height="8dp"
            android:layout_margin="4dp"
            android:background="@drawable/dot_active" />

        <ImageView
            android:id="@+id/dot2"
            android:layout_width="8dp"
            android:layout_height="8dp"
            android:layout_margin="4dp"
            android:background="@drawable/dot_inactive" />
    </LinearLayout>
</RelativeLayout>


swap 2
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    private static int currentIndex = 0; // Tracks which view is visible

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Update visibility based on currentIndex
        if (currentIndex == 0) {
            views.setViewVisibility(R.id.firstView, View.VISIBLE);
            views.setViewVisibility(R.id.secondView, View.GONE);
            views.setImageViewResource(R.id.dot1, R.drawable.dot_active);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_inactive);
        } else {
            views.setViewVisibility(R.id.firstView, View.GONE);
            views.setViewVisibility(R.id.secondView, View.VISIBLE);
            views.setImageViewResource(R.id.dot1, R.drawable.dot_inactive);
            views.setImageViewResource(R.id.dot2, R.drawable.dot_active);
        }

        // Swipe left action
        Intent leftIntent = new Intent(context, MyWidgetProvider.class);
        leftIntent.setAction("SWIPE_LEFT");
        PendingIntent leftPendingIntent = PendingIntent.getBroadcast(
                context, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.firstView, leftPendingIntent);

        // Swipe right action
        Intent rightIntent = new Intent(context, MyWidgetProvider.class);
        rightIntent.setAction("SWIPE_RIGHT");
        PendingIntent rightPendingIntent = PendingIntent.getBroadcast(
                context, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.secondView, rightPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("SWIPE_LEFT".equals(intent.getAction())) {
            currentIndex = 0; // Show the first view
        } else if ("SWIPE_RIGHT".equals(intent.getAction())) {
            currentIndex = 1; // Show the second view
        }

        // Update all widget instances
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }
}