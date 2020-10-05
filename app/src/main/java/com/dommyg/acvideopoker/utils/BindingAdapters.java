package com.dommyg.acvideopoker.utils;

import android.graphics.Bitmap;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.dommyg.acvideopoker.TextViewOutline;

public class BindingAdapters {

    @BindingAdapter("android:textSize")
    public static void setTextSize(TextViewOutline textViewOutline, int sp) {
        textViewOutline.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    @BindingAdapter("android:src")
    public static void setImage(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
