package com.dommyg.acvideopoker.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.viewmodels.GameViewModel;

public class GameActivity extends AppCompatActivity {
    private GameViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Ties ViewModel to activity lifecycle and allows for fragments to retrieve and use the
        // ViewModel.
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
    }
}
