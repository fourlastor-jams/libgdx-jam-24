package io.github.fourlastor.game.level.reward.state;

import com.badlogic.ashley.core.Entity;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;

public class Chasing extends Movement {

    @AssistedInject
    public Chasing(@Assisted Entity players, Dependencies mappers) {
        super(players, mappers);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
    }

    @Override
    protected boolean chasing() {
        return true;
    }

    @AssistedFactory
    public interface Factory {
        Chasing create(Entity player);
    }
}
