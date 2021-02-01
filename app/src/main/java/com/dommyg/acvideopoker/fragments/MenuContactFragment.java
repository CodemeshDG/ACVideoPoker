package com.dommyg.acvideopoker.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"ACVideoPoker@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for AC Video Poker");
        // Flag for moving backwards to this app, not email app.
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setSelector(selectorIntent);

        try {
            requireContext().startActivity(Intent.createChooser(emailIntent, null));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    requireContext(),
                    R.string.error_activity_not_found,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu_contact;
    }
}
