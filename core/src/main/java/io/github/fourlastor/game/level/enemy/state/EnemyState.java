package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.component.Player;

import javax.inject.Inject;

public abstract class EnemyState implements State<Entity> {

    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<Enemy> enemies;
    protected final ComponentMapper<ActorComponent> actors;
    protected final ComponentMapper<Player> players;
    protected final ImmutableArray<Entity> playersEntities;

    public static class Dependencies {

        final ComponentMapper<BodyComponent> bodies;
        final ComponentMapper<Enemy> enemies;
        final ComponentMapper<ActorComponent> actors;
        final ComponentMapper<Player> players;

        @Inject
        public Dependencies(
                ComponentMapper<BodyComponent> bodies,
                ComponentMapper<Enemy> enemies,
                ComponentMapper<ActorComponent> actors,
                ComponentMapper<Player> players) {
            this.bodies = bodies;
            this.enemies = enemies;
            this.actors = actors;
            this.players = players;
        }
    }

    private float delta;

    public EnemyState(Dependencies dependencies, ImmutableArray<Entity> playersEntities) {
        enemies = dependencies.enemies;
        bodies = dependencies.bodies;
        actors = dependencies.actors;
        players = dependencies.players;
        this.playersEntities = playersEntities;
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
