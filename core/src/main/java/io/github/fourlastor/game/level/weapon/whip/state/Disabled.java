package io.github.fourlastor.game.level.weapon.whip.state;

import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.level.component.Whip;
import javax.inject.Inject;

public class Disabled extends WhipState {

    @Inject
    public Disabled(Dependencies dependencies) {
        super(dependencies);
    }

    @Override
    protected float timer(Entity entity) {
        return players.get(entity).whipWaitTime;
    }

    @Override
    protected boolean canCollide() {
        return false;
    }

    @Override
    protected WhipState nextState(Whip whip) {
        return whip.enabled;
    }
}
