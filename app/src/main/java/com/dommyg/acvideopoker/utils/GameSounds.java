package com.dommyg.acvideopoker.utils;

import android.app.Application;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dommyg.acvideopoker.BR;

import java.io.IOException;

public class GameSounds extends BaseObservable {
    private int bing;
    private int doot;

    public static final int SOUND_BING = 0;
    public static final int SOUND_DOOT = 1;

    private static final float VOLUME_3 = 1.0f;
    private static final float VOLUME_2 = 0.66f;
    private static final float VOLUME_1 = 0.33f;
    private static final float VOLUME_0 = 0;

    static final int VOLUME_3_ITERATOR = 3;
    private static final int VOLUME_2_ITERATOR = 2;
    private static final int VOLUME_1_ITERATOR = 1;
    private static final int VOLUME_0_ITERATOR = 0;

    private float currentVolume;
    private int currentVolumeIterator;

    private SoundPool soundPool;
    private AssetManager assetManager;

    public GameSounds(Application application) {
        this.assetManager = application.getAssets();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        this.soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        setSounds();

        currentVolume = VOLUME_3;
        currentVolumeIterator = VOLUME_3_ITERATOR;
    }

    private void setSounds() {
        try {
            bing = soundPool.load(assetManager.openFd("sounds/bing.wav"), 1);
            doot = soundPool.load(assetManager.openFd("sounds/doot.wav"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bindable
    public int getCurrentVolumeIterator() {
        return currentVolumeIterator;
    }

    public void play(int sound) {
        switch (sound) {
            case SOUND_BING:
                    soundPool.play(bing, currentVolume, currentVolume, 1, 0, 1);
                break;

            case SOUND_DOOT:
                    soundPool.play(doot, currentVolume, currentVolume, 1, 0, 1);
                break;
        }
    }

    /**
     * Updates currentVolume to the next increment depending on its current value.
     */
    public void changeVolume() {
        switch (currentVolumeIterator) {
            case VOLUME_3_ITERATOR:
                currentVolumeIterator = VOLUME_2_ITERATOR;
                currentVolume = VOLUME_2;
                break;

            case VOLUME_2_ITERATOR:
                currentVolumeIterator = VOLUME_1_ITERATOR;
                currentVolume = VOLUME_1;
                break;

            case VOLUME_1_ITERATOR:
                currentVolumeIterator = VOLUME_0_ITERATOR;
                currentVolume = VOLUME_0;
                break;

            case VOLUME_0_ITERATOR:
                currentVolumeIterator = VOLUME_3_ITERATOR;
                currentVolume = VOLUME_3;
                break;
        }
        notifyPropertyChanged(BR.currentVolumeIterator);
    }
}
