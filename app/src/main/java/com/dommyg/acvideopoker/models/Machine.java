package com.dommyg.acvideopoker.models;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dommyg.acvideopoker.BR;
import com.dommyg.acvideopoker.utils.GameSounds;
import com.dommyg.acvideopoker.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Contains all the elements for playing the game. Holds a deck and a bank.
 */
public class Machine extends BaseObservable {

    // These are possible hand outcomes with prize values.
    private static final int ROYAL_FLUSH_PRIZE = 800;
    private static final int STRAIGHT_FLUSH_PRIZE = 50;
    private static final int FOUR_OF_A_KIND_PRIZE = 25;
    private static final int FULL_HOUSE_PRIZE = 9;
    private static final int FLUSH_PRIZE = 6;
    private static final int STRAIGHT_PRIZE = 4;
    private static final int THREE_OF_A_KIND_PRIZE = 3;
    private static final int TWO_PAIR_PRIZE = 2;
    private static final int JACKS_OR_BETTER_PRIZE = 1;
    private static final int NOTHING_PRIZE = 0;

    private static final int BLANK_CARD_INDEX = -1;

    private Deck deck;
    private Bank bank;
    private BigDecimal betDenomination;
    private int bet;
    private BigDecimal winAmount;
    private BigDecimal winAmountDoubleUp;
    private int doubleUpSelection;

    private GameSounds gameSounds;
    private Statistics statistics;

    private boolean[] holds;
    private Bitmap[] cardImages;

    private boolean isNewHand;
    private boolean isInDeal;
    private boolean isInDoubleUp;
    private boolean isDisplayingGameOver;
    private boolean isBankrupt;
    private int currentSpeed;
    private int currentSpeedIterator;

    private Handler handlerCards;
    private Handler handlerGameOver;

    private Application application;

    public Machine(Application application) {
        this.application = application;
        this.deck = new Deck();
        this.bank = new Bank(application);
        this.gameSounds = new GameSounds(application);
        this.statistics = new Statistics(application);
        this.handlerCards = new Handler();
        this.handlerGameOver = new Handler();
        this.betDenomination = BigDecimal.valueOf(.25);
        this.bet = 1;
        this.winAmount = BigDecimal.valueOf(0);
        this.holds = new boolean[5];
        this.cardImages = new Bitmap[5];
        resetCardImages();
        this.isNewHand = true;
        this.isInDeal = false;
        this.isInDoubleUp = false;
        updateIsBankrupt();
        this.isDisplayingGameOver = true;
        this.currentSpeed = Constants.SPEED_1;
        this.currentSpeedIterator = Constants.SPEED_1_ITERATOR;
    }

    /**
     * Runs all the methods involved with gameplay based upon if the player pressed the deal/draw
     * button at the start of the game or mid game.
     */
    public void run() {
        if (isNewHand) {
            terminateGameOverAnimation();
            setIsInDeal(true);
            setIsInDoubleUp(false);
            removeHolds();
            resetWinAmount();
            processWager();
            statistics.incrementHandsPlayed();
        } else {
            setIsInDeal(true);
            deck.hold(holds);
        }
        deck.deal();
        processCardImages();
    }

    private void firstCycle() {
        deck.determineHandStatus();
        statistics.incrementHandCount(Statistics.KEY_DEALT, deck.getHandStatus());
        checkIfPlayDing();

        setIsNewHand(false);
        setIsInDeal(false);
    }

    private void finalCycle() {
        deck.determineHandStatus();
        statistics.incrementHandCount(Statistics.KEY_DRAWN, deck.getHandStatus());
        checkIfPlayDing();
        determinePayout();

        deck.resetDeck();
        deck.resetHandDisplay();

        setIsNewHand(true);
        setIsInDeal(false);
        updateIsBankrupt();
        handlerGameOver.post(new AnimateGameOverRunnable());
    }

    public void beginDoubleUp() {
        terminateGameOverAnimation();
        setIsInDeal(true);
        setIsInDoubleUp(true);

        removeHolds();
        resetCardImages();

        deck.deal();
        setHolds(0);
        getCardImage(0, 0);

        setWinAmountDoubleUp(getWinAmount());
        resetWinAmount();
        processWagerDoubleUp();

        deck.determineDoubleUpStatus();

        setIsNewHand(false);
        setIsInDeal(false);
        statistics.incrementDoubleUpPlays();
    }

