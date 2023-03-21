package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.InputMultiplexer;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.GdxGame;
import io.github.fourlastor.game.gameover.GameOverComponent;
import io.github.fourlastor.game.intro.IntroComponent;
import io.github.fourlastor.game.level.di.LevelComponent;
import javax.inject.Singleton;
import squidpony.squidmath.FastNoise;
import squidpony.squidmath.Noise;
import squidpony.squidmath.SilkRNG;

@Module
public class GameModule {

    @Provides
    @Singleton
    public GdxGame game(
            InputMultiplexer multiplexer,
            LevelComponent.Builder levelBuilder,
            IntroComponent.Builder introBuilder,
            GameOverComponent.Builder gameOverBuilder) {
        return new GdxGame(multiplexer, levelBuilder, introBuilder, gameOverBuilder);
    }

    @Provides
    public SilkRNG random() {
        return new SilkRNG();
    }

    @Provides
    public Noise.Noise3D noise3D() {
        return new FastNoise((int) System.currentTimeMillis());
    }
}
