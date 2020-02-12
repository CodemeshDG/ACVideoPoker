package com.dommyg.acvideopoker;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.io.IOException;

class GameSounds {
    private int bing;
    private int doot;

    final int SOUND_BING = 0;
    final int SOUND_DOOT = 1;

    private final float VOLUME_3 = 1.0f;
    private final float VOLUME_2 = 0.66f;
    private final float VOLUME_1 = 0.33f;
    private final float VOLUME_0 = 0;

    final int VOLUME_3_ITERATOR = 3;
    private final int VOLUME_2_ITERATOR = 2;
    private final int VOLUME_1_ITERATOR = 1;
    private final int VOLUME_0_ITERATOR = 0;

    private float currentVolume;
    private int currentVolumeIterator;

    private SoundPool soundPool;
    private Context context;

    GameSounds(Context context) {
        this.context = context;
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
        AssetManager assetManager = context.getAssets();

        try {
            bing = soundPool.load(assetManager.openFd("sounds/bing.wav"), 1);
            doot = soundPool.load(assetManager.openFd("sounds/doot.wav"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getCurrentVolumeIterator() {
        return currentVolumeIterator;
    }

    void play(int sound) {
        switch (sound) {
            case SOUND_BING:
                soundPool.play(bing, currentVolume, currentVolume, 1, 0, 1);
                break;

            case SOUND_DOOT:
                soundPool.play(doot, currentVolume, currentVolume, 1, 0, 1);
                break;
        }
    }

    int changeVolume() {
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
        return currentVolumeIterator;
    }
}
