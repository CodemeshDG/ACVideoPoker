package com.dommyg.acvideopoker.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.databinding.FragmentMachineScreenBinding;
import com.dommyg.acvideopoker.viewmodels.GameViewModel;

public class GameScreenFragment extends BaseFragment {
    private NavController navController;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        GameViewModel viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        FragmentMachineScreenBinding binding = ((FragmentMachineScreenBinding) baseBinding);

        binding.setViewModel(viewModel);
        binding.setFragment(this);
    }

    public void navigateToGameMenu() {
        navController.navigate(R.id.action_gameScreenFragment_to_menuFragment);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_machine_screen;
    }
}
