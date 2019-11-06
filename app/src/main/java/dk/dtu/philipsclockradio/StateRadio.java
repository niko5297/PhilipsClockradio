package dk.dtu.philipsclockradio;

import android.os.Handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;

public class StateRadio extends StateAdapter {
    /**
     * http://tunenet.dk/radio-tv/radiokanaler?start=1
     */

    private static int radioType = 1;
    private static double nuværendeFMFrekvens = 90.1;
    private static double nuværendeAMFrekvens = 530;
    private static int sleepCounter;
    private int stationsNummer = 1;
    private boolean radiokanal = false;
    private static double[] gemteKanaler = {25.1, 97.5, 85.2, 90.5, 101.8, 50.2, 60.6, 79.2, 30.2, 75.2, 65.3, 44.3, 99.3, 76.5, 88.8, 55.3, 43.6, 78.3, 66.6, 44.1};
    private double[] radioKanaler = {101.5, 97.0, 103.2, 102.7, 99.4, 106.6, 90.1, 105, 100.6, 97.7, 107.2, 107.6, 96.1};
    private boolean isRadioPlaying = true;
    private ContextClockradio mContext;
    private StateAlarm stateAlarm = new StateAlarm(1);
    private static int alarmIndicator, alarmIndicator2;
    private static boolean snoozeBefore = false;
    private static Handler mHandler = new Handler();
    private static Date[] alarmArray = new Date[2];
    private static boolean alarmRinging;

    StateRadio() {
    }

    /**
     * This runnable will run if sleep is active, to check if the sleep timer has been reached
     * If true, return to StateStandby
     */
    Runnable sleepCheck = new Runnable() {
        @Override
        public void run() {
            try {
                if (sleepCounter==StateSleep.getChosenSleepTime()){
                    mContext.setState(new StateStandby(mContext.getTime()));
                }
                sleepCounter++;


            }finally {
                mHandler.postDelayed(sleepCheck,60000);
            }

        }
    };

