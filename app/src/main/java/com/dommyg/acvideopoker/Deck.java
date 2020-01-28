package com.dommyg.acvideopoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

class Deck {
    /**
     * Contains hierarchical values and Strings reflective of handCalculation outcomes.
     */
    enum Result {
        ROYAL_FLUSH (9, R.string.result_royal_flush),
        STRAIGHT_FLUSH (8, R.string.result_straight_flush),
        FOUR_OF_A_KIND (7, R.string.result_four_of_a_kind),
        FULL_HOUSE (6, R.string.result_full_house),
        FLUSH (5, R.string.result_flush),
        STRAIGHT (4, R.string.result_straight),
        THREE_OF_A_KIND (3, R.string.result_three_of_a_kind),
        TWO_PAIR (2, R.string.result_two_pair),
        JACKS_OR_BETTER (1, R.string.result_jacks_or_better),
        NOTHING (0, R.string.result_nothing);

        private final int numValue;
        private final int stringValue;

        Result(int numValue, int stringValue) {
            this.numValue = numValue;
            this.stringValue = stringValue;
        }

        public int getNumValue() {
            return numValue;
        }

        public int getStringValue() {
            return stringValue;
        }
    }

    /**
     * Contains all 52 cards and is used to reset the currentDeck at the end of the game.
     */
    static final private ArrayList<Card> masterDeck = initializeMasterDeck();

    /**
     * Adds and returns a currentDeck of all 52 cards.
     */
    private static ArrayList<Card> initializeMasterDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        ArrayList<Card.Suit> suits = new ArrayList<>();

        suits.add(Card.Suit.SPADE);
        suits.add(Card.Suit.CLUB);
        suits.add(Card.Suit.HEART);
        suits.add(Card.Suit.DIAMOND);

        ArrayList<Card.Value> values = new ArrayList<>();

        values.add(Card.Value.TWO);
        values.add(Card.Value.THREE);
        values.add(Card.Value.FOUR);
        values.add(Card.Value.FIVE);
        values.add(Card.Value.SIX);
        values.add(Card.Value.SEVEN);
        values.add(Card.Value.EIGHT);
        values.add(Card.Value.NINE);
        values.add(Card.Value.TEN);
        values.add(Card.Value.JACK);
        values.add(Card.Value.QUEEN);
        values.add(Card.Value.KING);
        values.add(Card.Value.ACE);

        for (Card.Suit suit : suits) {
            for (Card.Value value : values) {
                deck.add(new Card(suit, value));
            }
        }

        return deck;
    }

    /**
     * Contains a deck which is dealt from during the game. Is reset with the masterDeck at the end
     * of each game.
     */
    private ArrayList<Card> currentDeck;

    /**
     * Contains cards dealt during the game, populated by the handDisplay when deal() is called.
     * This ArrayList is used to easily reorganize and analyze the cards to update the handStatus.
     */
    private ArrayList<Card> handCalculation;

    /**
     * Contains cards dealt during the game. This Card Array is used as reference to display in the
     * UI the cards dealt in the proper order.
     */
    private Card[] handDisplay;

    /**
     * Contains the Result of the handCalculation.
     */
    private Result handStatus;

    private static int HAND_SIZE = 5;

    public Deck() {
        this.currentDeck = new ArrayList<>();
        this.currentDeck.addAll(masterDeck);
        this.handCalculation = new ArrayList<>();
        this.handDisplay = new Card[5];
        this.handStatus = Result.NOTHING;
    }

    /**
     * Resets the currentDeck by clearing all cards in it and copying over the masterDeck. Used when
     * the game is over and a fresh deck must be prepared for the next game.
     */
    void resetDeck() {
        currentDeck.subList(0, currentDeck.size()).clear();
        currentDeck.addAll(masterDeck);
    }

    /**
     * Resets the handDisplay by setting all indexes to null. Used when the game is over and the
     * player's hand must be discarded.
     */
    void resetHandDisplay() {
        for (int i = 0; i < HAND_SIZE; i++) {
            handDisplay[i] = null;
        }
    }

    /**
     * Deals cards from the currentDeck into the player's handCalculation until it has five cards
     * total.
     */
    void deal() {
        for (int i = 0; i < HAND_SIZE; i++) {
            // Checking if there are empty positions in the handDisplay. All positions would be
            // empty at the start of the game. Some positions might be empty during mid game if a
            // player does not hold all cards.
            if (handDisplay[i] == null) {
                // Position is empty; remove a random card from the deck and insert it into the
                // handDisplay.
                int randomSelection = getRandom();
                handDisplay[i] = currentDeck.get(randomSelection);
                currentDeck.remove(randomSelection);
            }
        }

        if (handCalculation.size() > 0) {
            // handCalculation is not empty; empty it.
            handCalculation.subList(0, handCalculation.size()).clear();
        }
        // Add the cards from the handDisplay into the handCalculation.
        Collections.addAll(handCalculation, handDisplay);
    }

    /**
     * Returns a random index in the currentDeck. Used to pick a random card from the deck.
     */
    private int getRandom() {
        return new Random().nextInt(currentDeck.size());
    }

    /**
     * Processes holds in the handDisplay. If a card is not selected to be held by the player, it
     * will be discarded.
     */
    void hold(boolean card1, boolean card2, boolean card3, boolean card4, boolean card5) {
        if (!card1) {
            discard(1);
        }
        if (!card2) {
            discard(2);
        }
        if (!card3) {
            discard(3);
        }
        if (!card4) {
            discard(4);
        }
        if (!card5) {
            discard(5);
        }
    }

    /**
     * Discards a selected card from the handDisplay.
     */
    private void discard(int choice) {
        handDisplay[(choice - 1)] = null;
    }

    /**
     * Sorts handCalculation in ascending order.
     */
    void sortHandCalculation() {
        Collections.sort(handCalculation, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return card1.getValue().compareTo(card2.getValue());
            }
        });

        Collections.sort(handCalculation, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return card1.getSuit().compareTo(card2.getSuit());
            }
        });
    }

    private void determineHandStatus() {

    }

    /**
     * Returns if all cards in the handCalculation have the same suit.
     */
    private boolean checkFlush() {
        for (int i = 1; i < HAND_SIZE; i++) {
            if (!handCalculation.get(0).getSuit().equals(handCalculation.get(i).getSuit())) {
                return false;
            }
        }
        return true;
    }

}
