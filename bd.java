
public class AppInstallationReceiver extends BroadcastReceiver {
    private static final String TAG = "PrivacyTipsService:AppInstallationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Uri data  = intent.getData();
        String packageName = data != null ? data.getSchemeSpecificPart() : null;

        if (action != null && packageName != null) {
           if(action.equals(Intent.ACTION_PACKAGE_ADDED)) {
               Log.i(TAG,packageName+" Installed !!!");
           } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
               Log.i(TAG,packageName+" Uninstalled !!!");
           }
        } else {
            Log.e(TAG, "Intent Action or Package Name is Null");
        }
    }
}





 <receiver
            android:name="com.samsung.android.privacytips_ut.UI.AppInstallationReceiver"
            android:exported="false">
            <intent-filter>
                <action android:name="android.intent.action.PACKAGE_ADDED" />
                <action android:name="android.intent.action.PACKAGE_REMOVED" />
                <data android:scheme="package" />
            </intent-filter>
        </receiver>
