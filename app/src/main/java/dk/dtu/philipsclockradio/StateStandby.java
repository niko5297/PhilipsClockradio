package dk.dtu.philipsclockradio;

import android.os.Handler;
import java.util.Date;

public class StateStandby extends StateAdapter {

    private Date mTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;
    //Doesnt matter if its 1 or 2
    private StateAlarm stateAlarm = new StateAlarm(1);
    private StateSnooze stateSnooze = new StateSnooze();
    private static int alarmIndicator = 0;
    private boolean snoozeBefore = false;
    private static Date[] alarmArray = new Date[2];
    private boolean alarmRinging = false;

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

                if (alarmIndicator==2 && alarmArray[0].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.ui.turnOnTextBlink();
                    alarmRinging = true;
                }
                if (alarmIndicator==2 && alarmArray[1].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.ui.turnOnTextBlink();
                    alarmRinging = true;
                }
                if (alarmIndicator == 1 && alarmArray[0].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.setState(new StateRadio());
                }
                if (alarmIndicator == 1 && alarmArray[1].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.setState(new StateRadio());
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            finally {
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
        alarmArray = stateAlarm.getAlarmTime();
        snoozeBefore = stateSnooze.getSnoozeOver();
        alarmIndicator = stateAlarm.alarmSet()+1;

        context.updateDisplayTime();
        if(!context.isClockRunning){
            startClock();
        }
    }

    @Override
    public void onExitState(ContextClockradio context) {
        snoozeBefore = false;
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        stopClock();
        context.setState(new StateSetTime());
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        context.setState(new StateRadio());
    }

    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        stopClock();
        context.setState(new StateAlarm(1));

    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        stopClock();
        context.setState(new StateAlarm(2));
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        snoozeBefore = false;
        if (alarmRinging){
            context.ui.turnOffLED(2);
            context.ui.turnOffTextBlink();
            alarmRinging = false;

        }
        else {
            alarmIndicator++;
            switch (alarmIndicator) {
                case 1: {
                    context.ui.turnOnLED(1);
                    break;
                }
                case 2: {
                    context.ui.turnOffLED(1);
                    context.ui.turnOnLED(2);
                    break;
                }
                case 3: {
                    context.ui.turnOffLED(2);
                    alarmIndicator = 0;
                    break;
                }
            }
        }
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        snoozeBefore = false;
        if (alarmRinging){
            context.ui.turnOffLED(5);
            context.ui.turnOffTextBlink();
            alarmRinging = false;
        }
        else {
            alarmIndicator++;
            switch (alarmIndicator) {
                case 1: {
                    context.ui.turnOnLED(4);
                    break;
                }
                case 2: {
                    context.ui.turnOffLED(4);
                    context.ui.turnOnLED(5);
                    break;
                }
                case 3: {
                    context.ui.turnOffLED(5);
                    alarmIndicator = 0;
                    break;
                }
            }
        }
    }

    @Override
    public void onClick_Snooze(ContextClockradio context) {
        try {

            if (alarmIndicator==2 && stateAlarm.getAlarmTime().toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))
                    || snoozeBefore) {
                context.ui.turnOffTextBlink();
                context.setState(new StateSnooze());
            }
        }catch (Exception e){
            context.setState(new StateStandby(context.getTime()));
            System.out.println("Alarm skal være sat for at kunne benytte snooze");
            e.printStackTrace();
        }
    }


    public static int getAlarmIndicator(){
        return alarmIndicator;
    }
}
