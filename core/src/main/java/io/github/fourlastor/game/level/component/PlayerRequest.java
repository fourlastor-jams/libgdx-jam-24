package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import io.github.fourlastor.game.ui.PlayerActor;

/** Request to create a Player. */
public class PlayerRequest implements Component {
    public final Camera camera;
    public final PlayerActor actor;

    public PlayerRequest(Camera camera, PlayerActor actor) {
        this.camera = camera;
        this.actor = actor;
    }
}
