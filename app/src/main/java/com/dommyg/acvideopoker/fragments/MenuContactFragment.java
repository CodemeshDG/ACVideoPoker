package com.dommyg.acvideopoker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dommyg.acvideopoker.R;
import com.dommyg.acvideopoker.databinding.FragmentMenuContactBinding;

public class MenuContactFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentMenuContactBinding binding = ((FragmentMenuContactBinding) baseBinding);

        binding.setFragment(this);
    }

    public void openEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT,
                "Feedback for AC Video Poker");
        intent.setData(Uri.parse("mailto: ACVideoPoker@gmail.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Flag for moving backwards to this app, not email app.
        requireContext().startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu_contact;
    }
}
