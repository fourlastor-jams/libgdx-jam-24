package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Player;
import javax.inject.Inject;

public abstract class PlayerState implements State<Entity> {

    public static class Mappers {
        final ComponentMapper<Player> players;
        final ComponentMapper<BodyComponent> bodies;

        @Inject
        public Mappers(ComponentMapper<Player> players, ComponentMapper<BodyComponent> bodies) {
            this.players = players;
            this.bodies = bodies;
        }
    }

    protected final ComponentMapper<Player> players;
    protected final ComponentMapper<BodyComponent> bodies;

    public PlayerState(Mappers mappers) {
        this.players = mappers.players;
        this.bodies = mappers.bodies;
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
}
