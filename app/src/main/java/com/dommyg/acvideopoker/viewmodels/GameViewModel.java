package com.dommyg.acvideopoker.viewmodels;

import android.app.Application;
import android.content.res.Resources;

import androidx.lifecycle.AndroidViewModel;

import com.dommyg.acvideopoker.GameSounds;
import com.dommyg.acvideopoker.models.Bank;
import com.dommyg.acvideopoker.models.Deck;
import com.dommyg.acvideopoker.models.Machine;
import com.dommyg.acvideopoker.utils.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GameViewModel extends AndroidViewModel {
    private boolean[] holds;

    private boolean isNewHand;
    private boolean isInDeal;
    private int currentSpeed;

    private Machine jacksOrBetter;
    private Deck deck;
    private GameSounds gameSounds;

    public GameViewModel(Application application) {
        super(application);
        this.gameSounds = new GameSounds(application.getBaseContext());
        this.holds = new boolean[5];
        this.isNewHand = true;
        this.isInDeal = false;
        this.currentSpeed = Constants.SPEED_1;
        this.jacksOrBetter = new Machine();
        this.deck = jacksOrBetter.getDeck();
    }

    public boolean[] getHolds() {
        return holds;
    }

    public boolean getIsNewHand() {
        return isNewHand;
    }

    public boolean getIsInDeal() {
        return isInDeal;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
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
        return gameSounds.getCurrentVolumeIterator();
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
     * Updates the machine's betDenomination value to the next increment depending on its current
     * value.
     */
    public void processChangeDenomination() {
        // TODO: Data binding
        if (jacksOrBetter.getBetDenomination().equals(Constants.DENOM_25)) {
            processChangeDenomination(Constants.DENOM_50);
//            setDenominationButtonText(DENOM_50);
        } else if (jacksOrBetter.getBetDenomination().equals(Constants.DENOM_50)) {
            processChangeDenomination(Constants.DENOM_100);
//            setDenominationButtonText(DENOM_100);
        } else {
            processChangeDenomination(Constants.DENOM_25);
//            setDenominationButtonText(DENOM_25);
        }
    }

    /**
     * Updates the machine's betDenomination value.
     */
    private void processChangeDenomination(BigDecimal amount) {
        jacksOrBetter.setBetDenomination(amount);
    }

    /**
     * Updates the machine's bet value to the next increment depending on its current value.
     */
    void processChangeBet() {
        // TODO: Data binding
        if (jacksOrBetter.getBet() < 5) {
            jacksOrBetter.setBet(jacksOrBetter.getBet() + 1);
        } else {
            jacksOrBetter.setBet(1);
        }
//        setBetText();
    }

    /**
     * Updates the currentSpeed to the next increment depending on its current value.
     */
    void processChangeSpeed() {
        // TODO: Data binding
        switch (currentSpeed) {
            case Constants.SPEED_1:
                currentSpeed = Constants.SPEED_2;
//                setSpeedButtonText(SPEED_2_TEXT);
                break;

            case Constants.SPEED_2:
                currentSpeed = Constants.SPEED_3;
//                setSpeedButtonText(SPEED_3_TEXT);
                break;

            case Constants.SPEED_3:
                currentSpeed = Constants.SPEED_1;
//                setSpeedButtonText(SPEED_1_TEXT);
                break;
        }
    }

    /**
     * Updates gameSounds's currentVolume to the next increment depending on its current value.
     */
    void processChangeVolume() {
        // TODO: Data binding
        gameSounds.changeVolume();
//        setSoundButtonText(gameSounds.changeVolume());
    }

    /**
     * Sets the machine's winAmount to 0 and runs setWinText() so that the gameScreenFragment's
     * textViewWin is set to invisible.
     */
    private void resetWinText() {
        // TODO: Data binding
        jacksOrBetter.setWinAmount(
                BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_EVEN));
//        setWinText();
    }
}