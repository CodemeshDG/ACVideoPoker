package com.dommyg.acvideopoker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Deck myDeck = new Deck();
        setContentView(R.layout.fragment_machine_screen);
        Button deal = findViewById(R.id.buttonDeal);

        deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDeck.deal();
            }
        });

        Button sort = findViewById(R.id.buttonDoubleUp);

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDeck.sortHandCalculation();
            }
        });

        Button reset = findViewById(R.id.buttonMenu);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDeck.resetDeck();
                myDeck.resetHandDisplay();
            }
        });
    }
}
