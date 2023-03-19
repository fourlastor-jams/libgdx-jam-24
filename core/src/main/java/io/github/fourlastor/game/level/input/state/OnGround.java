package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import javax.inject.Inject;

public class OnGround extends PlayerState {

    @Inject
    public OnGround(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
    }
}
