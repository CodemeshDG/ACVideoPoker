package com.dommyg.acvideopoker.models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dommyg.acvideopoker.BR;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Tracks game statistics for the user's progress.
 */
public class Statistics extends BaseObservable {
    private static final String KEY_HANDS_PLAYED = "hands_played";
    private static final String KEY_MONEY_WAGERED = "money_wagered";
    private static final String KEY_MONEY_WON = "money_won";
    private static final String KEY_DOUBLE_UP_PLAYS = "double_up_plays";
    private static final String KEY_DOUBLE_UP_WINS = "double_up_wins";
    public static final String KEY_DEALT = "dealt";
    public static final String KEY_DRAWN = "drawn";

    private int[] dealt = new int[10];
    private int[] drawn = new int[10];
    private BigDecimal moneyWagered;
    private BigDecimal moneyWon;
    private long handsPlayed;
    private long doubleUpPlays;
    private long doubleUpWins;

    private final SharedPreferences sharedPreferences;

    public Statistics(Application application) {
        this.sharedPreferences =
                application.getSharedPreferences("machine", Context.MODE_PRIVATE);
        initializeArray(dealt, KEY_DEALT);
        initializeArray(drawn, KEY_DRAWN);
        loadHandsPlayed();
        loadDoubleUpPlays();
        loadDoubleUpWins();
        loadMoneyWagered();
        loadMoneyWon();
    }

    @Bindable
    public int[] getDealt() {
        return dealt;
    }

    public void incrementHandCount(String type, Deck.Result result) {
        if (type.equals(KEY_DEALT)) {
            dealt[result.getNumValue()]++;
        } else {
            drawn[result.getNumValue()]++;
        }
    }

    @Bindable
    public int[] getDrawn() {
        return drawn;
    }

    @Bindable
    public long getHandsPlayed() {
        return handsPlayed;
    }

    public void incrementHandsPlayed() {
        this.handsPlayed++;
        notifyPropertyChanged(BR.handsPlayed);
    }

    @Bindable
    public BigDecimal getMoneyWagered() {
        return moneyWagered;
    }

    public void addToMoneyWagered(BigDecimal moneyWagered) {
        this.moneyWagered =
                this.moneyWagered.add(moneyWagered).setScale(2, RoundingMode.HALF_EVEN);
        notifyPropertyChanged(BR.moneyWagered);
    }

    @Bindable
    public BigDecimal getMoneyWon() {
        return moneyWon;
    }

    public void addToMoneyWon(BigDecimal moneyWon) {
        this.moneyWon = this.moneyWon.add(moneyWon).setScale(2, RoundingMode.HALF_EVEN);
        notifyPropertyChanged(BR.moneyWon);
    }

    @Bindable
    public long getDoubleUpPlays() {
        return doubleUpPlays;
    }

    public void incrementDoubleUpPlays() {
        this.doubleUpPlays++;
        notifyPropertyChanged(BR.doubleUpPlays);
    }

    @Bindable
    public long getDoubleUpWins() {
        return doubleUpWins;
    }

    public void incrementDoubleUpWins() {
        this.doubleUpWins++;
        notifyPropertyChanged(BR.doubleUpWins);
    }

    private void initializeArray(int[] array, String type) {
        array[Deck.Result.NOTHING.getNumValue()] =
                loadHandCount(Deck.Result.NOTHING, type);
        array[Deck.Result.JACKS_OR_BETTER.getNumValue()] =
                loadHandCount(Deck.Result.JACKS_OR_BETTER, type);
        array[Deck.Result.TWO_PAIR.getNumValue()] =
                loadHandCount(Deck.Result.TWO_PAIR, type);
        array[Deck.Result.THREE_OF_A_KIND.getNumValue()] =
                loadHandCount(Deck.Result.THREE_OF_A_KIND, type);
        array[Deck.Result.STRAIGHT.getNumValue()] =
                loadHandCount(Deck.Result.STRAIGHT, type);
        array[Deck.Result.FLUSH.getNumValue()] =
                loadHandCount(Deck.Result.FLUSH, type);
        array[Deck.Result.FULL_HOUSE.getNumValue()] =
                loadHandCount(Deck.Result.FULL_HOUSE, type);
        array[Deck.Result.FOUR_OF_A_KIND.getNumValue()] =
                loadHandCount(Deck.Result.FOUR_OF_A_KIND, type);
        array[Deck.Result.STRAIGHT_FLUSH.getNumValue()] =
                loadHandCount(Deck.Result.STRAIGHT_FLUSH, type);
        array[Deck.Result.ROYAL_FLUSH.getNumValue()] =
                loadHandCount(Deck.Result.ROYAL_FLUSH, type);
    }

