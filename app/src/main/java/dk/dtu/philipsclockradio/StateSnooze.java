package dk.dtu.philipsclockradio;


import android.os.Handler;

public class StateSnooze extends StateAdapter {

    private static Handler handler = new Handler();
    private ContextClockradio mContext;
    private boolean isSnoozeOver;


    Runnable snoozeRun = new Runnable() {
        @Override
        public void run() {
            try {
                if (isSnoozeOver){
                    isSnoozeOver=false;
                    mContext.setState(new StateStandby(mContext.getTime()));

                }

            }finally {
                isSnoozeOver = true;
                handler.postDelayed(snoozeRun, 540000);
            }
        }
    };

    StateSnooze(){}


    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        context.ui.turnOnLED(3);
        snoozeRun.run();
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffLED(3);

    }

    @Override
    public void onClick_Snooze(ContextClockradio context) {
        context.ui.turnOffLED(3);
        context.setState(new StateStandby(context.getTime()));
    }
}
