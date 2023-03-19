package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Player;
import javax.inject.Inject;

/**
 * Moves platforms down when the player goes up
 */
public class CameraMovementSystem extends IteratingSystem {

    private static final Family FAMILY_PLAYER =
            Family.all(Player.class, BodyComponent.class).get();
    private final ComponentMapper<BodyComponent> bodies;
    private final ComponentMapper<Player> players;

    @Inject
    public CameraMovementSystem(ComponentMapper<BodyComponent> bodies, ComponentMapper<Player> players) {
        super(FAMILY_PLAYER);
        this.players = players;
        this.bodies = bodies;
    }

    private final Vector2 cameraPos = new Vector2();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 center = bodies.get(entity).body.getPosition();
        Camera camera = players.get(entity).camera;
        cameraPos.set(camera.position.x, camera.position.y);
        center.sub(cameraPos).scl(deltaTime * 8);
        camera.position.x += center.x;
        camera.position.y += center.y;
    }
}
