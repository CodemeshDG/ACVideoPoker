package com.dommyg.acvideopoker;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

class GameLogic {

    private final int DENOM_25 = 1;
    private final int DENOM_50 = 2;
    private final int DENOM_100 = 3;

    private final int SPEED_1 = 145;
    private final int SPEED_2 = 60;
    private final int SPEED_3 = 25;

    private final int SPEED_1_TEXT = 1;
    private final int SPEED_2_TEXT = 2;
    private final int SPEED_3_TEXT = 3;

    private boolean[] holds;

    private boolean isNewHand;
    private boolean isInDeal;
    private int currentSpeed;

    private Machine jacksOrBetter;

    private Deck deck;

    private GameScreenFragment gameScreenFragment;
    private Resources resources;
    private Handler handlerCards;
    private Handler handlerCredits;

    GameLogic(GameScreenFragment gameScreenFragment, Resources resources) {
        this.holds = new boolean[5];
        this.isNewHand = true;
        this.isInDeal = false;
        this.currentSpeed = SPEED_1;
        this.jacksOrBetter = new Machine();
        this.deck = jacksOrBetter.getDeck();
        this.gameScreenFragment = gameScreenFragment;
        this.resources = resources;
        this.handlerCards = new Handler();
        this.handlerCredits = new Handler();
    }

    /**
     * Runs all the methods involved with gameplay based upon if the player pressed the buttonDeal
     * at the start of the game or mid game.
     */
    void run() {
        if (isNewHand) {
            isInDeal = true;
            handleToggles();

            removeHolds();
            resetWinText();

            jacksOrBetter.processWager();

            setCreditText();

            deck.deal();
            setCardImages();
        } else {
            isInDeal = true;
            handleToggles();

            deck.hold(holds);
            deck.deal();

            setCardImages();
        }
    }

    private void firstCycle() {
        deck.determineHandStatus();

        toggleResultTextStyle();
        setResultText();

        isNewHand = false;
        isInDeal = false;
        handleToggles();
    }

    private void finalCycle() {
        deck.determineHandStatus();
//        BigDecimal previousBankroll = jacksOrBetter.getBank().getBankroll();
        jacksOrBetter.determinePayout();
//        BigDecimal currentBankroll = jacksOrBetter.getBank().getBankroll();

        setCreditText();
//        animateCreditText(previousBankroll, currentBankroll);
        toggleResultTextStyle();
        setResultText();
        setWinText();

        deck.resetDeck();
        deck.resetHandDisplay();

        isNewHand = true;
        isInDeal = false;
        handleToggles();
    }

    void initializeGameElements() {
        resetCardImages();
        removeHolds();
        toggleHoldButtons();
        setDenominationImage(DENOM_25);
        setCreditText();
        setBetText();
        setSpeedButtonText(SPEED_1_TEXT);
    }

    /**
     * Enables or disables the gameScreenFragment's buttonDeal based upon the isInDeal value so the
     * player may or may not interact with the deal button to deal new cards.
     */
    private void toggleDealButton() {
        Button dealButton = gameScreenFragment.getButtons()
                [gameScreenFragment.ARRAY_BUTTON_DEAL];
        if (isInDeal) {
            dealButton.setEnabled(false);
        } else {
            dealButton.setEnabled(true);
        }
    }

