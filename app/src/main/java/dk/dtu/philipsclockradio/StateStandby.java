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
    private static int alarmIndicator = 0, alarmIndicator2 = 0;
    private boolean snoozeBefore = false;
    private static Date[] alarmArray = new Date[2];
    private static boolean alarmRinging = false;
    private static boolean alarm1, alarm2 = false;

    StateStandby(Date time){
        mTime = time;
    }


    /**
     * This runnable will update the timer every minute
     * Each minute it will check the alarm time matches the current time
     * If true the alarm either rings or changes state acording to which alarm type has been set
     */
    Runnable mSetTime = new Runnable() {

        @Override
        public void run() {
            try {
                long currentTime = mTime.getTime();
                mTime.setTime(currentTime + 60000);
                mContext.setTime(mTime);
                    if (alarmIndicator == 1 && alarmArray[0].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))) {
                        mContext.ui.turnOffLED(1);
                        alarmRinging = true;
                        mContext.setState(new StateRadio());
                    }
                    if (alarmIndicator == 2 && alarmArray[0].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))) {
                        mContext.ui.turnOnTextBlink();
                        alarmRinging = true;
                    }
                    if (alarmIndicator2 == 1 && alarmArray[1].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))) {
                        mContext.ui.turnOffLED(4);
                        alarmRinging = true;
                        mContext.setState(new StateRadio());
                    }
                    if (alarmIndicator2 == 2 && alarmArray[1].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))) {
                        mContext.ui.turnOnTextBlink();
                        alarmRinging = true;
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
        if (!alarm1 && alarmArray[0]!=null) {
            alarmIndicator = stateAlarm.alarmSet()+1;
        }
        if (!alarm2 && alarmArray[1]!=null) {
            alarmIndicator2 = stateAlarm.alarmSet()+1;
        }
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

    /**
     * This method changes the alarm type.
     * It also stops the alarm if its ringing.
     * @param context
     */
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
                    alarm1 = true;
                    break;
                }
                case 2: {
                    context.ui.turnOffLED(1);
                    context.ui.turnOnLED(2);
                    alarm1 = true;
                    break;
                }
                case 3: {
                    context.ui.turnOffLED(2);
                    alarm1 = false;
                    alarmIndicator = 0;
                    break;
                }
            }
        }
    }


    /**
     * This method changes the alarm type.
     * It also stops the alarm if its ringing.
     * @param context
     */
    @Override
    public void onClick_AL2(ContextClockradio context) {
        snoozeBefore = false;
        if (alarmRinging){
            context.ui.turnOffLED(5);
            context.ui.turnOffTextBlink();
            alarmRinging = false;
        }
        else {
            alarmIndicator2++;
            switch (alarmIndicator2) {
                case 1: {
                    context.ui.turnOnLED(4);
                    alarm2 = true;
                    break;
                }
                case 2: {
                    context.ui.turnOffLED(4);
                    context.ui.turnOnLED(5);
                    alarm2 = true;
                    break;
                }
                case 3: {
                    context.ui.turnOffLED(5);
                    alarm2 = false;
                    alarmIndicator2 = 0;
                    break;
                }
            }
        }
    }

    /**
     *
     * @param context
     */
    @Override
    public void onClick_Snooze(ContextClockradio context) {
        try {

            if (alarmRinging || snoozeBefore) {
                context.ui.turnOffTextBlink();
                context.setState(new StateSnooze());
            }
        }catch (Exception e){
            context.setState(new StateStandby(context.getTime()));
            System.out.println("Alarm skal være sat for at kunne benytte snooze");
            e.printStackTrace();
        }
    }



    /*
    ---------------------------------------PUBLIC METHODS-----------------------------------------------
     */


    public static int getAlarmIndicator(){
        return alarmIndicator;
    }
    public static int getAlarmIndicator2(){
        return alarmIndicator2;
    }

    public static boolean isAlarmRinging() {
        return alarmRinging;
    }
}
