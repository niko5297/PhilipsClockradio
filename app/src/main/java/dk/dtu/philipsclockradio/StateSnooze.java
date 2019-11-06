package dk.dtu.philipsclockradio;


import android.os.Handler;

public class StateSnooze extends StateAdapter {

    private static Handler handler = new Handler();
    private ContextClockradio mContext;
    private boolean isSnoozeOver = false;
    private static boolean snoozeBefore = false;
    private static int alarmIndicator;

    //TODO: Få klokken til at opdatere hvert minut.

    //TODO: Lav kommentarer

    Runnable snoozeRun = new Runnable() {
        @Override
        public void run() {
            try {
                if (alarmIndicator == 1 && isSnoozeOver){
                    alarmIndicator = 3;
                    isSnoozeOver = false;
                    mContext.setState(new StateRadio());
                }

                if (alarmIndicator == 2 && isSnoozeOver) {
                    alarmIndicator = 3;
                    isSnoozeOver = false;
                    mContext.ui.turnOnTextBlink();
                    mContext.setState(new StateStandby(mContext.getTime()));

                }

            } finally {
                isSnoozeOver = true;
                handler.postDelayed(snoozeRun, 54000);
            }
        }
    };

    StateSnooze() {
    }


    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        alarmIndicator = StateStandby.getAlarmIndicator();
        snoozeRun.run();
    }

    @Override
    public void onExitState(ContextClockradio context) {
        snoozeBefore = true;
    }

    public static boolean getSnoozeOver(){
        return snoozeBefore;
    }

}
