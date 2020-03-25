package com.itlong.breathinglight;

import androidx.appcompat.app.AppCompatActivity;

import android.app.smdt.SmdtManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Pwm.Action{
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final long DEF_PERIOD = 10;

    private boolean onClickFlag;
    private Timer timer;
    private TimerTask timerTask;

    @BindView(R.id.whiteCheckBox)CheckBox whiteCheckBox;
    @BindView(R.id.redCheckBox)CheckBox redCheckBox;
    @BindView(R.id.greenCheckBox)CheckBox greenCheckBox;
    @BindView(R.id.timesEditText)EditText timesEditText;
    @BindView(R.id.testButton1) Button testButton;
    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.tenPercentButton)Button tenPercentButton;
    @BindView(R.id.twentyPercentButton) Button twentyPercentButton;
    @BindView(R.id.thrityPercentButton)Button thrityPercentButton;
    @BindView(R.id.fortyPercentButton)Button fortyPercentButton;
    @BindView(R.id.fiftyPercentButton)Button fiftyPercentButton;
    @BindView(R.id.sixtyPercentButton)Button sixtyPercentButton;
    @BindView(R.id.seventyPercentButton)Button seventyPercentButton;
    @BindView(R.id.eightyPercentButton)Button eightyPercentButton;
    @BindView(R.id.ninetyPercentButton)Button ninetyPercentButton;
    @BindView(R.id.hundredPercentButton)Button hundredPercentButton;

    boolean isWhiteCheck, isRedCheck, isGreenCheck;

    private SmdtManager smdt;
    private Pwm pwm;
    private BreathingLight breathingLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                doBreathingLight();
            }
        };
        timer = new Timer();

        onClickFlag = false;

        isWhiteCheck = whiteCheckBox.isChecked();
        isRedCheck = redCheckBox.isChecked();
        isGreenCheck = greenCheckBox.isChecked();

        smdt = SmdtManager.create(this);
        pwm = new Pwm(this, DEF_PERIOD, 1.0f);

    }

    @OnClick(R.id.testButton1)
    void onTestButton() {
        if (false == onClickFlag) {  //原来是关闭
            testButton.setText("已开启");
//                    timer.schedule(timerTask, 3000, 1 * 100);
//            smdt.smdtSetGpioDirection(4, 1, 1);  //打开LED 绿色
            checkBoxSetClickable(false);
            if (0.9000001f <= 1.0f) {
                Log.i(TAG, "0.9000001f <= 1.0f");
            } else {
                Log.i(TAG, "0.9000001f > 1.0f");
            }
        } else {
            testButton.setText("已关闭");
            timer.cancel();
            smdt.smdtSetGpioDirection(4, 1, 0);  //关闭LED 绿色
//            pwm.stop();
            checkBoxSetClickable(true);  //不允许改变
        }

        onClickFlag = !onClickFlag;
    }

    @OnClick(R.id.closeButton)
    void onCloseButton() {
        if (closeButton.getText().toString().equals(getString(R.string.closed_name))) {  //开启
            closeButton.setText(getString(R.string.opened_name));
            int times = 20;
            String timesText = timesEditText.getText().toString();
            try {
                times = Integer.parseInt(timesText);
                if (0 == times) {
                    times = 20;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception");
                e.printStackTrace();
            }
            breathingLight = new BreathingLight(times, 0.1f, 1.0f, this);
            breathingLight.start();
        } else {  //关闭
            closeButton.setText(getString(R.string.closed_name));
            if (null != breathingLight) {
                breathingLight.stop();
                breathingLight = null;
            }
        }
    }

    @OnClick(R.id.tenPercentButton)
    void onTenPercentButtong() {
        buttonProc(tenPercentButton, 0.1f, R.string.ten_percent_opened_name, R.string.ten_percent_closed_name);
    }

    @OnClick(R.id.twentyPercentButton)
    void onTwentyPercentButton() {
        buttonProc(twentyPercentButton, 0.2f, R.string.twenty_percent_opened_name, R.string.twenty_percent_closed_name);
    }

    @OnClick(R.id.thrityPercentButton)
    void onThrityPercentButton() {
        buttonProc(thrityPercentButton, 0.3f, R.string.thrity_percent_opened_name, R.string.thrity_percent_closed_name);
    }

    @OnClick(R.id.fortyPercentButton)
    void onFortyPercentButton() {
        buttonProc(fortyPercentButton, 0.4f, R.string.forty_percent_opened_name, R.string.forty_percent_closed_name);
    }

    @OnClick(R.id.fiftyPercentButton)
    void onFiftyPercentButton() {
        buttonProc(fiftyPercentButton, 0.5f, R.string.fifty_percent_opened_name, R.string.fifty_percent_closed_name);
    }

    @OnClick(R.id.sixtyPercentButton)
    void onSixtyPercentButton() {
        buttonProc(sixtyPercentButton, 0.6f, R.string.sixty_percent_opened_name, R.string.sixty_percent_closed_name);
    }

    @OnClick(R.id.seventyPercentButton)
    void onSeventyPercentButton() {
        buttonProc(seventyPercentButton, 0.7f, R.string.seventy_percent_opened_name, R.string.seventy_percent_closed_name);
    }

    @OnClick(R.id.eightyPercentButton)
    void onEightyPercentButton() {
        buttonProc(eightyPercentButton, 0.8f, R.string.eighty_percent_opened_name, R.string.eighty_percent_closed_name);
    }

    @OnClick(R.id.ninetyPercentButton)
    void onNinetyPercentButton() {
        buttonProc(ninetyPercentButton, 0.8f, R.string.ninety_percent_opened_name, R.string.ninety_percent_closed_name);
    }

    @OnClick(R.id.hundredPercentButton)
    void onHundredPercentButton() {
        buttonProc(hundredPercentButton, 1.0f, R.string.hundred_percent_opened_name, R.string.hundred_percent_closed_name);
    }

    @OnCheckedChanged(R.id.whiteCheckBox)
    void onWhiteCheckBox(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isWhiteCheck = true;
        } else {
            isWhiteCheck = false;
        }
    }

    @OnCheckedChanged(R.id.redCheckBox)
    void onRedCheckBox(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isRedCheck = true;
        } else {
            isRedCheck = false;
        }
    }

    @OnCheckedChanged(R.id.greenCheckBox)
    void onGreenCheckBox(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isGreenCheck = true;
        } else {
            isGreenCheck = false;
        }
    }

    private void doBreathingLight() {
        Log.i(TAG, "doBreathingLight");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        smdt.smdtSetGpioDirection(4, 1, 0);  //关闭LED 绿色
    }

    @Override
    public void on() {
        Log.i(TAG, "on......");
        if (isWhiteCheck) {
            smdt.setControl(3,  1);  //打开LED  白色
        }
        if (isRedCheck) {
            smdt.smdtSetUsbPower(1, 3, 1);  //打开LED 红色
        }
        if (isGreenCheck) {
            smdt.smdtSetGpioDirection(4, 1, 1);  //打开LED 绿色
            Log.i(TAG, "on green led......");
        }
    }

    @Override
    public void off() {
        Log.i(TAG, "off.....");
        if (isWhiteCheck) {
            smdt.setControl(3,  0);  //关闭LED 白色
        }
        if (isRedCheck) {
            smdt.smdtSetUsbPower(1, 3, 0);  //关闭LED 红色
        }
        if (isGreenCheck) {
            smdt.smdtSetGpioDirection(4, 1, 0);  //关闭LED 绿色
            Log.i(TAG, "off green led......");
        }
    }

    private void checkBoxSetClickable(boolean clickable) {
        whiteCheckBox.setClickable(clickable);
        redCheckBox.setClickable(clickable);
        greenCheckBox.setClickable(clickable);
    }

    void buttonProc(Button button, float duty, int stringIdOpened, int stringIdClosed) {
        if (button.getText().toString().equals(getString(stringIdClosed))) {  //开启
            button.setText(getString(stringIdOpened));
            if (null != pwm) {
                pwm.stop();
            }
            pwm = new Pwm(this, DEF_PERIOD, duty);
            pwm.start();
        } else {  //关闭
            button.setText(getString(stringIdClosed));
            if (null != pwm) {
                pwm.stop();
                pwm = null;
            }
        }
    }
}