    public void continueDoubleUp(int index) {
        setIsInDeal(true);
        setDoubleUpSelection(index);
        setHolds(index);
        getCardImage(index, index);
        processCardImages();
    }

    private void completeDoubleUp() {
        deck.determineDoubleUpStatus(getDoubleUpSelection());
        if (deck.getHandStatus() == Deck.Result.DOUBLE_UP_WIN) {
            statistics.incrementDoubleUpWins();
        }
        checkIfPlayDing();
        processPayoutDoubleUp();

        deck.resetDeck();
        deck.resetHandDisplay();

        setIsNewHand(true);
        setIsInDeal(false);
        updateIsBankrupt();
        handlerGameOver.post(new AnimateGameOverRunnable());
    }

    /**
     * Removes any callbacks and messages from handlerGameOver and sets isDisplayingGameOver to
     * false.
     */
    private void terminateGameOverAnimation() {
        handlerGameOver.removeCallbacksAndMessages(null);
        setIsDisplayingGameOver(false);
    }

    public Deck getDeck() {
        return deck;
    }

    public Bank getBank() {
        return bank;
    }

    public Statistics getStatistics() {
        return statistics;
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
        updateIsBankrupt();
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
        updateIsBankrupt();
    }

    @Bindable
    public BigDecimal getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount.setScale(2, RoundingMode.HALF_EVEN);
        notifyPropertyChanged(BR.winAmount);
    }

    /**
     * Sets the winAmount to 0.
     */
    private void resetWinAmount() {
        setWinAmount(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_EVEN));
    }

    private BigDecimal getWinAmountDoubleUp() {
        return winAmountDoubleUp;
    }

    private void setWinAmountDoubleUp(BigDecimal winAmountDoubleUp) {
        this.winAmountDoubleUp = winAmountDoubleUp;
    }

    private int getDoubleUpSelection() {
        return doubleUpSelection;
    }

    private void setDoubleUpSelection(int doubleUpSelection) {
        this.doubleUpSelection = doubleUpSelection;
    }

    /**
     * Updates the currentSpeed to the next increment depending on its current value.
     */
    public void changeSpeed() {
        switch (currentSpeed) {
            case Constants.SPEED_1:
                currentSpeedIterator = Constants.SPEED_2_ITERATOR;
                currentSpeed = Constants.SPEED_2;
                break;

            case Constants.SPEED_2:
                currentSpeedIterator = Constants.SPEED_3_ITERATOR;
                currentSpeed = Constants.SPEED_3;
                break;

            case Constants.SPEED_3:
                currentSpeedIterator = Constants.SPEED_1_ITERATOR;
                currentSpeed = Constants.SPEED_1;
                break;
        }
        notifyPropertyChanged(BR.currentSpeedIterator);
    }

    @Bindable
    public int getCurrentSpeedIterator() {
        return currentSpeedIterator;
    }

    @Bindable
    public boolean[] getHolds() {
        return holds;
    }

    public void setHolds(int index) {
        if (!isInDoubleUp) {
            gameSounds.play(GameSounds.SOUND_DOOT);
        }
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
    public Bitmap[] getCardImages() {
        return cardImages;
    }

    private void setCardImages(int index, Bitmap cardImage) {
        cardImages[index] = cardImage;
        notifyPropertyChanged(BR.cardImages);
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
    public boolean getIsInDoubleUp() {
        return isInDoubleUp;
    }

    public void setIsInDoubleUp(boolean isInDoubleUp) {
        this.isInDoubleUp = isInDoubleUp;
        notifyPropertyChanged(BR.isInDoubleUp);
    }

    @Bindable
    public boolean getIsDisplayingGameOver() {
        return isDisplayingGameOver;
    }

    public void setIsDisplayingGameOver(boolean isDisplayingGameOver) {
        this.isDisplayingGameOver = isDisplayingGameOver;
        notifyPropertyChanged(BR.isDisplayingGameOver);
    }

    @Bindable
    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    /**
     * Determines if the player has enough money in their {@link Bank} to make a wager given the
     * current bet and betDenomination values.
     */
    public void updateIsBankrupt() {
        isBankrupt = bank.getBankroll().compareTo(calculateWager()) < 0;
        notifyPropertyChanged(BR.isBankrupt);
    }

    public GameSounds getGameSounds() {
        return gameSounds;
    }

    /**
     * Plays ding sound if {@link Deck}'s handStatus is not set to NOTHING.
     */
    private void checkIfPlayDing() {
        if (!deck.getHandStatus().equals(Deck.Result.NOTHING) &&
                !deck.getHandStatus().equals(Deck.Result.DOUBLE_UP_LOSE)) {
            gameSounds.play(GameSounds.SOUND_BING);
        }
    }

    /**
     * Returns the path of a card's image in String format based upon a card's index position in the
     * {@link Deck}'s handDisplay.
     *
     * @param readIndex Index position of {@link Deck}'s handDisplay to retrieve. Pass
     *                  BLANK_CARD_INDEX (-1) to retrieve the back of a card.
     */
    public String getCardImagePath(int readIndex) {
        if (readIndex >= 0 && readIndex <= (deck.getHandDisplay().length - 1)) {
            String value = application.getResources().getString(
                    deck.getHandDisplay()[readIndex].getValue().getStringValue()
            );
            String suit = application.getResources().getString(
                    deck.getHandDisplay()[readIndex].getSuit().getStringValue()
            );
            gameSounds.play(GameSounds.SOUND_DOOT);
            return "card_faces/" + value.toLowerCase() + "_" + suit.toLowerCase() + ".png";
        } else {
            return "card_faces/back.png";
        }
    }


    /**
     * Retrieves the card image in {@link Bitmap} format based upon the index of the {@link Deck}'s
     * handDisplay array.
     *
     * @param readIndex  The index to read from the handDisplay when retrieving the card image path.
     *                   Should be same as writeIndex except when you want to set the card image path
     *                   as a card back. In that case, use BLANK_CARD_INDEX (-1).
     * @param writeIndex The index of the cardImages array into which to set the retrieved
     *                   {@link Bitmap}.
     */
    public void getCardImage(int readIndex, int writeIndex) {
        InputStream inputStream;
        try {
            inputStream = application.getAssets().open(getCardImagePath(readIndex));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            setCardImages(writeIndex, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the image path of each index of the cardImagePaths array which is not marked as held by
     * holds array to be the back of a card.
     */
    private void resetCardImages() {
        for (int i = 0; i < deck.HAND_SIZE; i++) {
            if (!holds[i]) {
                getCardImage(BLANK_CARD_INDEX, i);
            }
        }
    }

    /**
     * Sets each index of the cardImagePaths array to the appropriate card face image path by using
     * the {@link Deck}'s handDisplay array.
     */
    private void processCardImages() {
        if (!isInDoubleUp) {
            resetCardImages();
        }
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
     * Updates and adds the winAmount to the {@link Bank}'s bankroll. Used for normal game.
     */
    private void processPayout(int prize) {
        setWinAmount(calculatePayout(prize));
        bank.setBankroll(bank.getBankroll().add(winAmount));
        statistics.addToMoneyWon(winAmount);
    }

    /**
     * Updates and adds the winAmount to the {@link Bank}'s bankroll. Used for double up.
     */
    private void processPayoutDoubleUp() {
        if (deck.getHandStatus().equals(Deck.Result.DOUBLE_UP_WIN)) {
            setWinAmount(winAmountDoubleUp.multiply(BigDecimal.valueOf(2)));
            bank.setBankroll(bank.getBankroll().add(winAmount));
            statistics.addToMoneyWon(winAmount);
        } else {
            setWinAmount(BigDecimal.valueOf(0));
        }
    }

    /**
     * Removes the wager amount from the {@link Bank}'s bankroll.
     */
    public void processWager() {
        BigDecimal wager = calculateWager();
        bank.setBankroll(bank.getBankroll().subtract(wager));
        statistics.addToMoneyWagered(wager);
    }

    /**
     * Removes the wager amount for double up from the {@link Bank}'s bankroll.
     */
    public void processWagerDoubleUp() {
        bank.setBankroll(bank.getBankroll().subtract(getWinAmountDoubleUp()));
        statistics.addToMoneyWagered(getWinAmountDoubleUp());
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
                getCardImage(index, index);
//                gameSounds.play(GameSounds.SOUND_DOOT);
            }

            index++;

            if (index < deck.HAND_SIZE) {
                if (!holds[index]) {
                    handlerCards.postDelayed(this, currentSpeed);
                } else {
                    handlerCards.post(this);
                }
            } else if (isInDoubleUp) {
                completeDoubleUp();
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
