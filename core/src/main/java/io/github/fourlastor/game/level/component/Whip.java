package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.fourlastor.game.level.weapon.whip.WhipStateMachine;
import io.github.fourlastor.game.level.weapon.whip.state.Disabled;
import io.github.fourlastor.game.level.weapon.whip.state.Enabled;

public class Whip implements Component {

    public final WhipStateMachine stateMachine;
    public final Enabled enabled;
    public final Disabled disabled;
    public final Actor actor;

    public Whip(WhipStateMachine stateMachine, Enabled enabled, Disabled disabled, Actor actor) {
        this.stateMachine = stateMachine;
        this.enabled = enabled;
        this.disabled = disabled;
        this.actor = actor;
    }

    public static class Request implements Component {

        public final Actor actor;

        public Request(Actor actor) {
            this.actor = actor;
        }
    }
}
