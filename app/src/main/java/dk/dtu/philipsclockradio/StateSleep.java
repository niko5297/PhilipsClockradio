package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateSleep extends StateAdapter {

    private int[] sleepTime = {120, 90, 60, 30, 15};
    private static int chosenSleepTime;
    private int counter;
    private ContextClockradio mContext;
    private long currentTime;
    private long differenceTime;
    private static Handler mHandler = new Handler();
    private boolean isSleepActive;

    StateSleep() {
    }

    /**
     * This runnable checks every second if you have been idling for 5 seconds
     * If true you'll return to radio.
     */
    Runnable mTime = new Runnable() {

        @Override
        public void run() {
            try {
                differenceTime = System.currentTimeMillis();
                if (currentTime + 5000 < differenceTime && !isSleepActive) {
                    mContext.setState(new StateRadio());
                    isSleepActive = true;
                }

            } finally {
                mHandler.postDelayed(mTime, 1000);

            }
        }
    };


    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        isSleepActive = false;
        context.ui.turnOnLED(3);
        context.ui.setDisplayText(sleepTime[0] + "");
        currentTime = System.currentTimeMillis();
        differenceTime = System.currentTimeMillis();
        //System.out.println(currentTime);
        mTime.run();
    }

    @Override
    public void onExitState(ContextClockradio context) {
        mHandler.removeCallbacks(mTime);
        //mHandler.sendMessage(message);
        //System.out.println(message);
    }

    /**
     * When you click on the sleep display current sleep timer
     * Click again and move one position in the array and display that
     * If end of array have been reached, turn off LED
     * Save the chosen sleeptimer to be used by the radio.
     * @param context
     */
    @Override
    public void onClick_Sleep(ContextClockradio context) {
        currentTime = System.currentTimeMillis();
        boolean slutning = false;
        if (counter >= sleepTime.length - 1) {
            counter = 0;
            slutning = true;

        } else
            counter++;
        chosenSleepTime = sleepTime[counter];
        context.ui.setDisplayText(sleepTime[counter] + "");

        if (slutning) {
            context.ui.turnOffLED(3);
            context.ui.setDisplayText("OFF");
        }

    }


    /*
    -----------------------------------PUBLIC METHODS----------------------------------
     */


    public static int getChosenSleepTime() {
        return chosenSleepTime;
    }
}
