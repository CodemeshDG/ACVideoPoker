package com.dommyg.acvideopoker.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.dommyg.acvideopoker.TextViewOutline;

import java.io.IOException;
import java.io.InputStream;

public class BindingAdapters {

    @BindingAdapter("android:textSize")
    public static void setTextSize(TextViewOutline textViewOutline, int sp) {
        textViewOutline.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

//    @BindingAdapter("android:src")
//    public static void setImage(ImageView imageView, AssetManager assetManager, String imagePath) {
//        InputStream inputStream;
//        try {
//            inputStream = assetManager.open(imagePath);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            imageView.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
