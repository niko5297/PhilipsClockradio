package dk.dtu.philipsclockradio;

import java.util.Calendar;
import java.util.Date;

public class StateAlarm extends StateAdapter {

    private Date time = new Date();
    private String mDisplayText;
    public static Date[] alarmArray = new Date[2];
    private static boolean alarm1IsSet, alarm2IsSet;
    private int alarm;

    //TODO: Forbedre kode

    //TODO: Lav kommentarer


    StateAlarm(int alarm) {
        this.alarm = alarm;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnTextBlink();
        time = resetTime(time);
        mDisplayText = time.toString().substring(11, 16);
        context.ui.setDisplayText(mDisplayText);
        if (alarm == 1) {
            context.ui.turnOffLED(1);
            context.ui.turnOnLED(2);

            try {
                if (alarmArray[0] != null) {
                    context.ui.setDisplayText(alarmArray[0].toString().substring(11, 16));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            context.ui.turnOffLED(4);
            context.ui.turnOnLED(5);

            try {

                if (alarmArray[1] != null) {
                    context.ui.setDisplayText(alarmArray[1].toString().substring(11, 16));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        try {
            if (alarm == 1) {
                if (alarm1IsSet) {
                    alarmArray[0].setTime(alarmArray[0].getTime() + 60000);
                    mDisplayText = alarmArray[0].toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                } else {
                    time.setTime(time.getTime() + 60000);
                    mDisplayText = time.toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                    alarmArray[0] = time;
                }
            } else {
                if (alarm2IsSet) {
                    alarmArray[1].setTime(alarmArray[1].getTime() + 60000);
                    mDisplayText = alarmArray[1].toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                } else {
                    time.setTime(time.getTime() + 60000);
                    mDisplayText = time.toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                    alarmArray[1] = time;
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        try {
            if (alarm == 1) {
                if (alarm1IsSet) {
                    alarmArray[0].setTime(alarmArray[0].getTime() + 3600000);
                    mDisplayText = alarmArray[0].toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                } else {
                    time.setTime(time.getTime() + 3600000);
                    mDisplayText = time.toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                    alarmArray[0] = time;
                }
            } else {
                if (alarm2IsSet) {
                    alarmArray[1].setTime(alarmArray[1].getTime() + 3600000);
                    mDisplayText = alarmArray[1].toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                } else {
                    time.setTime(time.getTime() + 3600000);
                    mDisplayText = time.toString().substring(11, 16);
                    context.ui.setDisplayText(mDisplayText);
                    alarmArray[1] = time;
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        //mContext.updateDisplayTime();
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        alarm1IsSet = true;
        context.setState(new StateStandby(context.getTime()));
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        alarm2IsSet = true;
        context.setState(new StateStandby(context.getTime()));

    }


    public Date[] getAlarmTime() {
        return alarmArray;
    }

    public int alarmSet(){
        if (alarm1IsSet || alarm2IsSet){
            return 1;
        }
        else
            return 0;
    }





    /*
    --------------------------------SUPPORT METHODS------------------------------
     */

    private Date resetTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }


}
