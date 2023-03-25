package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import io.github.fourlastor.game.level.Message;
import javax.inject.Inject;

public class Dead extends PlayerState {

    private final MessageDispatcher dispatcher;

    @Inject
    public Dead(Mappers mappers, MessageDispatcher dispatcher) {
        super(mappers);
        this.dispatcher = dispatcher;
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        bodies.get(entity).body.setLinearVelocity(0, 0);
        dispatcher.dispatchMessage(Message.GAME_OVER.ordinal());
    }
}
