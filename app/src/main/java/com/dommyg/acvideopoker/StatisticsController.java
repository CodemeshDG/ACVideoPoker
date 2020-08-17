package com.dommyg.acvideopoker;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;

public class StatisticsController {
    private static final String KEY_HANDS_PLAYED = "hands_played";
    private static final String KEY_MONEY_WAGERED = "money_wagered";
    private static final String KEY_MONEY_WON = "money_won";
    private static final String KEY_DEALT = "dealt";
    private static final String KEY_DRAWN = "drawn";
    static final int PROCESS_DEALT = 0;
    static final int PROCESS_DRAWN = 1;

    private SharedPreferences sharedPreferences;

    public StatisticsController(Context context) {
        this.sharedPreferences = context.getSharedPreferences("statistics", Context.MODE_PRIVATE);
    }

    int loadHandsPlayed() {
        return sharedPreferences.getInt(KEY_HANDS_PLAYED, 0);
    }

    /**
     * Increments count of hands played in SharedPreferences by 1.
     */
    void incrementHandsPlayed() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HANDS_PLAYED, (loadHandsPlayed() + 1));
        editor.apply();
    }

    BigDecimal loadMoneyWagered() {
        double amount = Double.parseDouble(sharedPreferences.getString(KEY_MONEY_WAGERED,
                "0"));
        return BigDecimal.valueOf(amount);
    }

    void saveMoneyWagered(BigDecimal amount) {
        BigDecimal newAmount = amount.add(loadMoneyWagered());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MONEY_WAGERED, newAmount.toString());
        editor.apply();
    }

    BigDecimal loadMoneyWon() {
        double amount = Double.parseDouble(sharedPreferences.getString(KEY_MONEY_WON, "0"));
        return BigDecimal.valueOf(amount);
    }

    void saveMoneyWon(BigDecimal amount) {
        BigDecimal newAmount = amount.add(loadMoneyWon());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MONEY_WON, newAmount.toString());
        editor.apply();
    }

    /**
     * Returns from SharedPreferences the number of hands played based upon specifications provided.
     * @param result The type of hand result to retrieve.
     * @param dealtOrDrawn Use 0 to return results which have been dealt to the player, and use 1 to
     *                     return results which occurred by drawing.
     */
    int loadHandCount(Deck.Result result, int dealtOrDrawn) {
        String hand = result.name().toLowerCase().replace(" ", "_");
        String type;
        if (dealtOrDrawn == PROCESS_DRAWN) {
            type = KEY_DRAWN;
        } else {
            type = KEY_DEALT;
        }
        return sharedPreferences.getInt(type + "_" + hand, 0);
    }

    /**
     * Increments by 1 in SharedPreferences the number of hands played based upon specifications
     * provided.
     * @param result The type of hand result to save.
     * @param dealtOrDrawn Use 0 to save results which have been dealt to the player, and use 1 to
     *                     save results which occurred by drawing.
     */
    void incrementHandCount(Deck.Result result, int dealtOrDrawn) {
        String hand = result.name().toLowerCase().replace(" ", "_");
        String type;
        if (dealtOrDrawn == PROCESS_DRAWN) {
            type = KEY_DRAWN;
        } else {
            type = KEY_DEALT;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(type + "_" + hand, (loadHandCount(result, dealtOrDrawn) + 1));
        editor.apply();
    }
}
