package com.dommyg.acvideopoker.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.databinding.FragmentMenuStatisticsBinding;
import com.dommyg.acvideopoker.viewmodels.GameViewModel;

public class MenuStatisticsFragment extends BaseFragment {
    private GameViewModel viewModel;
    private FragmentMenuStatisticsBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        binding = ((FragmentMenuStatisticsBinding) baseBinding);

        binding.setFragment(this);
        binding.setStatistics(viewModel.getJacksOrBetter().getStatistics());
        binding.setIsResettingMachine(viewModel.getIsResettingMachine());
    }

    public void leftButtonPress() {
        if (viewModel.getIsResettingMachine().get()) {
            resetMachine();
        } else {
            navigateBack();
        }
    }

    public void rightButtonPress() {
        viewModel.setIsResettingMachine(!viewModel.getIsResettingMachine().get());
    }

    private void resetMachine() {
        viewModel.resetMachine();
        binding.setStatistics(viewModel.getJacksOrBetter().getStatistics());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu_statistics;
    }
}
