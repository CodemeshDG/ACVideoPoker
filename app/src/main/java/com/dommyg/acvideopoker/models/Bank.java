package com.dommyg.acvideopoker.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dommyg.acvideopoker.BR;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Holds the player's money.
 */
public class Bank extends BaseObservable {

    private BigDecimal bankroll;

    Bank() {
        this.bankroll = BigDecimal.valueOf(200.00).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Bindable
    public BigDecimal getBankroll() {
        return bankroll;
    }

    void setBankroll(BigDecimal bankroll) {
        this.bankroll = bankroll;
        notifyPropertyChanged(BR.bankroll);
    }
}
