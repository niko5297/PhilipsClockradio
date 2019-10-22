package dk.dtu.philipsclockradio;

public class StateRadio extends StateAdapter {

    StateRadio(){}


    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.toggleRadioPlaying();
        context.ui.turnOnLED(1);

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
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
    }

    @Override
    public void onLongClick_Hour(ContextClockradio context) {
    }

    @Override
    public void onLongClick_Min(ContextClockradio context) {
    }
}
