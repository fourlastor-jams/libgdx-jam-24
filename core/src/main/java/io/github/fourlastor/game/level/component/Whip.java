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
    public final Actor front;
    public final Actor back;
    public final Actor top;
    public final Actor bottom;

    public Whip(
            WhipStateMachine stateMachine,
            Enabled enabled,
            Disabled disabled,
            Actor front,
            Actor back,
            Actor top,
            Actor bottom) {
        this.stateMachine = stateMachine;
        this.enabled = enabled;
        this.disabled = disabled;
        this.front = front;
        this.back = back;
        this.top = top;
        this.bottom = bottom;
    }

    public static class Request implements Component {

        public final Actor front;
        public final Actor back;
        public final Actor top;
        public final Actor bottom;

        public Request(Actor front, Actor back, Actor top, Actor bottom) {
            this.front = front;
            this.back = back;
            this.top = top;
            this.bottom = bottom;
        }
    }
}
