package com.dommyg.acvideopoker.utils;

import android.app.Application;

import com.dommyg.acvideopoker.R;

import java.math.BigDecimal;

public class StringUtil {

    public static String createDenominationButtonText(BigDecimal value) {
        if (Constants.DENOM_25.equals(value)) {
            return "25¢";
        } else if (Constants.DENOM_50.equals(value)) {
            return "50¢";
        } else if (Constants.DENOM_100.equals(value)) {
            return "$1";
        } else {
            return "0¢";
        }
    }

    public static String createCreditText(BigDecimal value, Application application) {
        return getResourceString(R.string.credit, application).concat(String.valueOf(value));
    }

    public static String createBetButtonText(int value, Application application) {
        return getResourceString(R.string.bet, application).concat(" ")
                .concat(String.valueOf(value));
    }

    public static String createSpeedButtonText(int value, Application application) {
        return getResourceString(R.string.button_speed, application).concat(" (" + value + ")");
    }

    public static String createSoundButtonText(int value, Application application) {
        return getResourceString(R.string.button_sound, application).concat(" (" + value + ")");
    }

    public static String createWinText(BigDecimal value, Application application) {
        return getResourceString(R.string.win, application).concat(String.valueOf(value));
    }

    public static String createResultText(int value, Application application) {
        return getResourceString(value, application);
    }

    private static String getResourceString(int stringId, Application application) {
        return application.getResources().getString(stringId);
    }
}