    /**
     * This runnable will run to check if an alarm has reached the current time
     * If true, the alarm will ring
     */
    Runnable alarmCheck = new Runnable() {
        @Override
        public void run() {
            try {

                if (alarmIndicator==2 && alarmArray[0].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.ui.turnOnTextBlink();
                    alarmRinging = true;
                }
                if (alarmIndicator2==2 && alarmArray[1].toString().substring(11, 16).equals(mContext.getTime().toString().substring(11, 16))){
                    mContext.ui.turnOnTextBlink();
                    alarmRinging = true;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            finally {
                mHandler.postDelayed(alarmCheck, 60000);
            }
        }
    };


    /**
     * Upon entering RadioState.
     * Starts by defining variable from other classes
     * Sorting the radiochannel array.
     * Check the radio type and displays that.
     * Runs sleep runnable if sleep is active
     * Runs alarm runnable to check if an alarm should ring
     * @param context
     */
    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        snoozeBefore = StateSnooze.getSnoozeOver();
        alarmIndicator = StateStandby.getAlarmIndicator()+1;
        alarmIndicator2 = StateStandby.getAlarmIndicator2()+1;
        alarmArray = stateAlarm.getAlarmTime();
        Arrays.sort(radioKanaler);
        context.ui.toggleRadioPlaying();
        if (radioType == 1) {
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        } else {
            context.ui.setDisplayText(nuværendeAMFrekvens + "");
        }
        if (StateSleep.getChosenSleepTime()!=0){
            sleepCheck.run();
        }
        alarmCheck.run();

    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.toggleRadioPlaying();
        mHandler.removeCallbacks(alarmCheck);
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));
    }

    /**
     * This method changes the radiotype from FM to AM and opposite
     * @param context
     */
    @Override
    public void onClick_Power(ContextClockradio context) {
        radioType++;
        if (radioType>2){
            radioType=1;
        }
        if (radioType == 1) {
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        } else {
            context.ui.setDisplayText(nuværendeAMFrekvens + "");
        }
    }

    /**
     * This method increments FM radio by 0.1 MHz and displays that
     * This method also increments AM radio by 10 KHz and displays that
     * @param context
     */
    @Override
    public void onClick_Min(ContextClockradio context) {
        if (radioType == 1) {
            BigDecimal tempBig = new BigDecimal(0.1);
            nuværendeFMFrekvens = nuværendeFMFrekvens + tempBig.doubleValue();
            nuværendeFMFrekvens = Double.parseDouble(format(nuværendeFMFrekvens));

            if (nuværendeFMFrekvens > 107.6) {
                nuværendeFMFrekvens = 90.1;
            }
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        }
        if (radioType == 2) {
            nuværendeAMFrekvens = nuværendeAMFrekvens + 10;
            if (nuværendeAMFrekvens > 1700) {
                nuværendeAMFrekvens = 530;
            }
            context.ui.setDisplayText(nuværendeAMFrekvens + "");
        }
    }

    /**
     * This method decrements FM radio by 0.1 MHz and displays that
     * This method also decrements AM radio by 10 KHz and displays that
     * @param context
     */
    @Override
    public void onClick_Hour(ContextClockradio context) {
        if (radioType == 1) {
            BigDecimal tempBig = new BigDecimal(0.1);
            nuværendeFMFrekvens = nuværendeFMFrekvens - tempBig.doubleValue();
            nuværendeFMFrekvens = Double.parseDouble(format(nuværendeFMFrekvens));

            if (nuværendeFMFrekvens < 90.1) {
                nuværendeFMFrekvens = 107.6;
            }
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        }
        if (radioType == 2) {
            nuværendeAMFrekvens = nuværendeAMFrekvens - 10;
            if (nuværendeAMFrekvens < 530) {
                nuværendeAMFrekvens = 1700;
            }
            context.ui.setDisplayText(nuværendeAMFrekvens + "");

        }
    }

    /**
     * This method jumps to the next radiochannel that is lower than your current frequency
     * Since there are no AM radiochannels that functionality haven't been implemented
     * @param context
     */
    @Override
    public void onLongClick_Hour(ContextClockradio context) {
        double distance = Math.abs(radioKanaler[0] - nuværendeFMFrekvens);
        int idx = 0;
        if (radioType == 1) {
            for (int c = 0; c < radioKanaler.length; c++) {
                double cdistance = Math.abs(radioKanaler[c] - nuværendeFMFrekvens);
                if (cdistance < distance) {
                    idx = c;
                    distance = cdistance;
                }
            }
            if (nuværendeFMFrekvens == radioKanaler[idx] || nuværendeFMFrekvens < radioKanaler[idx]) {
                if (idx - 1 < 0) {
                    nuværendeFMFrekvens = radioKanaler[12];
                } else
                    nuværendeFMFrekvens = radioKanaler[idx - 1];

            } else
                nuværendeFMFrekvens = radioKanaler[idx];

            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        }
        if (radioType == 2) {

        }
    }

    /**
     * This method jumps to the next radiochannel that is higher than your current frequency
     * Since there are no AM radiochannels that functionality haven't been implemented
     * @param context
     */
    @Override
    public void onLongClick_Min(ContextClockradio context) {
        double distance = Math.abs(radioKanaler[0] - nuværendeFMFrekvens);
        int idx = 0;
        if (radioType == 1) {
            for (int c = 0; c < radioKanaler.length; c++) {
                double cdistance = Math.abs(radioKanaler[c] - nuværendeFMFrekvens);
                if (cdistance < distance) {
                    idx = c;
                    distance = cdistance;
                }
            }
            if (nuværendeFMFrekvens == radioKanaler[idx] || nuværendeFMFrekvens > radioKanaler[idx]) {
                if (idx + 1 > radioKanaler.length - 1) {
                    nuværendeFMFrekvens = radioKanaler[0];
                } else
                    nuværendeFMFrekvens = radioKanaler[idx + 1];

            } else
                nuværendeFMFrekvens = radioKanaler[idx];

            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        }
        if (radioType == 2) {

        }
    }

    /**
     * This method lets you save a chosen radiochannel by holding present.
     * It displays the current position.
     * @param context
     */
    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        if (isRadioPlaying) {
            stationsNummer = 1;
        }
        isRadioPlaying = false;

        for (int i = 0; i < radioKanaler.length; i++) {
            if (nuværendeFMFrekvens == radioKanaler[i]) {
                radiokanal = true;
            } else
                radiokanal = false;

            if (radiokanal) {
                context.ui.setDisplayText(stationsNummer + "");
                context.ui.turnOnTextBlink();
                stationsNummer++;
                if (stationsNummer > 20) {
                    stationsNummer = 1;
                }
                break;
            }
        }
    }

    /**
     * This method saves the chosen radiochannel in that specific position
     * It then switches back where you left.
     * @param context
     */
    @Override
    public void onClick_Preset(ContextClockradio context) {


        if (isRadioPlaying && stationsNummer <= 20) {
            if (gemteKanaler[stationsNummer - 1] != 0) {
                nuværendeFMFrekvens = gemteKanaler[stationsNummer - 1];
            } else
                nuværendeFMFrekvens = 0;

            context.ui.setDisplayText(nuværendeFMFrekvens + "");

            stationsNummer++;
        } else stationsNummer = 1;


        if (radiokanal && isRadioPlaying == false) {
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
            context.ui.turnOffTextBlink();
            gemteKanaler[stationsNummer - 1] = nuværendeFMFrekvens;
            stationsNummer = 1;
            isRadioPlaying = true;
        }

    }

    /**
     * This method lets you snooze the radio, if an alarm is ringing or just have.
     * It switches state and then returns after 9 minutes.
     * @param context
     */
    @Override
    public void onClick_Snooze(ContextClockradio context) {
        try {
            if (StateStandby.isAlarmRinging() || snoozeBefore || alarmRinging) {
                context.ui.setDisplayText(context.getTime().toString().substring(11, 16));
                context.setState(new StateSnooze());
            }
        }catch (Exception e){
            System.out.println("Alarm skal være sat for at kunne benytte snooze");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        context.setState(new StateSleep());
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        context.ui.turnOffTextBlink();
        alarmRinging = false;
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        context.ui.turnOffTextBlink();
        alarmRinging = false;
    }

    /*
    --------------------------------SUPPORT METHODS------------------------------
     */

    private static String format(double value) {

        NumberFormat format = NumberFormat.getInstance();

        format.setMinimumFractionDigits(1);

        format.setMaximumFractionDigits(2);

        format.setRoundingMode(RoundingMode.HALF_EVEN);

        return format.format(value);

    }


}
