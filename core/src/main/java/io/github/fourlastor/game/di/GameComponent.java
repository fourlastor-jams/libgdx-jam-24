package io.github.fourlastor.game.di;

import dagger.Component;
import io.github.fourlastor.game.GdxGame;
import io.github.fourlastor.game.di.modules.AssetsModule;
import io.github.fourlastor.game.di.modules.DragonBonesModule;
import io.github.fourlastor.game.di.modules.GameModule;
import io.github.fourlastor.game.di.modules.GdxModule;
import io.github.fourlastor.game.di.modules.LdtkModule;
import io.github.fourlastor.game.di.modules.ScreensModule;
import io.github.fourlastor.game.di.modules.SpineModule;
import javax.inject.Singleton;

@Singleton
@Component(
        modules = {
            GameModule.class,
            AssetsModule.class,
            GdxModule.class,
            ScreensModule.class,
            LdtkModule.class,
            SpineModule.class,
            DragonBonesModule.class,
        })
public interface GameComponent {
    GdxGame game();

    static GameComponent component() {
        return DaggerGameComponent.create();
    }
}
