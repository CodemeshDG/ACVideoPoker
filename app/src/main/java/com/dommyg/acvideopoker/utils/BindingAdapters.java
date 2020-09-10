package com.dommyg.acvideopoker.utils;

import android.util.TypedValue;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("android:textSize")
    public static void setTextSize(TextView textView, int sp) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }
}
