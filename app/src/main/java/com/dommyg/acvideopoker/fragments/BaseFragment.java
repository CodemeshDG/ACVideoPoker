package com.dommyg.acvideopoker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected ViewDataBinding baseBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        baseBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);

        return baseBinding.getRoot();
    }

    public void navigateBack() {
        requireActivity().onBackPressed();
    }

    public abstract int getLayoutId();
}
