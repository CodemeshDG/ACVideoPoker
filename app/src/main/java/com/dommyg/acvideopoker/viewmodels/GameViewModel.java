package com.dommyg.acvideopoker.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dommyg.acvideopoker.models.Bank;
import com.dommyg.acvideopoker.models.Deck;
import com.dommyg.acvideopoker.models.Machine;

import java.math.BigDecimal;

public class GameViewModel extends AndroidViewModel {
    private Machine jacksOrBetter;

    public GameViewModel(Application application) {
        super(application);
        this.jacksOrBetter = new Machine(application);
    }

    public boolean[] getHolds() {
        return jacksOrBetter.getHolds();
    }

    public String[] getCardImagePaths() {
        return jacksOrBetter.getCardImagePaths();
    }

    public boolean getIsNewHand() {
        return jacksOrBetter.getIsNewHand();
    }

    public boolean getIsInDeal() {
        return jacksOrBetter.getIsInDeal();
    }

    public boolean getIsDisplayingGameOver() {
        return jacksOrBetter.getIsDisplayingGameOver();
    }

    public int getCurrentSpeed() {
        return jacksOrBetter.getCurrentSpeed();
    }

    public BigDecimal getBetDenomination() {
        return jacksOrBetter.getBetDenomination();
    }

    public Bank getBank() {
        return jacksOrBetter.getBank();
    }

    public int getBet() {
        return jacksOrBetter.getBet();
    }

    public int getCurrentVolume() {
        return jacksOrBetter.getGameSounds().getCurrentVolumeIterator();
    }

    public BigDecimal getWinAmount() {
        return jacksOrBetter.getWinAmount();
    }

    public int getHandValue() {
        return jacksOrBetter.getDeck().getHandStatus().getStringValue();
    }

    public Deck.Result getHandStatus() {
        return jacksOrBetter.getDeck().getHandStatus();
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
     * Updates {@link com.dommyg.acvideopoker.GameSounds}'s currentVolume to the next increment
     * depending on its current value.
     */
    public void processChangeVolume() {
        jacksOrBetter.getGameSounds().changeVolume();
    }

    /**
     * Sets the {@link Machine}'s hold at a specific index.
     */
    public void processHold(int index) {
        jacksOrBetter.setHolds(index);
    }

    /**
     * Runs the {@link Machine}'s game logic.
     */
    public void dealOrDraw() {
        jacksOrBetter.run();
    }
}