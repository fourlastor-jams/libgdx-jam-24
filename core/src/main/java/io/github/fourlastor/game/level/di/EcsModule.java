package io.github.fourlastor.game.level.di;

import com.badlogic.ashley.core.ComponentMapper;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.Animated;
import io.github.fourlastor.game.level.component.BodyBuilderComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.component.HpBar;
import io.github.fourlastor.game.level.component.MovingComponent;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.component.Reward;
import io.github.fourlastor.game.level.component.SoundComponent;
import io.github.fourlastor.game.level.component.Whip;
import io.github.fourlastor.game.level.component.XpBarComponent;

@Module
public class EcsModule {

    @Provides
    @ScreenScoped
    public ComponentMapper<ActorComponent> actorComponent() {
        return ComponentMapper.getFor(ActorComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<BodyComponent> bodyComponent() {
        return ComponentMapper.getFor(BodyComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<BodyBuilderComponent> bodyBuilderComponent() {
        return ComponentMapper.getFor(BodyBuilderComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Player> playerComponent() {
        return ComponentMapper.getFor(Player.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<MovingComponent> movingComponent() {
        return ComponentMapper.getFor(MovingComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<SoundComponent> soundComponent() {
        return ComponentMapper.getFor(SoundComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Animated> animatedComponent() {
        return ComponentMapper.getFor(Animated.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Whip> whipComponent() {
        return ComponentMapper.getFor(Whip.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Enemy> enemyComponent() {
        return ComponentMapper.getFor(Enemy.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<HpBar> hpBarComponent() {
        return ComponentMapper.getFor(HpBar.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Reward> rewardComponent() {
        return ComponentMapper.getFor(Reward.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<XpBarComponent> xpBarComponent() {
        return ComponentMapper.getFor(XpBarComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Reward.PickUp> rewardPickupComponent() {
        return ComponentMapper.getFor(Reward.PickUp.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<Reward.State> rewardStateComponent() {
        return ComponentMapper.getFor(Reward.State.class);
    }
}
