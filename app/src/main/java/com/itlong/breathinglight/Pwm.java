package com.itlong.breathinglight;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Pwm {
    private static final String TAG = Pwm.class.getSimpleName();

    private Timer timerOn;
    private Timer timerOff;
    private Action action;
    private long timeOn;
    private long timeOff;

    public Pwm(final Action action, long period, float duty) {
        this.action = action;
        timeOn = (long) (period * duty);  //计算on()的时间
        timeOff = period - timeOn;        //计算off()的时间
    }

    synchronized void start() {
        action.on();    //on
        timerOn = new Timer();
        timerOff = new Timer();
        try {
            timerOn.schedule(new TimerTask() {
                @Override
                public void run() {
//                    Log.i(TAG, "timerTaskOffRun enter");
                    timerTaskOffRun();
//                    Log.i(TAG, "timerTaskOffRun leave");
                }
            }, timeOn);  //从现在起过timerOn ms执行一次
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,"start");
    }

    synchronized void stop() {
        action.off();
        if (null != timerOn) {
            timerOn.cancel();
            timerOn.purge();
            timerOn = null;
        }
        if (null != timerOff) {
            timerOff.cancel();
            timerOff.purge();
            timerOff = null;
        }
        Log.i(TAG,"stop");
    }

    private synchronized void timerTaskOnRun() {
        action.off();   //on() finish, turn off()
        Log.i(TAG,"timerTaskOn run");
        try {
            if (null != timerOff) {
                timerOff.schedule(new TimerTask() {
                    @Override
                    public void run() {
//                        Log.i(TAG, "timerTaskOffRun enter1");
                        timerTaskOffRun();
//                        Log.i(TAG, "timerTaskOffRun leave1");
                    }
                }, timeOff);  //从现在起过timerOff ms执行一次
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void timerTaskOffRun() {
        action.on();  //off() finish, turn on()
        Log.i(TAG,"timerTaskOff run");
        try {
            if (null != timerOn) {
                timerOn.schedule(new TimerTask() {
                    @Override
                    public void run() {
//                        Log.i(TAG, "timerTaskOnRun enter");
                        timerTaskOnRun();
//                        Log.i(TAG, "timerTaskOnRun enter");
                    }
                }, timeOn);  //从现在起过timerOn ms执行一次
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Action {
        void on();
        void off();
    }
}
