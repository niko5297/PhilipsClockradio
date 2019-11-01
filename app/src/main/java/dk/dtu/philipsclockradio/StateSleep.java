package dk.dtu.philipsclockradio;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class StateSleep extends StateAdapter {

    private static int[] sleepTime = {120,90,60,30,15};
    private static int counter;
    private ContextClockradio mContext;
    private static long currentTime;
    private static long differenceTime;
    private static Handler mHandler = new Handler();
    private boolean isSleepActive;

    //TODO: Forbedre kode


    StateSleep(){}


    Runnable mTime = new Runnable() {

        @Override
        public void run() {
            try {
                differenceTime=System.currentTimeMillis();
                //System.out.println(differenceTime);
                if (currentTime+5000<differenceTime && !isSleepActive){
                    //System.out.println("Du er nået hertil");
                    mContext.setState(new StateStandby(mContext.getTime()));
                    isSleepActive=true;
                }

            } finally {
                mHandler.postDelayed(mTime,1000);

            }
        }
    };



    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        isSleepActive=false;
        context.ui.turnOnLED(3);
        context.ui.setDisplayText(sleepTime[0]+"");
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

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        currentTime = System.currentTimeMillis();
        boolean slutning = false;
        if (counter>=sleepTime.length-1){
            counter=0;
            slutning=true;

        }
        else
            counter++;

        context.ui.setDisplayText(sleepTime[counter]+"");

        if (slutning){
            context.ui.turnOffLED(3);
            context.ui.setDisplayText("OFF");
        }

    }
}
