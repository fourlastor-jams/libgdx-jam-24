package io.github.fourlastor.game.level.reward.state;

import com.badlogic.ashley.core.Entity;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;

public class Away extends Movement {


    @AssistedInject
    public Away(@Assisted Entity player, Dependencies mappers) {
        super(player, mappers);
    }

    @Override
    protected boolean chasing() {
        return false;
    }


    @AssistedFactory
    public interface Factory {
        Away create(Entity player);
    }
}
