package com.dommyg.acvideopoker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Constants {
    public static final BigDecimal DENOM_25 = BigDecimal.valueOf(.25)
            .setScale(2, RoundingMode.HALF_EVEN);
    public static final BigDecimal DENOM_50 = BigDecimal.valueOf(.50)
            .setScale(2, RoundingMode.HALF_EVEN);
    public static final BigDecimal DENOM_100 = BigDecimal.valueOf(1.00)
            .setScale(2, RoundingMode.HALF_EVEN);

    public static final int SPEED_1 = 145;
    public static final int SPEED_2 = 60;
    public static final int SPEED_3 = 25;

    public static final int SPEED_1_ITERATOR = 1;
    public static final int SPEED_2_ITERATOR = 2;
    public static final int SPEED_3_ITERATOR = 3;

    public static final int CARD_DESIGN_CLASSIC_INDEX = 0;
    public static final int CARD_DESIGN_LARGE_INDEX = 1;

    public static final String KEY_BANKROLL = "bankroll";
    public static final String KEY_CARD_DESIGN = "card_design";
    public static final String KEY_BET_DENOMINATION = "bet_denomination";
    public static final String KEY_BET = "bet";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_SPEED_ITERATOR = "speed_iterator";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_VOLUME_ITERATOR = "volume_iterator";
}
