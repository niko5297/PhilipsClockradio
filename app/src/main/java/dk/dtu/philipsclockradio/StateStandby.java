package dk.dtu.philipsclockradio;

import android.os.Handler;
import java.util.Date;

public class StateStandby extends StateAdapter {

    private Date mTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;
    private static StateAlarm stateAlarmAL1 = new StateAlarm(1);
    private static StateAlarm stateAlarmAL2 = new StateAlarm(2);
    private static boolean isAlarm1;
    private static boolean isAlarm2;

    StateStandby(Date time){
        mTime = time;
    }

    //Opdaterer hvert 60. sekund med + 1 min til tiden
    Runnable mSetTime = new Runnable() {

        @Override
        public void run() {
            try {
                long currentTime = mTime.getTime();
                mTime.setTime(currentTime + 60000);
                mContext.setTime(mTime);
                if (isAlarm1 && stateAlarmAL1.getAlarmTime().toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.ui.turnOnTextBlink();
                }
            } finally {
                mHandler.postDelayed(mSetTime, 60000);
            }
        }
    };

    void startClock() {
        mSetTime.run();
        mContext.isClockRunning = true;
    }

    void stopClock() {
        mHandler.removeCallbacks(mSetTime);
        mContext.isClockRunning = false;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        //Lokal context oprettet for at Runnable kan få adgang
        mContext = context;

        context.updateDisplayTime();
        if(!context.isClockRunning){
            startClock();
        }
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        stopClock();
        context.setState(new StateSetTime());
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        stopClock();
        context.setState(new StateRadio());
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        stopClock();
        context.setState(new StateSleep());
    }

    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        isAlarm1=true;
        stopClock();
        context.setState(new StateAlarm(1));

    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        isAlarm2=true;
        stopClock();
        context.setState(new StateAlarm(2));
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        if (isAlarm1){
            context.ui.turnOffLED(2);
            isAlarm1=false;
        }
        else {
            context.ui.turnOnLED(2);
            isAlarm1=true;
        }
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        if (isAlarm2){
            context.ui.turnOffLED(5);
            isAlarm2=false;
        }
        else {
            context.ui.turnOnLED(5);
            isAlarm2=true;
        }
    }

    @Override
    public void onClick_Snooze(ContextClockradio context) {
        stopClock();
        context.setState(new StateSnooze());
    }
}
