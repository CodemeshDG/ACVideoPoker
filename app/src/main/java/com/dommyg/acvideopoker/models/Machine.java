package com.dommyg.acvideopoker.models;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Handler;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dommyg.acvideopoker.BR;
import com.dommyg.acvideopoker.GameSounds;
import com.dommyg.acvideopoker.utils.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Contains all the elements for playing the game. Holds a deck and a bank.
 */
public class Machine extends BaseObservable {

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
    private String[] cardImagePaths = new String[5];

    private boolean isNewHand;
    private boolean isInDeal;
    private boolean isDisplayingGameOver;
    private int currentSpeed;

    private Handler handlerCards;
    private Handler handlerGameOver;

    private Application application;

    public Machine(Application application) {
        this.application = application;
        this.deck = new Deck();
        this.bank = new Bank();
        this.gameSounds = new GameSounds(application);
        this.handlerCards = new Handler();
        this.handlerGameOver = new Handler();
        this.betDenomination = BigDecimal.valueOf(.25);
        this.bet = 1;
        this.winAmount = BigDecimal.valueOf(0);
        this.holds = new boolean[5];
        this.isNewHand = true;
        this.isInDeal = false;
        this.isDisplayingGameOver = true;
        this.currentSpeed = Constants.SPEED_1;
    }

    /**
     * Runs all the methods involved with gameplay based upon if the player pressed the deal/draw
     * button at the start of the game or mid game.
     */
    public void run() {
        if (isNewHand) {
            handlerGameOver.removeCallbacksAndMessages(null);
            setIsInDeal(true);
//            handleToggles();

            removeHolds();
            resetWinAmount();

            processWager();

//            setCreditText();

            deck.deal();
            setCardImages();
        } else {
            setIsInDeal(true);
//            handleToggles();

            deck.hold(holds);
            deck.deal();

            setCardImages();
        }
    }

    private void firstCycle() {
        deck.determineHandStatus();

//        toggleResultTextStyle();
//        setResultText();

        setIsNewHand(false);
        setIsInDeal(false);
//        handleToggles();
    }

    private void finalCycle() {
        deck.determineHandStatus();
//        BigDecimal previousBankroll = jacksOrBetter.getBank().getBankroll();
        determinePayout();
//        BigDecimal currentBankroll = jacksOrBetter.getBank().getBankroll();

//        setCreditText();
//        animateCreditText(previousBankroll, currentBankroll);
//        toggleResultTextStyle();
//        setResultText();
//        setWinText();

        deck.resetDeck();
        deck.resetHandDisplay();

        setIsNewHand(true);
        setIsInDeal(false);
        handlerGameOver.post(new AnimateGameOverRunnable());
//        handleToggles();
    }

    public Deck getDeck() {
        return deck;
    }

    public Bank getBank() {
        return bank;
    }

    @Bindable
    public BigDecimal getBetDenomination() {
        return betDenomination;
    }

    /**
     * Updates the betDenomination value to the next increment depending on its current value.
     */
    public void changeDenomination() {
        if (betDenomination.equals(Constants.DENOM_25)) {
            betDenomination = Constants.DENOM_50;
        } else if (betDenomination.equals(Constants.DENOM_50)) {
            betDenomination = Constants.DENOM_100;
        } else {
            betDenomination = Constants.DENOM_25;
        }
        notifyPropertyChanged(BR.betDenomination);
    }

    @Bindable
    public int getBet() {
        return bet;
    }

    /**
     * Updates the bet value to the next increment depending on its current value.
     */
    public void changeBet() {
        if (bet < 5) {
            bet++;
        } else {
            bet = 1;
        }
        notifyPropertyChanged(BR.bet);
    }

