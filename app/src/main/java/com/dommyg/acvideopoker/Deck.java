package com.dommyg.acvideopoker;

import java.util.ArrayList;

class Deck {
    /**
     * Contains hierarchical values and Strings reflective of hand outcomes.
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
     * Contains all 52 cards and is used to reset the deck at the end of the game.
     */
    static final private ArrayList<Card> masterDeck = initializeMasterDeck();

    /**
     * Adds and returns a deck of all 52 cards.
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



}
