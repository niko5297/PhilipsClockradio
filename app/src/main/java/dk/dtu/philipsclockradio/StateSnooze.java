package dk.dtu.philipsclockradio;


import android.os.Handler;

public class StateSnooze extends StateAdapter {

    private static Handler handler = new Handler();
    private ContextClockradio mContext;
    private StateStandby stateStandby;
    private StateAlarm stateAlarm;
    private boolean isSnoozeOver;
    private int alarmIndicator;

    //TODO: Alarmen skal ringe når den retuneres til StateStandby, så man kan benytte snooze igen.

    //TODO Find en ny måde hvorpå at du kan komme igennem runnable første gang uden at hoppe ind. Nu hvor du kan snooze igen, se statestandby


    Runnable snoozeRun = new Runnable() {
        @Override
        public void run() {
            try {
                if (alarmIndicator == 1 && isSnoozeOver){
                    isSnoozeOver = false;
                    mContext.setState(new StateRadio());
                }

                if (alarmIndicator == 2 && isSnoozeOver) {
                    isSnoozeOver = false;
                    mContext.ui.turnOnTextBlink();
                    mContext.setState(new StateStandby(mContext.getTime()));

                }

            } finally {
                isSnoozeOver = true;
                handler.postDelayed(snoozeRun, 1000);
            }
        }
    };

    StateSnooze() {
    }


    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        stateStandby = new StateStandby(context.getTime());
        alarmIndicator = stateStandby.getAlarmIndicator();
        snoozeRun.run();
    }

    @Override
    public void onExitState(ContextClockradio context) {
    }

    public boolean getSnoozeOver(){
        return isSnoozeOver;
    }

}