    /**
     * Sets a hold for an index and updates the visibility of the appropriate hold textView.
     */
    void setHolds(int index) {
        if (!holds[index]) {
            holds[index] = true;
            gameScreenFragment.getTextViewHolds()[index].setVisibility(View.VISIBLE);
        } else {
            holds[index] = false;
            gameScreenFragment.getTextViewHolds()[index].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Sets all holds to false.
     */
    private void removeHolds() {
        for (int i = 0; i < deck.HAND_SIZE; i++) {
            holds[i] = false;
        }
        for (TextView hold : gameScreenFragment.getTextViewHolds()) {
            hold.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Enables or disables all indexes in gameScreenFragment's cards ImageView array based upon the
     * isNewHand or isInDeal values so that the player may or may not interact with the cards to
     * hold them.
     */
    private void toggleHoldButtons() {
        if (isNewHand || isInDeal) {
            for (ImageView card : gameScreenFragment.getCards()) {
                card.setEnabled(false);
            }
        } else {
            for (ImageView card : gameScreenFragment.getCards()) {
                card.setEnabled(true);
            }
        }
    }

    /**
     * Sets each index of the gameScreenFragment's cards ImageView array to the appropriate card
     * face image by using the deck's handDisplay array.
     */
    private void setCardImages() {
        resetCardImages();
        AssetManager assetManager = gameScreenFragment.getContext().getAssets();
        handlerCards.postDelayed(
                new CardImageRunnable(assetManager, 0), currentSpeed
        );
    }

    /**
     * Sets each index of the gameScreenFragment's cards ImageView array to an image of a card back.
     */
    private void resetCardImages() {
        AssetManager assetManager = gameScreenFragment.getContext().getAssets();
        InputStream inputStream;
        String path = "card_faces/back.png";
        try {
            inputStream = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            for (int i = 0; i < deck.HAND_SIZE; i++) {
                if (!holds[i]) {
                    gameScreenFragment.getCards()[i].setImageBitmap(bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the machine's betDenomination value to the next increment depending on its current
     * value.
     */
    void setDenominationText() {
        if (jacksOrBetter.getBetDenomination().equals(BigDecimal.valueOf(.25))) {
            processChangeDenomination(.50);
            setDenominationImage(DENOM_50);
        } else if (jacksOrBetter.getBetDenomination().equals(BigDecimal.valueOf(.50))) {
            processChangeDenomination(1.00);
            setDenominationImage(DENOM_100);
        } else {
            processChangeDenomination(.25);
            setDenominationImage(DENOM_25);
        }
    }

    private void setDenominationImage(int value) {
        ImageView denomButton = gameScreenFragment.getImageViewDenomination();
        AssetManager assetManager = gameScreenFragment.getContext().getAssets();
        InputStream inputStream;
        Bitmap bitmap;
        String path = "denom/";
        try {
            switch (value) {
                case DENOM_25:
                    inputStream = assetManager.open(path + "denom_25.png");
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    denomButton.setImageBitmap(bitmap);
                    break;
                case DENOM_50:
                    inputStream = assetManager.open(path + "denom_50.png");
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    denomButton.setImageBitmap(bitmap);
                    break;
                case DENOM_100:
                    inputStream = assetManager.open(path + "denom_100.png");
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    denomButton.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the machine's betDenomination value.
     */
    private void processChangeDenomination(double amount) {

        jacksOrBetter.setBetDenomination(BigDecimal.valueOf(amount));
    }

    /**
     * Enables or disables the gameScreenFragment's imageViewDenomination based upon the isNewHand
     * and isInDeal values so the player may or may not interact with the denomination button to
     * change the bet denomination.
     */
    private void toggleDenominationButton() {
        if (isNewHand && !isInDeal) {
            gameScreenFragment.getImageViewDenomination().setEnabled(true);
        } else {
            gameScreenFragment.getImageViewDenomination().setEnabled(false);
        }
    }

    /**
     * Sets the gameScreenFragment's textViewCredit based upon the bank's bankroll value.
     */
    private void setCreditText() {
        String creditText = resources.getString(R.string.credit) +
                jacksOrBetter.getBank().getBankroll();
        gameScreenFragment.getTextViewOperations()[gameScreenFragment.ARRAY_OPERATIONS_CREDIT]
                .setText(creditText);
    }

    private void animateCreditText(BigDecimal previousBankroll, BigDecimal currentBankroll) {
        TextView creditText = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_CREDIT];
        handlerCredits.post(
                new AnimateCreditsRunnable(previousBankroll, currentBankroll, creditText)
        );
    }

    /**
     * Sets the gameScreenFragment's textViewResult based upon the deck's handStatus.
     */
    private void setResultText() {
        TextView resultTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_RESULT];
        resultTextView.setText(deck.getHandStatus().getStringValue());
    }

    /**
     * Sets visibility of the gameScreenFragment's textViewResult based upon the isInDeal and deck's
     * handStatus values.
     */
    private void toggleResultText() {
        TextView resultTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_RESULT];
        if (deck.getHandStatus().equals(Deck.Result.NOTHING) || isInDeal) {
            resultTextView.setVisibility(View.INVISIBLE);
        } else {
            resultTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the color of the gameScreenFragment's textViewResult based upon the isNewHand value.
     */
    private void toggleResultTextStyle() {
        TextView resultTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_RESULT];
        if (isNewHand) {
            resultTextView.setTextColor(resources.getColor(R.color.colorWhiteFont));
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            resultTextView.setTextColor(resources.getColor(R.color.colorRedFont));
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
    }

    /**
     * Sets the gameScreenFragment's textViewBet based upon the machine's bet.
     */
    private void setBetText() {
        TextView betTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_BET];
        String betText = resources.getString(R.string.bet);
        betTextView.setText(betText + " " + jacksOrBetter.getBet());
    }

    /**
     * Updates the machine's bet value to the next increment depending on its current value.
     */
    void processChangeBet() {
        if (jacksOrBetter.getBet() < 5) {
            jacksOrBetter.setBet(jacksOrBetter.getBet() + 1);
        } else {
            jacksOrBetter.setBet(1);
        }
        setBetText();
    }

    /**
     * Enables or disables the gameScreenFragment's buttonBet based upon the isNewHand variable so
     * the player may or may not interact with the bet button to change the bet amount.
     */
    private void toggleBetButton() {
        Button betButton = gameScreenFragment.getButtons()[gameScreenFragment.ARRAY_BUTTON_BET];
        if (isNewHand && !isInDeal) {
            betButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
        }
    }

    /**
     * Sets the gameScreenFragment's textViewSpeed based upon the currentSpeed.
     */
    private void setSpeedButtonText(int currentSpeedText) {
        Button speedButton = gameScreenFragment.getButtons()
                [gameScreenFragment.ARRAY_BUTTON_SPEED];
        String speedText = resources.getString(R.string.button_speed);
        speedButton.setText(speedText + " (" + currentSpeedText + ")");
    }

    /**
     * Updates the currentSpeed to the next increment depending on its current value.
     */
    void processChangeSpeed() {
        switch (currentSpeed) {
            case SPEED_1:
                currentSpeed = SPEED_2;
                setSpeedButtonText(SPEED_2_TEXT);
                break;

            case SPEED_2:
                currentSpeed = SPEED_3;
                setSpeedButtonText(SPEED_3_TEXT);
                break;

            case SPEED_3:
                currentSpeed = SPEED_1;
                setSpeedButtonText(SPEED_1_TEXT);
                break;
        }
    }

    /**
     * Enables or disables the gameScreenFragment's buttonSpeed based upon the isNewHand and
     * isInDeal values so the player may or may not interact with the speed button to change the
     * speed of dealing cards.
     */
    private void toggleSpeedButton() {
        Button speedButton = gameScreenFragment.getButtons()
                [gameScreenFragment.ARRAY_BUTTON_SPEED];
        if (isNewHand && !isInDeal) {
            speedButton.setEnabled(true);
        } else {
            speedButton.setEnabled(false);
        }
    }

    /**
     * Sets visibility of the gameScreenFragment's textViewGameOver based upon the isNewHand and
     * isInDeal values.
     */
    private void toggleGameOver() {
        TextView gameOverTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_GAME_OVER];
        if (isNewHand && !isInDeal) {
            gameOverTextView.setVisibility(View.VISIBLE);
        } else {
            gameOverTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Sets the gameScreenFragment's textViewWin's String value and visibility based upon the
     * machine's winAmount value.
     */
    private void setWinText() {
        TextView winTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_WIN];
        if (jacksOrBetter.getWinAmount().doubleValue() <= 0) {
            // No win; do not show win textView.
            winTextView.setVisibility(View.INVISIBLE);
        } else {
            String winText = resources.getString(R.string.win) +
                    jacksOrBetter.getWinAmount().setScale(2, RoundingMode.HALF_EVEN);
            winTextView.setText(winText);
            winTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the machine's winAmount to 0 and runs setWinText() so that the gameScreenFragment's
     * textViewWin is set to invisible.
     */
    private void resetWinText() {
        jacksOrBetter.setWinAmount(
                BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_EVEN));
        setWinText();
    }

    /**
     * Enables or disables important gameplay features depending upon isNewHand and isInDeal values.
     */
    private void handleToggles() {
        toggleResultText();
        toggleDealButton();
        toggleDenominationButton();
        toggleBetButton();
        toggleHoldButtons();
        toggleSpeedButton();
        toggleGameOver();
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
                String value = resources.getString(deck.getHandDisplay()[index].getValue().getStringValue());
                String suit = resources.getString(deck.getHandDisplay()[index].getSuit().getStringValue());
                String path = "card_faces/" + value.toLowerCase() + "_" + suit.toLowerCase() + ".png";
                try {
                    inputStream = assetManager.open(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    gameScreenFragment.getCards()[index].setImageBitmap(bitmap);
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

    private class AnimateCreditsRunnable implements Runnable {
        BigDecimal startAmount;
        BigDecimal currentAmount;
        BigDecimal newAmount;
        TextView creditTextView;

        AnimateCreditsRunnable(BigDecimal startAmount, BigDecimal newAmount, TextView creditTextView) {
            this.startAmount = startAmount;
            this.currentAmount = startAmount;
            this.newAmount = newAmount;
            this.creditTextView = creditTextView;
        }

        @Override
        public void run() {
            currentAmount = currentAmount.add(BigDecimal.valueOf(.01)
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN));
            creditTextView.setText(resources.getString(R.string.credit) + currentAmount);
            if (!currentAmount.equals(newAmount)) {
                handlerCredits.postDelayed(this, 1);
            }
        }
    }
}