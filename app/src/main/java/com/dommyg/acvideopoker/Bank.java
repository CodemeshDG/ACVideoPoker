package com.dommyg.acvideopoker;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Holds the player's money.
 */
class Bank {

    private BigDecimal bankroll;

    Bank() {
        this.bankroll = BigDecimal.valueOf(200.00).setScale(2, RoundingMode.HALF_EVEN);
    }

    BigDecimal getBankroll() {
        return bankroll;
    }

    void setBankroll(BigDecimal bankroll) {
        this.bankroll = bankroll;
    }
}
