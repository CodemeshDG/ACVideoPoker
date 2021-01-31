package com.dommyg.acvideopoker.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.databinding.FragmentMenuCardsBinding;
import com.dommyg.acvideopoker.viewmodels.GameViewModel;

public class MenuCardsFragment extends BaseFragment {
    private FragmentMenuCardsBinding binding;
    private GameViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding = ((FragmentMenuCardsBinding) baseBinding);

        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        binding.setFragment(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu_cards;
    }
}
