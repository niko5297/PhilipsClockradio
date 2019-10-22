package dk.dtu.philipsclockradio;

import java.util.Arrays;

public class StateRadio extends StateAdapter {

    //TODO: implementer søgning af radiokanaler. Ved langt klik, skal man komme frem til den næste radiokanal
    /**
     * http://tunenet.dk/radio-tv/radiokanaler?start=1
     */

    public static int radioType = 1;
    public static double nuværendeFrekvens;
    private double[] radioKanaler = {101.5,97.0,103.2,102.7,99.4,106.6,90.1,105,100.6,97.7,107.2,107.6,96.1};

    StateRadio(){}


    @Override
    public void onEnterState(ContextClockradio context) {
        Arrays.sort(radioKanaler);
        context.ui.toggleRadioPlaying();
        if (radioType==1) {
            context.ui.turnOnLED(1);
            context.ui.setDisplayText(nuværendeFrekvens+"");
        }
        else
            context.ui.turnOnLED(4);

    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.toggleRadioPlaying();
        context.ui.turnOffLED(1);
        context.ui.turnOffLED(4);
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        if (radioType==1){
            context.ui.turnOffLED(1);
            context.ui.turnOnLED(4);
            radioType=4;
        }
        else {
            context.ui.turnOnLED(1);
            context.ui.turnOffLED(4);
        }
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        if (radioType==1){
            nuværendeFrekvens++;
            System.out.println(nuværendeFrekvens);
            context.ui.setDisplayText(nuværendeFrekvens+"");
        }
        if (radioType==4){

        }
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        if (radioType==1){
            nuværendeFrekvens--;
            System.out.println(nuværendeFrekvens);
            context.ui.setDisplayText(nuværendeFrekvens+"");
        }
        if (radioType==4){

        }
    }

    @Override
    public void onLongClick_Hour(ContextClockradio context) {
        double distance = Math.abs(radioKanaler[0] - nuværendeFrekvens);
        int idx = 0;
        for(int c = 0; c < radioKanaler.length; c++){
            double cdistance = Math.abs(radioKanaler[c] - nuværendeFrekvens);
            if(cdistance > distance){
                idx = c;
                distance = cdistance;
            }
        }
        double theNumber = radioKanaler[idx];
        System.out.println(theNumber);
        if (radioType==1){
            for (int i = 0; i<radioKanaler.length; i++);

        }
        if (radioType==4){

        }
    }

    /**
     * Husk at tage højde for antallet af elementer i arrayet.
     * @param context
     */
    @Override
    public void onLongClick_Min(ContextClockradio context) {
        double distance = Math.abs(radioKanaler[0] - nuværendeFrekvens);
        int idx = 0;
        double theNumber = radioKanaler[idx];
        System.out.println(theNumber);
        if (radioType==1){
            for(int c = 0; c < radioKanaler.length; c++){
                double cdistance = Math.abs(radioKanaler[c] - nuværendeFrekvens);
                if(cdistance < distance){
                    idx = c;
                    distance = cdistance;
                }
            }
            if (nuværendeFrekvens==radioKanaler[idx]){
                nuværendeFrekvens=radioKanaler[idx+1];
            }
            else
            nuværendeFrekvens = radioKanaler[idx];

            context.ui.setDisplayText(nuværendeFrekvens+"");
        }
        if (radioType==4){

        }
    }
}
