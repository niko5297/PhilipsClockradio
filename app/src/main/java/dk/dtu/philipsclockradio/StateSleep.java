package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateSleep extends StateAdapter {

    public static int[] sleepTime = {120,90,60,30,15};
    public static int counter;
    private ContextClockradio mContext;
    public static long currentTime;
    public static long differenceTime;
    private static Handler mHandler = new Handler();

    StateSleep(){}


    Runnable mTime = new Runnable() {

        @Override
        public void run() {
            try {
                differenceTime=System.currentTimeMillis();
                System.out.println(differenceTime);
                if (currentTime+5000<differenceTime){
                    System.out.println("Du er nået hertil");
                }

            } finally {
                mHandler.postDelayed(mTime,1000);

            }
        }
    };



    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnLED(3);
        context.ui.setDisplayText(sleepTime[0]+"");
        currentTime = System.currentTimeMillis();
        differenceTime = System.currentTimeMillis();
        System.out.println(currentTime);
        mTime.run();
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffLED(3);
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
            context.setState(new StateStandby(context.getTime()));
        }

    }
}
