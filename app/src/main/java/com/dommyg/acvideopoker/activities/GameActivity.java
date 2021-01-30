package com.dommyg.acvideopoker.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.viewmodels.GameViewModel;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        GameViewModel viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        viewModel.saveState();
        super.onSaveInstanceState(outState);
    }
}
