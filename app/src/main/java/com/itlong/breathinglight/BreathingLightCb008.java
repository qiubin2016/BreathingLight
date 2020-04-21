package com.itlong.breathinglight;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class BreathingLightCb008 {
    private static final String TAG = BreathingLightCb008.class.getSimpleName();
    private static final int MAX_END = 255;
    private static final int MIN_BEGIN = 0;
    private static final int STEP = 1;

    private int actionStep;  //当前亮度等级
    private boolean breatheDirection;  //true：呼气，false：吸气
    private long breathePeriod;    //吸气/呼气的间隔
    private int stepChangeCount;    //每个吸气/呼气亮度调节次数
    private long stepChangePeriod;  //每次吸气/呼气中每级亮度的维持时间
    private Timer timerChangeStep;
    private Action action;

    public BreathingLightCb008(int times, byte beginLevel, byte endLevel, Action action) {
        actionStep = (int)(beginLevel & 0xFF);  //当前亮度等级
        breatheDirection = false;  //默认吸气
        breathePeriod = 60 * 1000 / times / 2;  //一次呼气/吸气的时间
        stepChangeCount = (int)(endLevel & 0xFF) - (int)(beginLevel & 0xFF);  //例如：255级 - 0级 = 255
        stepChangePeriod = breathePeriod / stepChangeCount;  //增加/减少一级亮度的间隔
        this.action = action;
        Log.i(TAG, "beginLevel:" + (int)(beginLevel & 0xFF) + ",endLevel:" + (int)(endLevel & 0xFF) + ",actionStep:" + actionStep);
        Log.i(TAG, "stepChangePeriod:" + stepChangePeriod + ",stepChangeCount:" + (int)stepChangeCount);
    }

    public synchronized void start() {
        timerChangeStep = new Timer();
        timerChangeStep.schedule(new TimerTask() {
            @Override
            public void run() {
                timerChangeStepProc();
            }
        }, 0, stepChangePeriod);
    }

    public synchronized void stop() {
        if (null != timerChangeStep) {
            timerChangeStep.cancel();
            timerChangeStep = null;
            action.setLedBrightness((byte)MIN_BEGIN);  //关闭LED
        }
    }

    private synchronized void timerChangeStepProc() {
        int tmpStep;

        if (breatheDirection) {  //呼气，actionStep要自减
            tmpStep = actionStep - STEP;  //亮度减一级
            if (tmpStep >= MIN_BEGIN) {
                actionStep = tmpStep;
                if (MIN_BEGIN == actionStep) {
                    breatheDirection = !breatheDirection;    //呼气变吸气
                    Log.i(TAG, "change direction:" + breatheDirection);
                }
            }
        } else {  //吸气，actionStep要自增
            tmpStep = actionStep + STEP;
            if (tmpStep <= MAX_END) {
                Log.i(TAG, "-----1");
                actionStep = tmpStep;
                if (MAX_END == actionStep) {
                    breatheDirection = !breatheDirection;    //吸气变呼气
                    Log.i(TAG, "change direction:" + breatheDirection);
                }
            } else {
                Log.i(TAG, "-----2");
            }
            Log.i(TAG, "-----3");
        }
        Log.i(TAG, "change actionStep:" + actionStep);
        action.setLedBrightness((byte)actionStep);
    }

    public interface Action {
        void setLedBrightness(byte brightness);
    }

}
