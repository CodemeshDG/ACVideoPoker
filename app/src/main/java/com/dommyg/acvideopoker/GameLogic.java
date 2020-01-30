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
        resetCardImages();
    }

    /**
     * Runs all the methods involved with gameplay based upon if the player pressed the buttonDeal
     * at the start of the game or mid game.
     */
    private void run() {
        if (isNewHand) {
            resetWinText();
            jacksOrBetter.processWager();
            deck.deal();
            deck.determineHandStatus();
            setCreditText();
            setResultText();
            isNewHand = false;
        } else {
            deck.hold(holds);
            deck.deal();
            deck.determineHandStatus();
            setCardImages();
            deck.resetDeck();
            deck.resetHandDisplay();
            jacksOrBetter.determinePayout();
            setWinText();
            setCreditText();
            removeHolds();
        }
    }

    /**
     * Sets a hold for an index and updates the visibility of the appropriate hold textView.
     */
    private void setHolds(int index) {
        if (holds[index] = false) {
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
        for (boolean hold : holds) {
            hold = false;
        }
        for (TextView hold : gameScreenFragment.getTextViewHolds()) {
            hold.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Enables or disables all indexes in gameScreenFragment's cards ImageView array based upon the
     * isNewHand value so that the player may or may not interact with the cards to hold them.
     */
    private void toggleHolds() {
        if (isNewHand) {
            for (ImageView card : gameScreenFragment.getCards()) {
                card.setEnabled(true);
            }
        } else {
            for (ImageView card : gameScreenFragment.getCards()) {
                card.setEnabled(false);
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
    private void setDenominationText() {
        if (jacksOrBetter.getBetDenomination().equals(BigDecimal.valueOf(.25))) {
            processChangeDenomination(.50);
        } else if (jacksOrBetter.getBetDenomination().equals(BigDecimal.valueOf(.50))) {
            processChangeDenomination(1.00);
        } else {
            processChangeDenomination(.25);
        }
    }

    /**
     * Updates the machine's betDenomination value.
     */
    private void processChangeDenomination(double amount) {
        jacksOrBetter.setBetDenomination(BigDecimal.valueOf(amount));
    }
    // TODO: Add setDenominationText() and processChangeDenomination()

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
        gameScreenFragment.getTextViewOperations()[gameScreenFragment.ARRAY_OPERATIONS_RESULT]
                .setText(deck.getHandStatus().getStringValue());
    }

    /**
     * Sets the gameScreenFragment's textViewBet and updates the machine's bet value to the next
     * increment depending on its current value.
     */
    private void setBetText() {
        TextView betTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_BET];
        switch (jacksOrBetter.getBet()) {
            case 1:
                processChangeBet(2);
                betTextView.setText("2");
                break;
            case 2:
                processChangeBet(3);
                betTextView.setText("3");
                break;
            case 3:
                processChangeBet(4);
                betTextView.setText("4");
                break;
            case 4:
                processChangeBet(5);
                betTextView.setText("5");
                break;
            case 5:
                processChangeBet(1);
                betTextView.setText("1");
                break;
        }
    }

    /**
     * Updates the machine's bet value.
     */
    private void processChangeBet(int amount) {
        jacksOrBetter.setBet(amount);
    }

    /**
     * Enables or disables the gameScreenFragment's buttonBet based upon the isNewHand variable.
     */
    private void toggleBetButton() {
        Button betButton = gameScreenFragment.getButtons()[gameScreenFragment.ARRAY_BUTTON_BET];
        if (isNewHand) {
            betButton.setEnabled(false);
        } else {
            betButton.setEnabled(true);
        }
    }

    /**
     * Sets the gameScreenFragment's textViewWin's String value and visibility based upon the
     * machine's winAmount value.
     */
    private void setWinText() {
        TextView winTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_WIN];
        if (jacksOrBetter.getWinAmount().equals(BigDecimal.valueOf(0))) {
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
        jacksOrBetter.setWinAmount(BigDecimal.valueOf(0));
        setWinText();
    }

    // TODO: Finish this toggle method.
    private void handleToggles()
}
