package com.dommyg.acvideopoker;

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

class GameScreenFragment extends Fragment {

    private Button buttonMenu;
    private Button buttonSound;
    private Button buttonSpeed;
    private Button buttonDoubleUp;
    private Button buttonBet;
    private Button buttonDeal;

    private Button[] buttons = {buttonMenu, buttonSound, buttonSpeed, buttonDoubleUp, buttonBet,
            buttonDeal};

    final int ARRAY_BUTTON_MENU = 0;
    final int ARRAY_BUTTON_SOUND = 1;
    final int ARRAY_BUTTON_SPEED = 2;
    final int ARRAY_BUTTON_DOUBLE_UP = 3;
    final int ARRAY_BUTTON_BET = 4;
    final int ARRAY_BUTTON_DEAL = 5;

    private ImageView imageViewCard1;
    private ImageView imageViewCard2;
    private ImageView imageViewCard3;
    private ImageView imageViewCard4;
    private ImageView imageViewCard5;

    private ImageView[] cards = {imageViewCard1, imageViewCard2, imageViewCard3, imageViewCard4,
            imageViewCard5};

    private TextView textViewResult;
    private TextView textViewHold1;
    private TextView textViewHold2;
    private TextView textViewHold3;
    private TextView textViewHold4;
    private TextView textViewHold5;
    private TextView textViewWin;
    private TextView textViewCredit;
    private TextView textViewBet;
    private TextView textViewGameOver;

    private TextView[] textViewHolds = {textViewHold1, textViewHold2, textViewHold3, textViewHold4,
            textViewHold5};

    final int ARRAY_CARD_1 = 0;
    final int ARRAY_CARD_2 = 1;
    final int ARRAY_CARD_3 = 2;
    final int ARRAY_CARD_4 = 3;
    final int ARRAY_CARD_5 = 4;

    private TextView[] textViewOperations = {textViewResult, textViewWin, textViewCredit,
            textViewBet, textViewGameOver};

    final int ARRAY_OPERATIONS_RESULT = 0;
    final int ARRAY_OPERATIONS_WIN = 1;
    final int ARRAY_OPERATIONS_CREDIT = 2;
    final int ARRAY_OPERATIONS_BET = 3;
    final int ARRAY_OPERATIONS_GAME_OVER = 4;

    private GameLogic gameLogic;

    static GameScreenFragment newInstance() {
        return new GameScreenFragment();
    }

    private GameScreenFragment() {
        this.gameLogic = new GameLogic(this, getResources());
    }

    Button[] getButtons() {
        return buttons;
    }

    ImageView[] getCards() {
        return cards;
    }

    TextView[] getTextViewHolds() {
        return textViewHolds;
    }

    TextView[] getTextViewOperations() {
        return textViewOperations;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_machine_screen, container, false);

        setViews(v);

        return v;
    }

    private void setViews(View v) {
        buttonMenu = v.findViewById(R.id.buttonMenu);

        buttonSound = v.findViewById(R.id.buttonSound);

        buttonSpeed = v.findViewById(R.id.buttonSpeed);

        buttonDoubleUp = v.findViewById(R.id.buttonDoubleUp);

        buttonBet = v.findViewById(R.id.buttonBet);

        buttonDeal = v.findViewById(R.id.buttonDeal);

        textViewResult = v.findViewById(R.id.textViewResult);

        textViewHold1 = v.findViewById(R.id.textViewHold1);

        textViewHold2 = v.findViewById(R.id.textViewHold2);

        textViewHold3 = v.findViewById(R.id.textViewHold3);

        textViewHold4 = v.findViewById(R.id.textViewHold4);

        textViewHold5 = v.findViewById(R.id.textViewHold5);

        textViewWin = v.findViewById(R.id.textViewWin);

        textViewCredit = v.findViewById(R.id.textViewCredit);

        textViewBet = v.findViewById(R.id.textViewBet);

        textViewGameOver = v.findViewById(R.id.textViewGameOver);
    }
}
