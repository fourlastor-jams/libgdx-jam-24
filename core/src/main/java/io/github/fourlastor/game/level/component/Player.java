package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.OnGround;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class Player implements Component {

    public final Camera camera;
    public final InputStateMachine stateMachine;
    public final OnGround onGround;

    public Player(
            Camera camera, InputStateMachine stateMachine,
            OnGround onGround) {
        this.camera = camera;
        this.stateMachine = stateMachine;
        this.onGround = onGround;
    }
}
