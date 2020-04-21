package com.itlong.breathinglight;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class Cb008Operation {
    private static final String TAG = Cb008Operation.class.getSimpleName();

    public static void ledOn() {
        Log.i(TAG, "ledOn");
        ledOperation("1");
    }

    public static void ledOff() {
        ledOperation("0");
    }

    public static void ledOperation(String status) {
        Log.i(TAG, "ledOperation");
        try {
            FileOutputStream fos = new FileOutputStream("/sys/exgpio/led_en");
            fos.write(status.getBytes());
            fos.close();
            Log.i(TAG, "led operation");
        } catch (Exception e) {
            Log.i(TAG, "exception");
            e.printStackTrace();
        }
    }

    public static void setLedBrightness(byte brightness) {
        String status = String.valueOf(brightness & 0xff);
        try {
            Log.i(TAG, "/sys/class/leds/wled/brightness");
            FileOutputStream fos = new 	FileOutputStream("/sys/class/leds/wled/brightness");
            fos.write(status.getBytes());
            fos.close();
            Log.i(TAG, "/sys/class/leds/wled/brightness close");
        } catch (Exception e) {
            Log.i(TAG, "exception");
            e.printStackTrace();
        }
    }
}
