package io.github.fourlastor.game.level.weapon.whip;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.weapon.whip.state.WhipState;

public class WhipStateMachine extends DefaultStateMachine<Entity, WhipState> {

    @AssistedInject
    public WhipStateMachine(@Assisted Entity entity, @Assisted WhipState initialState) {
        super(entity, initialState);
    }

    public void update(float deltaTime) {
        currentState.setDelta(deltaTime);
        update();
    }

    @AssistedFactory
    public interface Factory {
        WhipStateMachine create(Entity entity, WhipState initialState);
    }
}
