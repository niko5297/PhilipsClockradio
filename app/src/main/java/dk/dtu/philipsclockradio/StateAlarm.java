package dk.dtu.philipsclockradio;

import java.util.Date;

public class StateAlarm extends StateAdapter {

    Date mTime;
    public int alarm;


    //TODO: Få alarm til at fungere mht. tiden. Den opdatere den nuværende tid, hvilket den ikke skal.

    StateAlarm(int alarm){
        this.alarm=alarm;
    }


    @Override
    public void onEnterState(ContextClockradio context) {
        if (alarm==1){
            context.ui.turnOnLED(1);
        }
        else
            context.ui.turnOnLED(5);
        mTime = context.getTime();
    }

    @Override
    public void onExitState(ContextClockradio context) {
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        mTime.setTime(mTime.getTime() + 60000);
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        mTime.setTime(mTime.getTime() + 3600000);
        System.out.println(mTime);
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        System.out.println(context.getTime());
        context.setState(new StateStandby(context.getTime()));
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));

    }
}
