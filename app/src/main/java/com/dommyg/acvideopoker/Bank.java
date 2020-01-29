package com.dommyg.acvideopoker;

import java.math.BigDecimal;

/**
 * Holds the player's money.
 */
class Bank {

    private BigDecimal bankroll;

    Bank() {
        this.bankroll = BigDecimal.valueOf(200.00);
    }

    BigDecimal getBankroll() {
        return bankroll;
    }

    void setBankroll(BigDecimal bankroll) {
        this.bankroll = bankroll;
    }
}
