package dk.dtu.philipsclockradio;

import android.os.Handler;
import android.os.health.SystemHealthManager;

import java.util.Date;

public class StateAlarm extends StateAdapter {

    Date time;
    public int alarm;




    //TODO: Få alarm til at fungere mht. tiden. Den opdatere den nuværende tid, hvilket den ikke skal.

    /**
     *
     * @param alarm
     */

    StateAlarm(int alarm){
        this.alarm=alarm;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        if (alarm==1){
            context.ui.turnOnLED(2);
        }
        else
            context.ui.turnOnLED(5);
        //time = context.getTime();
    }

    @Override
    public void onExitState(ContextClockradio context) {
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        time.setTime(time.getTime() + 60000);
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        time.setTime(time.getTime() + 3600000);
        System.out.println(time);
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));

    }


}
