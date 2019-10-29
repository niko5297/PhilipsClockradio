package dk.dtu.philipsclockradio;

public class StateSleep extends StateAdapter {

    public static int[] sleepTime = {120,90,60,30,15};
    public static int counter;

    StateSleep(){}

    //TODO: HUSK IDLE... Efter 5 sekunder hop tilbage til standby

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnLED(3);
        context.ui.setDisplayText(sleepTime[0]+"");
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffLED(3);
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {

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
