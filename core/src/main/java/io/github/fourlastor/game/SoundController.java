package io.github.fourlastor.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SoundController {
    private float soundVolume = 0.45f;
    private float musicVolume = 0.1f;

    @Inject
    public SoundController() {}

    public void play(Music music, float volume, boolean repeat) {
        music.setVolume(volume * musicVolume);
        if (repeat) music.setLooping(true);
        music.play();
    }

    public void play(Music music) {
        play(music, 1f, false);
    }

    public void play(Sound sound, float volume) {
        sound.play(volume * soundVolume, MathUtils.random(.95f, 1.05f), 0);
    }

    public void play(Sound sound) {
        play(sound, 1f);
    }
}
