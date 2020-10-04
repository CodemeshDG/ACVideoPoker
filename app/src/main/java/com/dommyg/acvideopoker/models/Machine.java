package com.dommyg.acvideopoker.models;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import com.dommyg.acvideopoker.GameSounds;
import com.dommyg.acvideopoker.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * Contains all the elements for playing the game. Holds a deck and a bank.
 */
public class Machine {

    // These are possible hand outcomes with prize values.
    static final private int ROYAL_FLUSH_PRIZE = 800;
    static final private int STRAIGHT_FLUSH_PRIZE = 50;
    static final private int FOUR_OF_A_KIND_PRIZE = 25;
    static final private int FULL_HOUSE_PRIZE = 9;
    static final private int FLUSH_PRIZE = 6;
    static final private int STRAIGHT_PRIZE = 4;
    static final private int THREE_OF_A_KIND_PRIZE = 3;
    static final private int TWO_PAIR_PRIZE = 2;
    static final private int JACKS_OR_BETTER_PRIZE = 1;
    static final private int NOTHING_PRIZE = 0;

    private Deck deck;
    private Bank bank;
    private BigDecimal betDenomination;
    private int bet;
    private BigDecimal winAmount;

    private GameSounds gameSounds;

    private boolean[] holds;
    private Bitmap[] cardImages;

    private boolean isNewHand;
    private boolean isInDeal;
    private int currentSpeed;

    private Application application;

    public Machine(Application application) {
        this.application = application;
        this.deck = new Deck();
        this.bank = new Bank();
        this.betDenomination = BigDecimal.valueOf(.25);
        this.bet = 1;
        this.winAmount = BigDecimal.valueOf(0);
        this.gameSounds = new GameSounds(application);
        this.holds = new boolean[5];
        this.isNewHand = true;
        this.isInDeal = false;
        this.currentSpeed = Constants.SPEED_1;
    }

    public Deck getDeck() {
        return deck;
    }

    public Bank getBank() {
        return bank;
    }

    public BigDecimal getBetDenomination() {
        return betDenomination;
    }

    public void setBetDenomination(BigDecimal betDenomination) {
        this.betDenomination = betDenomination;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public BigDecimal getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setHolds(int index) {
        gameSounds.play(GameSounds.SOUND_DOOT);
        holds[index] = !holds[index];
    }

    /**
     * Sets all holds to false.
     */
    public void removeHolds() {
        for (int i = 0; i < deck.HAND_SIZE; i++) {
            holds[i] = false;
        }
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

    public GameSounds getGameSounds() {
        return gameSounds;
    }

    /**
     * Determines which amount to payout to the player based upon the deck's handStatus.
     */
    public void determinePayout() {
        switch (deck.getHandStatus()) {
            case ROYAL_FLUSH:
                processPayout(ROYAL_FLUSH_PRIZE);
                break;

            case STRAIGHT_FLUSH:
                processPayout(STRAIGHT_FLUSH_PRIZE);
                break;

            case FOUR_OF_A_KIND:
                processPayout(FOUR_OF_A_KIND_PRIZE);
                break;

            case FULL_HOUSE:
                processPayout(FULL_HOUSE_PRIZE);
                break;

            case FLUSH:
                processPayout(FLUSH_PRIZE);
                break;

            case STRAIGHT:
                processPayout(STRAIGHT_PRIZE);
                break;

            case THREE_OF_A_KIND:
                processPayout(THREE_OF_A_KIND_PRIZE);
                break;

            case TWO_PAIR:
                processPayout(TWO_PAIR_PRIZE);
                break;

            case JACKS_OR_BETTER:
                processPayout(JACKS_OR_BETTER_PRIZE);
                break;

            case NOTHING:
                processPayout(NOTHING_PRIZE);
                break;
        }
    }

    /**
     * Updates and adds the winAmount to the bankroll.
     */
    private void processPayout(int prize) {
        this.winAmount = calculatePayout(prize);
        bank.setBankroll(bank.getBankroll().add(winAmount));
    }

    /**
     * Removes the wager amount from the bankroll.
     */
    public void processWager() {
        bank.setBankroll(bank.getBankroll().subtract(calculateWager()));
    }

    /**
     * Calculates the payout based upon the betDenomination.
     */
    private BigDecimal calculatePayout(int prize) {
        return betDenomination.multiply(BigDecimal.valueOf(bet * prize));
    }

    /**
     * Calculates the wager based upon the betDenomination.
     */
    private BigDecimal calculateWager() {
        return betDenomination.multiply(BigDecimal.valueOf(bet));
    }

    private class CardImageRunnable implements Runnable {
        AssetManager assetManager;
        int index;

        CardImageRunnable(AssetManager assetManager, int index) {
            this.assetManager = assetManager;
            this.index = index;
        }

        @Override
        public void run() {
            if (!holds[index]) {
                InputStream inputStream;
                String value = application.getResources().getString(deck.getHandDisplay()[index].getValue().getStringValue());
                String suit = application.getResources().getString(deck.getHandDisplay()[index].getSuit().getStringValue());
                String path = "card_faces/" + value.toLowerCase() + "_" + suit.toLowerCase() + ".png";
                try {
                    inputStream = assetManager.open(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    gameScreenFragment.getCards()[index].setImageBitmap(bitmap);
                    gameSounds.play(gameSounds.SOUND_DOOT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            index++;

            if (index < deck.HAND_SIZE) {
                if (!holds[index]) {
                    handlerCards.postDelayed(this, currentSpeed);
                } else {
                    handlerCards.post(this);
                }
            } else if (isNewHand) {
                firstCycle();
            } else {
                finalCycle();
            }
        }
    }
}