    @Bindable
    public BigDecimal getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
        notifyPropertyChanged(BR.winAmount);
    }

    /**
     * Sets the winAmount to 0.
     */
    private void resetWinAmount() {
        setWinAmount(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Bindable
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * Updates the currentSpeed to the next increment depending on its current value.
     */
    public void changeSpeed() {
        switch (currentSpeed) {
            case Constants.SPEED_1:
                currentSpeed = Constants.SPEED_2;
                break;

            case Constants.SPEED_2:
                currentSpeed = Constants.SPEED_3;
                break;

            case Constants.SPEED_3:
                currentSpeed = Constants.SPEED_1;
                break;
        }
        notifyPropertyChanged(BR.currentSpeed);
    }

    @Bindable
    public boolean[] getHolds() {
        return holds;
    }

    public void setHolds(int index) {
        gameSounds.play(GameSounds.SOUND_DOOT);
        holds[index] = !holds[index];
        notifyPropertyChanged(BR.holds);
    }

    /**
     * Sets all holds to false.
     */
    public void removeHolds() {
        for (int i = 0; i < deck.HAND_SIZE; i++) {
            holds[i] = false;
        }
        notifyPropertyChanged(BR.holds);
    }

    @Bindable
    public String[] getCardImagePaths() {
        return cardImagePaths;
    }

    @Bindable
    public boolean getIsNewHand() {
        return isNewHand;
    }

    private void setIsNewHand(boolean isNewHand) {
        this.isNewHand = isNewHand;
        notifyPropertyChanged(BR.isNewHand);
    }

    @Bindable
    public boolean getIsInDeal() {
        return isInDeal;
    }

    private void setIsInDeal(boolean isInDeal) {
        this.isInDeal = isInDeal;
        notifyPropertyChanged(BR.isInDeal);
    }

    @Bindable
    public boolean getIsDisplayingGameOver() {
        return isDisplayingGameOver;
    }

    public void setIsDisplayingGameOver(boolean isDisplayingGameOver) {
        this.isDisplayingGameOver = isDisplayingGameOver;
        notifyPropertyChanged(BR.isDisplayingGameOver);
    }

    public GameSounds getGameSounds() {
        return gameSounds;
    }

    /**
     * Returns the path of a card's image in String format based upon a card's index position in the
     * {@link Deck}'s handDisplay.
     */
    public String createCardImagePath(int index) {
        String value = application.getResources().getString(
                deck.getHandDisplay()[index].getValue().getStringValue()
        );
        String suit = application.getResources().getString(
                deck.getHandDisplay()[index].getSuit().getStringValue()
        );
        return "card_faces/" + value.toLowerCase() + "_" + suit.toLowerCase() + ".png";
    }

    /**
     * Sets the image path of each index of the cardImagePaths array which is not marked as held by
     * holds array to be the back of a card.
     */
    private void resetCardImages() {
        for (int i = 0; i < deck.HAND_SIZE; i++) {
            if (!holds[i]) {
                cardImagePaths[i] = "card_faces/back.png";
            }
        }
        notifyPropertyChanged(BR.cardImagePaths);
    }

    /**
     * Sets each index of the cardImagePaths array to the appropriate card face image path by using
     * the {@link Deck}'s handDisplay array.
     */
    private void setCardImages() {
        resetCardImages();
        AssetManager assetManager = application.getAssets();
        handlerCards.postDelayed(
                new CardImagePathsRunnable(assetManager, 0), currentSpeed
        );
    }

    /**
     * Determines which amount to payout to the player based upon the {@link Deck}'s handStatus.
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
     * Updates and adds the winAmount to the {@link Bank}'s bankroll.
     */
    private void processPayout(int prize) {
        setWinAmount(calculatePayout(prize));
        bank.setBankroll(bank.getBankroll().add(winAmount));
    }

    /**
     * Removes the wager amount from the {@link Bank}'s bankroll.
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

    private class CardImagePathsRunnable implements Runnable {
        AssetManager assetManager;
        int index;

        CardImagePathsRunnable(AssetManager assetManager, int index) {
            this.assetManager = assetManager;
            this.index = index;
        }

        @Override
        public void run() {
            if (!holds[index]) {
                cardImagePaths[index] = createCardImagePath(index);
                notifyPropertyChanged(BR.cardImagePaths);
                gameSounds.play(GameSounds.SOUND_DOOT);
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

    private class AnimateGameOverRunnable implements Runnable {

        @Override
        public void run() {
            if (!isDisplayingGameOver) {
                setIsDisplayingGameOver(true);
                handlerGameOver.postDelayed(this, 2000);
            } else {
                setIsDisplayingGameOver(false);
                handlerGameOver.postDelayed(this, 500);
            }
        }
    }
}
