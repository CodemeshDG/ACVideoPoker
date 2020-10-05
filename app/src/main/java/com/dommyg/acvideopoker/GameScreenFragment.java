package com.dommyg.acvideopoker;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameScreenFragment extends Fragment {

//    private Button buttonMenu;
//    private Button buttonSound;
//    private Button buttonSpeed;
//    private Button buttonDoubleUp;
//    private Button buttonBet;
//    private Button buttonDeal;
//    private Button buttonDenomination;
//
//    private Button[] buttons;
//
//    final int ARRAY_BUTTON_MENU = 0;
//    final int ARRAY_BUTTON_SOUND = 1;
//    final int ARRAY_BUTTON_SPEED = 2;
//    final int ARRAY_BUTTON_DOUBLE_UP = 3;
//    final int ARRAY_BUTTON_BET = 4;
//    final int ARRAY_BUTTON_DEAL = 5;
//    final int ARRAY_BUTTON_DENOMINATION = 6;
//
//    private ImageView imageViewCard1;
//    private ImageView imageViewCard2;
//    private ImageView imageViewCard3;
//    private ImageView imageViewCard4;
//    private ImageView imageViewCard5;
//
//    private ImageView[] cards;
//
//    private TextViewOutline textViewResult;
//    private TextViewOutline textViewHold1;
//    private TextViewOutline textViewHold2;
//    private TextViewOutline textViewHold3;
//    private TextViewOutline textViewHold4;
//    private TextViewOutline textViewHold5;
//    private TextViewOutline textViewWin;
//    private TextViewOutline textViewCredit;
//    private TextViewOutline textViewBet;
//    private TextViewOutline textViewGameOverCorner;
//    private TextViewOutline textViewGameOverCenter;
//
//    private TextViewOutline[] textViewHolds;
//
//    final int ARRAY_CARD_1 = 0;
//    final int ARRAY_CARD_2 = 1;
//    final int ARRAY_CARD_3 = 2;
//    final int ARRAY_CARD_4 = 3;
//    final int ARRAY_CARD_5 = 4;
//
//    private TextViewOutline[] textViewOperations;
//
//    final int ARRAY_OPERATIONS_RESULT = 0;
//    final int ARRAY_OPERATIONS_WIN = 1;
//    final int ARRAY_OPERATIONS_CREDIT = 2;
//    final int ARRAY_OPERATIONS_BET = 3;
//    final int ARRAY_OPERATIONS_GAME_OVER_CORNER = 4;
//    final int ARRAY_OPERATIONS_GAME_OVER_CENTER = 5;
//
//    private GameLogic gameLogic;
//
//    static GameScreenFragment newInstance(Resources resources) {
//        return new GameScreenFragment(resources);
//    }
//
//    private GameScreenFragment(Resources resources) {
//        this.gameLogic = new GameLogic(this, resources);
//    }
//
//    Button[] getButtons() {
//        return buttons;
//    }
//
//    ImageView[] getCards() {
//        return cards;
//    }
//
//    TextViewOutline[] getTextViewHolds() {
//        return textViewHolds;
//    }
//
//    TextViewOutline[] getTextViewOperations() {
//        return textViewOperations;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_machine_screen, container, false);

//        setViews(v);
//        setArrays();
//        gameLogic.initializeGameElements();

        return v;
    }

//    private void setViews(View v) {
//        buttonMenu = v.findViewById(R.id.buttonMenu);
//
//        buttonSound = v.findViewById(R.id.buttonSound);
//        buttonSound.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gameLogic.processChangeVolume();
//            }
//        });
//
//        buttonSpeed = v.findViewById(R.id.buttonSpeed);
//        buttonSpeed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gameLogic.processChangeSpeed();
//            }
//        });
//
//        buttonDoubleUp = v.findViewById(R.id.buttonDoubleUp);
//
//        buttonBet = v.findViewById(R.id.buttonBet);
//        buttonBet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gameLogic.processChangeBet();
//            }
//        });
//
//        buttonDeal = v.findViewById(R.id.buttonDeal);
//        buttonDeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gameLogic.run();
//            }
//        });
//
//        buttonDenomination = v.findViewById(R.id.buttonDenom);
//        buttonDenomination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gameLogic.processChangeDenomination();
//            }
//        });
//
//        imageViewCard1 = v.findViewById(R.id.imageViewCard1);
//        imageViewCard1.setOnClickListener(new cardClickListener(ARRAY_CARD_1));
//
//        imageViewCard2 = v.findViewById(R.id.imageViewCard2);
//        imageViewCard2.setOnClickListener(new cardClickListener(ARRAY_CARD_2));
//
//        imageViewCard3 = v.findViewById(R.id.imageViewCard3);
//        imageViewCard3.setOnClickListener(new cardClickListener(ARRAY_CARD_3));
//
//        imageViewCard4 = v.findViewById(R.id.imageViewCard4);
//        imageViewCard4.setOnClickListener(new cardClickListener(ARRAY_CARD_4));
//
//        imageViewCard5 = v.findViewById(R.id.imageViewCard5);
//        imageViewCard5.setOnClickListener(new cardClickListener(ARRAY_CARD_5));
//
//        textViewResult = v.findViewById(R.id.textViewResult);
//
//        textViewHold1 = v.findViewById(R.id.textViewHold1);
//
//        textViewHold2 = v.findViewById(R.id.textViewHold2);
//
//        textViewHold3 = v.findViewById(R.id.textViewHold3);
//
//        textViewHold4 = v.findViewById(R.id.textViewHold4);
//
//        textViewHold5 = v.findViewById(R.id.textViewHold5);
//
//        textViewWin = v.findViewById(R.id.textViewWin);
//
//        textViewCredit = v.findViewById(R.id.textViewCredit);
//
//        textViewBet = v.findViewById(R.id.textViewBet);
//
//        textViewGameOverCorner = v.findViewById(R.id.textViewGameOverCorner);
//
//        textViewGameOverCenter = v.findViewById(R.id.textViewGameOverCenter);
//    }
//
//    private void setArrays() {
//        buttons = new Button[] {buttonMenu, buttonSound, buttonSpeed, buttonDoubleUp, buttonBet,
//                buttonDeal, buttonDenomination};
//        cards = new ImageView[] {imageViewCard1, imageViewCard2, imageViewCard3, imageViewCard4,
//                imageViewCard5};
//        textViewHolds = new TextViewOutline[] {textViewHold1, textViewHold2, textViewHold3, textViewHold4,
//                textViewHold5};
//        textViewOperations = new TextViewOutline[] {textViewResult, textViewWin, textViewCredit,
//                textViewBet, textViewGameOverCorner, textViewGameOverCenter};
//    }
//
//    private class cardClickListener implements View.OnClickListener {
//        private int index;
//
//        private cardClickListener(int index) {
//            this.index = index;
//        }
//
//        @Override
//        public void onClick(View view) {
//            gameLogic.setHolds(index);
//        }
//    }
}
