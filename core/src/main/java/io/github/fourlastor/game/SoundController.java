package io.github.fourlastor.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SoundController {
    private float audioVolume = 1f;

    @Inject
    public SoundController() {

    }

    public void play(Sound sound, float volume) {
        sound.play(volume * audioVolume, MathUtils.random(.95f, 1.05f), 0);
    }

    public void play(Sound sound) {
        play(sound, 1f);
    }
}
