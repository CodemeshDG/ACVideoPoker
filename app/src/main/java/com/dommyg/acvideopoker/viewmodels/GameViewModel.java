package com.dommyg.acvideopoker.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dommyg.acvideopoker.models.Machine;
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