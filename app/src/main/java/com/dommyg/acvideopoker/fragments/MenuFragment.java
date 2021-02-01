package com.dommyg.acvideopoker.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.databinding.FragmentMenuBinding;

public class MenuFragment extends BaseFragment {
    private NavController navController;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        ((FragmentMenuBinding) baseBinding).setFragment(this);
    }

    public void navigateToPayouts() {
        navController.navigate(R.id.action_menuFragment_to_menuPayoutsFragment);
    }

    public void navigateToBank() {
        navController.navigate(R.id.action_menuFragment_to_menuBankFragment);
    }

    public void navigateToStatistics() {
        navController.navigate(R.id.action_menuFragment_to_menuStatisticsFragment);
    }

    public void navigateToCards() {
        navController.navigate(R.id.action_menuFragment_to_menuCardsFragment);
    }

    public void navigateToContact() {
        navController.navigate(R.id.action_menuFragment_to_menuContactFragment);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu;
    }
}
