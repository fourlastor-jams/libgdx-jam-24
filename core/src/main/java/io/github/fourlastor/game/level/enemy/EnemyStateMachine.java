package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.enemy.state.EnemyState;

public class EnemyStateMachine extends DefaultStateMachine<Entity, EnemyState> {

    @AssistedInject
    public EnemyStateMachine(@Assisted Entity entity, @Assisted EnemyState initialState) {
        super(entity, initialState);
    }

    public void update(float deltaTime) {
        currentState.setDelta(deltaTime);
        update();
    }

    @AssistedFactory
    public interface Factory {
        EnemyStateMachine create(Entity entity, EnemyState initialState);
    }
}
