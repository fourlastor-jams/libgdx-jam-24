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

    public final Settings settings;
    public float movementTime = 0f;
    public float xp = 0f;
    public float maxXp = 1000f;
    public float maxHp = 100f;
    public float hp = maxHp;

    public Player(Camera camera, InputStateMachine stateMachine, OnGround onGround, Settings settings) {
        this.camera = camera;
        this.stateMachine = stateMachine;
        this.onGround = onGround;
        this.settings = settings;
    }

    public static class Settings {
        public final float speed;
        public final float accelerationTime;

        public Settings(float speed, float accelerationTime) {
            this.speed = speed;
            this.accelerationTime = accelerationTime;
        }
    }
}
