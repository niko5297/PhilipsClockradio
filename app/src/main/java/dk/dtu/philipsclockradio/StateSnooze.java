package dk.dtu.philipsclockradio;


import android.os.Handler;

public class StateSnooze extends StateAdapter {

    private static Handler handler = new Handler();
    private ContextClockradio mContext;
    private StateStandby stateStandby;
    private boolean isSnoozeOver;
    private int alarmIndicator;

    //TODO: Alarmen skal ringe når den retuneres til StateStandby, så man kan benytte snooze igen.


    Runnable snoozeRun = new Runnable() {
        @Override
        public void run() {
            try {
                if (alarmIndicator == 1 & isSnoozeOver){
                    isSnoozeOver = false;
                    mContext.setState(new StateRadio());
                }

                if (alarmIndicator == 2 && isSnoozeOver) {
                    isSnoozeOver = false;
                    mContext.setState(new StateStandby(mContext.getTime()));

                }

            } finally {
                isSnoozeOver = true;
                handler.postDelayed(snoozeRun, 540000);
            }
        }
    };

    StateSnooze() {
    }


    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        alarmIndicator = stateStandby.getAlarmIndicator();
        snoozeRun.run();
    }

    @Override
    public void onExitState(ContextClockradio context) {

        context.ui.turnOnTextBlink();
    }

}
