package com.dommyg.acvideopoker.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;

import com.dommyg.acvideopoker.models.Machine;
import com.dommyg.acvideopoker.utils.Constants;
import com.dommyg.acvideopoker.utils.GameSounds;

public class GameViewModel extends AndroidViewModel {
    private Machine jacksOrBetter;

    public GameViewModel(Application application) {
        super(application);
        this.jacksOrBetter = new Machine(application);
    }

    public Machine getJacksOrBetter() {
        return jacksOrBetter;
    }

    public void resetMachine() {
        jacksOrBetter.getStatistics().resetStatistics();
        jacksOrBetter.getBank().resetBankroll();
        this.jacksOrBetter = new Machine(getApplication());
    }

    /**
     * Updates the {@link Machine}'s betDenomination value.
     */
    public void processChangeDenomination() {
        jacksOrBetter.changeDenomination();
    }

    /**
     * Updates the {@link Machine}'s bet value.
     */
    public void processChangeBet() {
        jacksOrBetter.changeBet();
    }

    /**
     * Updates the {@link Machine}'s currentSpeed.
     */
    public void processChangeSpeed() {
        jacksOrBetter.changeSpeed();
    }

    /**
     * Updates {@link GameSounds}'s currentVolume to the next increment
     * depending on its current value.
     */
    public void processChangeVolume() {
        jacksOrBetter.getGameSounds().changeVolume();
    }

    /**
     * If the game is not in double up mode, sets the {@link Machine}'s hold at a specific index.
     * Otherwise, finishes the double up game logic with the selected card.
     */
    public void processCardSelection(int index) {
        if (jacksOrBetter.getIsInDoubleUp()) {
            jacksOrBetter.continueDoubleUp(index);
        } else {
            jacksOrBetter.setHolds(index);
        }
    }

    /**
     * Adds the amount to the {@link Machine}'s {@link com.dommyg.acvideopoker.models.Bank}.
     */
    public void addCashToMachine(double amount) {
        jacksOrBetter.getBank().addCash(amount);
    }

    /**
     * Runs the {@link Machine}'s main game logic.
     */
    public void dealOrDraw() {
        jacksOrBetter.run();
    }

    /**
     * Starts the {@link Machine}'s double up game logic.
     */
    public void doubleUp() {
        jacksOrBetter.beginDoubleUp();
    }

    @Override
    protected void onCleared() {
        jacksOrBetter.getBank().saveBankroll();
        jacksOrBetter.getStatistics().saveStatistics();
        super.onCleared();
    }
}