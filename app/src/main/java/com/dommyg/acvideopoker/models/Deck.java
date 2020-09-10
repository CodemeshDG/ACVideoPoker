package com.dommyg.acvideopoker.models;

import com.dommyg.acvideopoker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Deck {
    /**
     * Contains hierarchical values and Strings reflective of handCalculation outcomes.
     */
    public enum Result {
        ROYAL_FLUSH(9, R.string.result_royal_flush),
        STRAIGHT_FLUSH(8, R.string.result_straight_flush),
        FOUR_OF_A_KIND(7, R.string.result_four_of_a_kind),
        FULL_HOUSE(6, R.string.result_full_house),
        FLUSH(5, R.string.result_flush),
        STRAIGHT(4, R.string.result_straight),
        THREE_OF_A_KIND(3, R.string.result_three_of_a_kind),
        TWO_PAIR(2, R.string.result_two_pair),
        JACKS_OR_BETTER(1, R.string.result_jacks_or_better),
        NOTHING(0, R.string.result_nothing);

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

    public final int HAND_SIZE = 5;
    private final int FIRST_CARD = 0;
    private final int HAND_LAST_CARD = 4;

    Deck() {
        this.currentDeck = new ArrayList<>();
        this.currentDeck.addAll(masterDeck);
        this.handCalculation = new ArrayList<>();
        this.handDisplay = new Card[5];
        this.handStatus = Result.NOTHING;
    }

    public Result getHandStatus() {
        return handStatus;
    }

    public Card[] getHandDisplay() {
        return handDisplay;
    }

    /**
     * Resets the currentDeck by clearing all cards in it and copying over the masterDeck. Used when
     * the game is over and a fresh deck must be prepared for the next game.
     */
    public void resetDeck() {
        currentDeck.subList(FIRST_CARD, currentDeck.size()).clear();
        currentDeck.addAll(masterDeck);
    }

    /**
     * Resets the handDisplay by setting all indexes to null. Used when the game is over and the
     * player's hand must be discarded.
     */
    public void resetHandDisplay() {
        for (int i = 0; i < HAND_SIZE; i++) {
            handDisplay[i] = null;
        }
    }

    /**
     * Deals cards from the currentDeck into the player's handCalculation until it has five cards
     * total.
     */
    public void deal() {
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
            handCalculation.subList(FIRST_CARD, handCalculation.size()).clear();
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
    public void hold(boolean[] holds) {
        if (!holds[0]) {
            discard(1);
        }
        if (!holds[1]) {
            discard(2);
        }
        if (!holds[2]) {
            discard(3);
        }
        if (!holds[3]) {
            discard(4);
        }
        if (!holds[4]) {
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
     * Sorts handCalculation in ascending order of value.
     */
    private void sortHandCalculationByValue() {
        Collections.sort(handCalculation, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return card1.getValue().compareTo(card2.getValue());
            }
        });
    }

    /**
     * Sorts handCalculation in ascending order of suit (spades, clubs, hearts, diamonds).
     */
    private void sortHandCalculationBySuit() {
        Collections.sort(handCalculation, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return card1.getSuit().compareTo(card2.getSuit());
            }
        });
    }

    /**
     * Sets the hand status by using various checking functions: checkStraight(), checkFlush(),
     * checkRoyalFlush(), and checkPair().
     */
    public void determineHandStatus() {
        sortHandCalculationByValue();
        boolean straight = checkStraight();

        sortHandCalculationBySuit();
        boolean flush = checkFlush();

        if (flush && straight) {
            if (checkRoyalFlush()) {
                handStatus = Result.ROYAL_FLUSH;
                return;
            } else {
                handStatus = Result.STRAIGHT_FLUSH;
                return;
            }
        }

        if (flush) {
            handStatus = Result.FLUSH;
            return;
        }

        if (straight) {
            handStatus = Result.STRAIGHT;
            return;
        }

        handStatus = checkPair();
    }

    /**
     * Returns true if all cards in the handCalculation have the same suit. Should only be called
     * during a game after sortHandCalculationBySuit() is called or the return might not be accurate.
     */
    private boolean checkFlush() {
        return handCalculation.get(FIRST_CARD).getSuit()
                .equals(handCalculation.get(HAND_LAST_CARD).getSuit());
    }

    /**
     * Returns true if all cards' numValue in the handCalculation are uniquely 1 away from another
     * card (ex: 4, 5, 6, 7, 8). Should only be called during a game after
     * sortHandCalculationByValue() is called or the return might not be accurate.
     */
    private boolean checkStraight() {
        int lastCard = handCalculation.get(HAND_LAST_CARD).getValue().getNumValue();
        int firstCard = handCalculation.get(FIRST_CARD).getValue().getNumValue();

        if (lastCard == Card.Value.ACE.getNumValue()) {
            // Last card is an ace. The ace can start an A-2-3-4-5 straight or can end a 10-J-Q-K-A
            // straight.
            if (firstCard == Card.Value.TWO.getNumValue()
                    || firstCard == Card.Value.TEN.getNumValue()) {
                // First card is either a two or a ten; a straight is possible.
                for (int i = 0; i < HAND_SIZE - 2; i++) {
                    if (handCalculation.get(i).getValue().getNumValue() !=
                            handCalculation.get(i + 1).getValue().getNumValue() - 1) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            // Last card is not an ace. Checking all cards for a straight.
            for (int i = 0; i < HAND_SIZE - 1; i++) {
                if (handCalculation.get(i).getValue().getNumValue() !=
                        handCalculation.get(i + 1).getValue().getNumValue() - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if all cards' numValue in the handCalculation are ten or above. This alone does
     * not prove a royal flush has occurred; only if checkFlush() and checkStraight() also return
     * true proves a royal flush.
     */
    private boolean checkRoyalFlush() {
        for (int i = 0; i < HAND_SIZE; i++) {
            if (handCalculation.get(i).getValue().getNumValue()
                    < Card.Value.TEN.getNumValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs operations related to checking for pairs, which encompasses several results: four of
     * a kind, full house, three of a kind, two pair, jacks or better, or nothing. Returns an int
     * equal to a Result numValue.
     */
    private Result checkPair() {
        // There could be a maximum of two pairs with a five card hand.
        ArrayList<Card> firstSet = new ArrayList<>();
        ArrayList<Card> secondSet = new ArrayList<>();

        while(!handCalculation.isEmpty()) {
            Card source = new Card(handCalculation.get(FIRST_CARD));

            if (firstSet.size() != 0) {
                // First set exists; checking for a match.
                if (source.getValue()
                        .equals(firstSet.get(FIRST_CARD).getValue())) {
                    // Match; add to set and restart loop.
                    firstSet.add(handCalculation.get(FIRST_CARD));
                    handCalculation.remove(FIRST_CARD);
                    continue;
                }
            }

            if (secondSet.size() != 0 ) {
                // Second set exists; checking for a match.
                if(source.getValue()
                        .equals(secondSet.get(FIRST_CARD).getValue())) {
                    // Match; add to set and restart loop.
                    secondSet.add(handCalculation.get(FIRST_CARD));
                    handCalculation.remove(FIRST_CARD);
                    continue;
                }
            }

            if (handCalculation.size() >= 2) {
                // At least two cards remain.
                boolean match = false;
                for (int i = 1; i < handCalculation.size(); i++) {
                    if (source.getValue()
                            .equals(handCalculation.get(i).getValue())) {
                        // Match found; add to firstSet or secondSet if firstSet already contains a
                        // match.
                        match = true;

                        if (firstSet.size() == 0) {
                            firstSet.add(handCalculation.get(FIRST_CARD));
                            firstSet.add(handCalculation.get(i));
                        } else {
                            secondSet.add(handCalculation.get(FIRST_CARD));
                            secondSet.add(handCalculation.get(i));
                        }
                        handCalculation.remove(i);
                        handCalculation.remove(FIRST_CARD);
                        break;
                    }
                }
                if (!match) {
                    // Card matches nothing; remove.
                    handCalculation.remove(FIRST_CARD);
                }
            } else {
                // This is the final card, which matches nothing; remove.
                handCalculation.remove(FIRST_CARD);
            }
        }
        return processPair(firstSet, secondSet);
    }

    /**
     * Returns a Result based on the two sets of cards provided by the checkPair() parsing.
     */
    private Result processPair(ArrayList<Card> firstSet, ArrayList<Card> secondSet) {
        if (firstSet.isEmpty()) {
            return Result.NOTHING;
        } else {
            if (firstSet.size() == 4) {
                return Result.FOUR_OF_A_KIND;
            } else if (firstSet.size() == 3) {
                if (!secondSet.isEmpty()) {
                    return Result.FULL_HOUSE;
                } else {
                    return Result.THREE_OF_A_KIND;
                }
            } else if (secondSet.isEmpty()) {
                if (firstSet.get(FIRST_CARD).getValue().getNumValue()
                        >= Card.Value.JACK.getNumValue()) {
                    return Result.JACKS_OR_BETTER;
                } else {
                    return Result.NOTHING;
                }
            } else if (secondSet.size() == 2) {
                return Result.TWO_PAIR;
            } else {
                return Result.FULL_HOUSE;
            }
        }
    }
}
