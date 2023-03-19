package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;

/** Request to create a Player. */
public class PlayerRequest implements Component {
    public final Camera camera;

    public PlayerRequest(Camera camera) {
        this.camera = camera;
    }
}
