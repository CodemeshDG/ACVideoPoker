package com.dommyg.acvideopoker;

import java.math.BigDecimal;

/**
 * Contains all the elements for playing the game. Holds a deck and a bank.
 */
class Machine {

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

    Machine() {
        this.deck = new Deck();
        this.bank = new Bank();
        this.betDenomination = BigDecimal.valueOf(.25);
        this.bet = 1;
        this.winAmount = BigDecimal.valueOf(0);
    }

    Deck getDeck() {
        return deck;
    }

    Bank getBank() {
        return bank;
    }

    BigDecimal getBetDenomination() {
        return betDenomination;
    }

    void setBetDenomination(BigDecimal betDenomination) {
        this.betDenomination = betDenomination;
    }

    int getBet() {
        return bet;
    }

    void setBet(int bet) {
        this.bet = bet;
    }

    BigDecimal getWinAmount() {
        return winAmount;
    }

    void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    /**
     * Determines which amount to payout to the player based upon the deck's handStatus.
     */
    void determinePayout() {
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
    void processWager() {
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
}
