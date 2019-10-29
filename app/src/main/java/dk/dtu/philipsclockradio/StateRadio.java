package dk.dtu.philipsclockradio;

import java.util.Arrays;

public class StateRadio extends StateAdapter {

    //TODO: implementer søgning af radiokanaler. Ved langt klik, skal man komme frem til den næste radiokanal
    /**
     * http://tunenet.dk/radio-tv/radiokanaler?start=1
     */

    public static int radioType = 1;
    public static double nuværendeFrekvens = 90.1;
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
            double a = 1.0;
            double b = 0.10;
            double x = 9 * b;
            a = a - (x);

    /* We use Math.round() function to round the answer to
         closest long, then we multiply and divide by 1.0 to
         to set the decimal places to 1 place (this can be done
         according to the requirements.*/
            System.out.println(a);
            System.out.println("a = " + Math.round(a*1.0)/1.0);
            nuværendeFrekvens = nuværendeFrekvens+Math.round(a*1.0)/1.0;
            System.out.println(nuværendeFrekvens);
            context.ui.setDisplayText(nuværendeFrekvens+"");
        }
        if (radioType==4){

        }
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        if (radioType==1){
            double a = 1.0;
            double b = 0.10;
            double x = 9 * b;
            a = a - (x);

    /* We use Math.round() function to round the answer to
         closest long, then we multiply and divide by 1.0 to
         to set the decimal places to 1 place (this can be done
         according to the requirements.*/
            System.out.println("a = " + Math.round(a*1.0)/1.0);
            nuværendeFrekvens = nuværendeFrekvens-Math.round(a*1.0)/1.0;
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
        double theNumber = radioKanaler[idx];
        System.out.println(theNumber);
        if (radioType==1) {
            for (int c = 0; c < radioKanaler.length; c++) {
                double cdistance = Math.abs(radioKanaler[c] - nuværendeFrekvens);
                if (cdistance < distance) {
                    idx = c;
                    distance = cdistance;
                }
            }
            if (nuværendeFrekvens == radioKanaler[idx]) {
                if (idx - 1 <0) {
                    nuværendeFrekvens = radioKanaler[12];
                } else
                    nuværendeFrekvens = radioKanaler[idx - 1];

            } else
                nuværendeFrekvens = radioKanaler[idx];

            context.ui.setDisplayText(nuværendeFrekvens + "");
        }
        if (radioType==4){

        }
    }

    /**
     * Brug Math.round() til double problemet (lægge 0.1 til frekvensen)
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
                if (idx+1>radioKanaler.length-1){
                    nuværendeFrekvens=radioKanaler[0];
                }
                else
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
