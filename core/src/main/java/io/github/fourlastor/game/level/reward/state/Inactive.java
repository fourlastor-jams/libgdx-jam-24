package io.github.fourlastor.game.level.reward.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.component.Reward;

public class Inactive extends RewardState {


    @AssistedInject
    public Inactive(
            @Assisted Entity player,
            Dependencies mappers) {
        super(mappers, player);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        Vector2 position = bodies.get(entity).body.getPosition();
        Vector2 playerPosition = bodies.get(player).body.getPosition();
        if (position.dst(playerPosition) < 4f) {
            Reward.State state = rewards.get(entity);
            state.stateMachine.changeState(state.away);
        }
    }

    @AssistedFactory
    public interface Factory {
        Inactive create(Entity player);
    }
}
