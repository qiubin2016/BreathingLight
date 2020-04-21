package com.itlong.breathinglight;

import android.app.Application;
import android.os.Build;
import android.util.Log;

public class CfgApp extends Application {
    private static final String TAG = CfgApp.class.getSimpleName();

    private static final String MANUFACTURER_BIHU = "CreateBest";

    private static boolean isCreateBest;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (21 == Build.VERSION.SDK_INT)
        if (MANUFACTURER_BIHU.equals(Build.MANUFACTURER)) {
            Log.i(TAG, "is CreateBest");
            isCreateBest = true;
        }
        Log.i(TAG, "----------------------------");
    }

    public static boolean isCreateBest() {
        return isCreateBest;
    }
}
