package com.dommyg.acvideopoker;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private boolean[] holds;

    private boolean isNewHand;

    private Machine jacksOrBetter;

    private Deck deck;

    private GameScreenFragment gameScreenFragment;
    private Resources resources;

    GameLogic(GameScreenFragment gameScreenFragment, Resources resources) {
        this.holds = new boolean[5];
        this.isNewHand = true;
        this.jacksOrBetter = new Machine();
        this.deck = jacksOrBetter.getDeck();
        this.gameScreenFragment = gameScreenFragment;
        this.resources = resources;
    }

    /**
     * Runs all the methods involved with gameplay based upon if the player pressed the buttonDeal
     * at the start of the game or mid game.
     */
    void run() {
        if (isNewHand) {
            removeHolds();
            resetWinText();

            jacksOrBetter.processWager();
            deck.deal();
            deck.determineHandStatus();

            setCardImages();
            setCreditText();
            setResultText();

            isNewHand = false;
            handleToggles();
        } else {
            deck.hold(holds);
            deck.deal();
            deck.determineHandStatus();
            jacksOrBetter.determinePayout();

            setCardImages();
            setCreditText();
            setResultText();
            setWinText();

            deck.resetDeck();
            deck.resetHandDisplay();

            isNewHand = true;
            handleToggles();
        }
    }

    void initializeGameElements() {
        resetCardImages();
        removeHolds();
        toggleHoldButtons();
        setDenominationImage(DENOM_25);
        setCreditText();
        setBetText();
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
     * isNewHand value so that the player may or may not interact with the cards to hold them.
     */
    private void toggleHoldButtons() {
        if (isNewHand) {
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
        AssetManager assetManager = gameScreenFragment.getContext().getAssets();
        InputStream inputStream;
        for (int i = 0; i < deck.HAND_SIZE; i++) {
            String value = resources.getString(deck.getHandDisplay()[i].getValue().getStringValue());
            String suit = resources.getString(deck.getHandDisplay()[i].getSuit().getStringValue());
            String path = "card_faces/" + value.toLowerCase() + "_" + suit.toLowerCase() + ".png";
            try {
                inputStream = assetManager.open(path);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                gameScreenFragment.getCards()[i].setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            for (ImageView image : gameScreenFragment.getCards()) {
                image.setImageBitmap(bitmap);
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
     * variable so the player may or may not interact with the denomination button to change the bet
     * denomination.
     */
    private void toggleDenominationButton() {
        if (isNewHand) {
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

    /**
     * Sets the gameScreenFragment's textViewResult based upon the deck's handStatus.
     */
    private void setResultText() {
        TextView resultTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_RESULT];
        resultTextView.setText(deck.getHandStatus().getStringValue());
        if (deck.getHandStatus().equals(Deck.Result.NOTHING)) {
            resultTextView.setVisibility(View.INVISIBLE);
        } else {
            resultTextView.setVisibility(View.VISIBLE);
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
        if (isNewHand) {
            betButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
        }
    }

    /**
     * Sets visibility of the gameScreenFragment's textViewGameOver based upon the isNewHand
     * variable.
     */
    private void toggleGameOver() {
        TextView gameOverTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_GAME_OVER];
        if (isNewHand) {
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
        if (jacksOrBetter.getWinAmount().equals(
                BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN))) {
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
     * Enables or disables important gameplay features depending upon isNewHand value.
     */
    private void handleToggles() {
        toggleDenominationButton();
        toggleBetButton();
        toggleHoldButtons();
        toggleGameOver();
    }
}