    /**
     * Returns from SharedPreferences the number of hands played based upon specifications provided.
     *
     * @param result The type of hand result to retrieve.
     * @param type   Specify whether to retrieve results of dealt or drawn cards.
     */
    private int loadHandCount(Deck.Result result, String type) {
        String hand = result.name().toLowerCase().replace(" ", "_");
        return sharedPreferences.getInt(type + "_" + hand, 0);
    }

    private void saveHandCount(SharedPreferences.Editor editor, Deck.Result result, int[] array,
                               String type) {
        String hand = result.name().toLowerCase().replace(" ", "_");
        editor.putInt(type + "_" + hand, array[result.getNumValue()]);
    }
    
    private void saveHands(SharedPreferences.Editor editor) {
        saveHandCount(editor, Deck.Result.NOTHING, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.JACKS_OR_BETTER, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.TWO_PAIR, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.THREE_OF_A_KIND, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.STRAIGHT, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.FLUSH, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.FULL_HOUSE, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.FOUR_OF_A_KIND, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.STRAIGHT_FLUSH, dealt, KEY_DEALT);
        saveHandCount(editor, Deck.Result.ROYAL_FLUSH, dealt, KEY_DEALT);

        saveHandCount(editor, Deck.Result.NOTHING, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.JACKS_OR_BETTER, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.TWO_PAIR, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.THREE_OF_A_KIND, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.STRAIGHT, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.FLUSH, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.FULL_HOUSE, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.FOUR_OF_A_KIND, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.STRAIGHT_FLUSH, drawn, KEY_DRAWN);
        saveHandCount(editor, Deck.Result.ROYAL_FLUSH, drawn, KEY_DRAWN);
    }

    private void loadHandsPlayed() {
        handsPlayed = sharedPreferences.getLong(KEY_HANDS_PLAYED, 0);
    }

    private void saveHandsPlayed(SharedPreferences.Editor editor) {
        editor.putLong(KEY_HANDS_PLAYED, handsPlayed);
    }

    private void loadMoneyWagered() {
        double amount = Double.parseDouble(sharedPreferences.getString(KEY_MONEY_WAGERED,
                "0"));
        moneyWagered = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN);
    }

    private void saveMoneyWagered(SharedPreferences.Editor editor) {
        editor.putString(KEY_MONEY_WAGERED, moneyWagered.toString());
    }

    private void loadMoneyWon() {
        double amount = Double.parseDouble(sharedPreferences.getString(KEY_MONEY_WON, "0"));
        moneyWon = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN);
    }

    private void saveMoneyWon(SharedPreferences.Editor editor) {
        editor.putString(KEY_MONEY_WON, moneyWon.toString());
    }

    private void loadDoubleUpPlays() {
        doubleUpPlays = sharedPreferences.getLong(KEY_DOUBLE_UP_PLAYS, 0);
    }

    private void saveDoubleUpPlays(SharedPreferences.Editor editor) {
        editor.putLong(KEY_DOUBLE_UP_PLAYS, doubleUpPlays);
    }

    private void loadDoubleUpWins() {
        doubleUpWins = sharedPreferences.getLong(KEY_DOUBLE_UP_WINS, 0);
    }

    private void saveDoubleUpWins(SharedPreferences.Editor editor) {
        editor.putLong(KEY_DOUBLE_UP_WINS, doubleUpWins);
    }

    public void resetStatistics() {
        sharedPreferences.edit().clear().commit();
    }

    public void saveStatistics(SharedPreferences.Editor editor) {
        saveHands(editor);
        saveHandsPlayed(editor);
        saveDoubleUpPlays(editor);
        saveDoubleUpWins(editor);
        saveMoneyWagered(editor);
        saveMoneyWon(editor);
    }
}
