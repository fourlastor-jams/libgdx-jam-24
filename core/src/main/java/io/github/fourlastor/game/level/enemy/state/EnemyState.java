package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Enemy;
import javax.inject.Inject;

public abstract class EnemyState implements State<Entity> {

    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<Enemy> enemies;
    protected final ImmutableArray<Entity> players;

    public static class Dependencies {

        final ComponentMapper<BodyComponent> bodies;
        final ComponentMapper<Enemy> enemies;

        @Inject
        public Dependencies(ComponentMapper<BodyComponent> bodies, ComponentMapper<Enemy> enemies) {
            this.bodies = bodies;
            this.enemies = enemies;
        }
    }

    private float delta;

    public EnemyState(Dependencies dependencies, ImmutableArray<Entity> players) {
        this.enemies = dependencies.enemies;
        this.bodies = dependencies.bodies;
        this.players = players;
    }

    public final void setDelta(float delta) {
        this.delta = delta;
    }

    protected final float delta() {
        return delta;
    }

    @Override
    public void enter(Entity entity) {}

    @Override
    public void update(Entity entity) {}

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram msg) {
        return false;
    }
}
