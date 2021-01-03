package com.dommyg.acvideopoker.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.databinding.FragmentMenuPayoutsBinding;
import com.dommyg.acvideopoker.viewmodels.GameViewModel;

public class MenuPayoutsFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GameViewModel viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        FragmentMenuPayoutsBinding binding = ((FragmentMenuPayoutsBinding) baseBinding);

        binding.setFragment(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu_payouts;
    }
}
