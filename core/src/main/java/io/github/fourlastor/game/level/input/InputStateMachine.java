package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.input.state.PlayerState;

public class InputStateMachine extends DefaultStateMachine<Entity, PlayerState> {

    @AssistedInject
    public InputStateMachine(@Assisted Entity entity, @Assisted PlayerState initialState) {
        super(entity, initialState);
    }

    @AssistedFactory
    public interface Factory {
        InputStateMachine create(Entity entity, PlayerState initialState);
    }
}
