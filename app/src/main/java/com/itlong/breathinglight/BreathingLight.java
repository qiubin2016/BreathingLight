package com.itlong.breathinglight;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class BreathingLight {
    private static final String TAG = BreathingLight.class.getSimpleName();

    private static final float MAX_END = 1.0f;
    private static final float MIN_BEGIN = 0.0f;
    private static final float EQUAL_FLOAT_VALUE = 0.01f;    //比较float值相等的精度
    private static final float STEP = 0.1f;
    private static final long DEF_PERIOD = 10;
    private float begin;
    private float end;
    private float actionStep;
    private Pwm pwm;
    private boolean breatheDirection;  //true：呼气，false：吸气
    private long breathePeriod;    //吸气/呼气的间隔
    private int stepChangeCount;    //每个步长维持的次数
    private long stepChangePeriod;
    private Pwm.Action action;
    private Timer timerChangeDirection;
    private Timer timerChangeStep;

    public BreathingLight(int times, float begin, float end, final Pwm.Action action) {
        this.begin = (float) ((begin * 1000) / 100 / 10);
        this.end = (float) ((end * 1000) / 100 / 10);
        actionStep = this.begin;
        breatheDirection = false;
        breathePeriod = 60 * 1000 / times / 2;  //1分钟内次数times
        stepChangeCount = (int)((this.end - this.begin) / STEP);  //例如：10%—100%变化，STEP = 9
        stepChangePeriod = breathePeriod / stepChangeCount;  //多久要切换一次actionStep
        this.action = action;
    }

    public synchronized void start() {
//        timerChangeDirection = new Timer();
//        timerChangeDirection.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                breatheDirection = !breatheDirection;  //变换方向
//            }
//        }, 0, breathePeriod);

        timerChangeStep = new Timer();
        timerChangeStep.schedule(new TimerTask() {
            @Override
            public void run() {
                timerChangeStepProc();
            }
        }, 0, stepChangePeriod);

        if (null != pwm) {
            pwm.stop();
            pwm = null;
        }
        pwm = new Pwm(action, DEF_PERIOD, actionStep);
        pwm.start();
    }

    private synchronized void timerChangeStepProc() {
        float tmpStep;

        if (breatheDirection) {  //呼气，actionStep要自减
            tmpStep = actionStep - STEP;
            if (tmpStep > MIN_BEGIN) {
                actionStep = tmpStep;
                if ((actionStep < begin) || (Math.abs(actionStep - begin) <= EQUAL_FLOAT_VALUE)) {
                    breatheDirection = !breatheDirection;    //呼气变吸气
                    Log.i(TAG, "change direction:" + breatheDirection);
                }
            }
        } else {  //吸气，actionStep要自增
            tmpStep = actionStep + STEP;
//            Log.i(TAG, "actionStep:" + tmpStep + ",end:" + end + ",val:" + MAX_END);
            if ((tmpStep < MAX_END) || (Math.abs(tmpStep - MAX_END) <= EQUAL_FLOAT_VALUE)){
                actionStep = tmpStep;
                if ((actionStep > end) || (Math.abs(actionStep - end) <= EQUAL_FLOAT_VALUE)) {
                    breatheDirection = !breatheDirection;    //吸气变呼气
                    Log.i(TAG, "change direction:" + breatheDirection);
                } else {
//                    Log.i(TAG, "actionStep < end");
                }
            } else {
//                Log.i(TAG, "> 1.0f");
            }
        }
        Log.i(TAG, "change actionStep:" + actionStep);
        //按新的占空比运行
        if (null != pwm) {
            pwm.stop();
            pwm = null;
        }
        pwm = new Pwm(action, DEF_PERIOD, actionStep);
        pwm.start();
    }

    public synchronized void stop() {
        if (null != pwm) {
            pwm.stop();
            pwm = null;
        }
        if (null != timerChangeStep) {
            timerChangeStep.cancel();
            timerChangeStep = null;
        }
    }
}
