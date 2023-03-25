package io.github.fourlastor.game.level.reward;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.reward.state.RewardState;

public class RewardStateMachine extends DefaultStateMachine<Entity, RewardState> {

    @AssistedInject
    public RewardStateMachine(@Assisted Entity entity, @Assisted RewardState initialState) {
        super(entity, initialState);
    }

    public void update(float deltaTime) {
        currentState.setDelta(deltaTime);
        update();
    }

    @AssistedFactory
    public interface Factory {
        RewardStateMachine create(Entity entity, RewardState initialState);
    }
}
