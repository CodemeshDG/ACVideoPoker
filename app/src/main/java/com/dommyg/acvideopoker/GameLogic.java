package com.dommyg.acvideopoker;

import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

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
        }
    }

    private void setHolds(int index) {
        if (holds[index] = false) {
            holds[index] = true;
            gameScreenFragment.getTextViewHolds()[index].setVisibility(View.VISIBLE);
        } else {
            holds[index] = false;
            gameScreenFragment.getTextViewHolds()[index].setVisibility(View.INVISIBLE);
        }
    }

    private void removeHolds() {
        for (boolean hold : holds) {
            hold = false;
        }
        for (TextView hold : gameScreenFragment.getTextViewHolds()) {
            hold.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleHolds() {
        if (isNewHand) {
            for (ImageView card: gameScreenFragment.getCards()) {
                card.setEnabled(true);
            }
        } else {
            for (ImageView card : gameScreenFragment.getCards()) {
                card.setEnabled(false);
            }
        }
    }

    // TODO: Add setDenominationText() and processChangeDenomination()

    private void setCreditText() {
        String creditText = resources.getString(R.string.credit) +
                jacksOrBetter.getBank().getBankroll();
        gameScreenFragment.getTextViewOperations()[gameScreenFragment.ARRAY_OPERATIONS_CREDIT]
                .setText(creditText);
    }

    private void setResultText() {
        gameScreenFragment.getTextViewOperations()[gameScreenFragment.ARRAY_OPERATIONS_RESULT]
                .setText(deck.getHandStatus().getStringValue());
    }

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

    private void processChangeBet(int amount) {
        jacksOrBetter.setBet(amount);
    }

    private void toggleBetButton() {
        Button betButton = gameScreenFragment.getButtons()[gameScreenFragment.ARRAY_BUTTON_BET];
        if (isNewHand) {
            betButton.setEnabled(false);
        } else {
            betButton.setEnabled(true);
        }
    }

    private void setWinText() {
        TextView winTextView = gameScreenFragment.getTextViewOperations()
                [gameScreenFragment.ARRAY_OPERATIONS_WIN];
        if (jacksOrBetter.getWinAmount().equals(BigDecimal.valueOf(0))) {
            winTextView.setVisibility(View.INVISIBLE);
        } else {
            String winText = resources.getString(R.string.win) +
                    jacksOrBetter.getWinAmount().setScale(2, RoundingMode.HALF_EVEN);
                    winTextView.setText(winText);
            winTextView.setVisibility(View.VISIBLE);
        }
    }

    private void resetWinText() {
        jacksOrBetter.setWinAmount(BigDecimal.valueOf(0));
        setWinText();
    }
}
