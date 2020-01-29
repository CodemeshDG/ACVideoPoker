package com.dommyg.acvideopoker;

import androidx.fragment.app.Fragment;

public class GameScreenActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GameScreenFragment.newInstance();
    }
}
