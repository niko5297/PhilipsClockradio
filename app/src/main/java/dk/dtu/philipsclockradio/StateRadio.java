package dk.dtu.philipsclockradio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;

public class StateRadio extends StateAdapter {

    //TODO: Forbedre kode

    /**
     * http://tunenet.dk/radio-tv/radiokanaler?start=1
     */

    private int radioType = 1;
    private double nuværendeFMFrekvens = 90.1;
    private double nuværendeAMFrekvens = 0;
    private int stationsNummer = 1;
    private boolean radiokanal = false;
    private double[] gemteKanaler = {25.1, 97.5, 85.2, 90.5, 101.8, 50.2, 60.6, 79.2, 30.2, 75.2, 65.3, 44.3, 99.3, 76.5, 88.8, 55.3, 43.6, 78.3, 66.6, 44.1};
    private double[] radioKanaler = {101.5, 97.0, 103.2, 102.7, 99.4, 106.6, 90.1, 105, 100.6, 97.7, 107.2, 107.6, 96.1};
    private boolean isRadioPlaying = true;

    StateRadio() {
    }


    @Override
    public void onEnterState(ContextClockradio context) {
        Arrays.sort(radioKanaler);
        context.ui.toggleRadioPlaying();
        if (radioType == 1) {
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        } else {
            context.ui.setDisplayText(nuværendeFMFrekvens + "");
        }

    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.toggleRadioPlaying();
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        if (radioType == 1) {
            radioType = 4;
        } else {
            radioType = 1;
        }
    }

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
        if (radioType == 4) {

        }
    }

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
        if (radioType == 4) {

        }
    }

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
        if (radioType == 4) {

        }
    }

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
        if (radioType == 4) {

        }
    }

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
