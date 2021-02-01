package com.dommyg.acvideopoker.models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dommyg.acvideopoker.BR;
import com.dommyg.acvideopoker.utils.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Holds the player's money.
 */
public class Bank extends BaseObservable {

    private BigDecimal bankroll;
    private Application application;

    Bank(Application application) {
        this.application = application;
        this.bankroll = loadBankroll();
    }

    @Bindable
    public BigDecimal getBankroll() {
        return bankroll;
    }

    void setBankroll(BigDecimal bankroll) {
        this.bankroll = bankroll.setScale(2, RoundingMode.HALF_EVEN);
        notifyPropertyChanged(BR.bankroll);
    }

    private BigDecimal loadBankroll() {
        SharedPreferences sharedPreferences =
                application.getSharedPreferences("machine", Context.MODE_PRIVATE);
        return BigDecimal.valueOf(
                Double.parseDouble(
                        sharedPreferences.getString(
                                Constants.KEY_BANKROLL,
                                "200.00"
                        )
                )
        ).setScale(
                2,
                RoundingMode.HALF_EVEN
        );
    }

    public void saveBankroll(SharedPreferences.Editor editor) {
        editor.putString(Constants.KEY_BANKROLL, String.valueOf(bankroll));
    }

    public void addCash(double amount) {
        setBankroll(
                this.bankroll.add(
                        BigDecimal.valueOf(amount)
                                .setScale(2, RoundingMode.HALF_EVEN)
                )
        );
    }

    public void resetBankroll() {
        SharedPreferences sharedPreferences =
                application.getSharedPreferences("bank", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
