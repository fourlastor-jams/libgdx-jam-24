package io.github.fourlastor.game.level.weapon.whip.state;

import io.github.fourlastor.game.level.component.Whip;
import javax.inject.Inject;

public class Enabled extends WhipState {

    @Inject
    public Enabled(Dependencies mappers) {
        super(mappers);
    }

    @Override
    protected boolean canCollide() {
        return true;
    }

    @Override
    protected WhipState nextState(Whip whip) {
        return whip.disabled;
    }

    @Override
    protected float timer() {
        return 5;
    }
}
