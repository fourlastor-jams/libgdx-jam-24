package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.Animated;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.component.Player;
import javax.inject.Inject;

public abstract class PlayerState implements State<Entity> {

    private float delta = 0f;

    public static class Mappers {
        final ComponentMapper<Player> players;
        final ComponentMapper<BodyComponent> bodies;
        final ComponentMapper<ActorComponent> actors;
        final ComponentMapper<Animated> animated;
        final ComponentMapper<Enemy> enemies;

        @Inject
        public Mappers(
                ComponentMapper<Player> players,
                ComponentMapper<BodyComponent> bodies,
                ComponentMapper<ActorComponent> actors,
                ComponentMapper<Animated> animated,
                ComponentMapper<Enemy> enemies) {
            this.players = players;
            this.bodies = bodies;
            this.actors = actors;
            this.animated = animated;
            this.enemies = enemies;
        }
    }

    protected final ComponentMapper<Player> players;
    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<ActorComponent> actors;
    protected final ComponentMapper<Animated> animated;
    protected final ComponentMapper<Enemy> enemies;

    public PlayerState(Mappers mappers) {
        this.players = mappers.players;
        this.bodies = mappers.bodies;
        this.actors = mappers.actors;
        this.animated = mappers.animated;
        this.enemies = mappers.enemies;
    }

    @Override
    public void enter(Entity entity) {}

    @Override
    public void update(Entity entity) {}

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }

    public final void setDelta(float delta) {
        this.delta = delta;
    }

    protected final float delta() {
        return delta;
    }
}
