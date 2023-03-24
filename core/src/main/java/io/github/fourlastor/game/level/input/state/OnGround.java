package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.physics.BodyHelper;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class OnGround extends PlayerState {

    private final BodyHelper helper;

    @Inject
    public OnGround(Mappers mappers, BodyHelper helper) {
        super(mappers);
        this.helper = helper;
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
    }

    private final Vector2 targetVelocity = new Vector2();

    @Override
    public void update(Entity entity) {
        super.update(entity);
        Body body = bodies.get(entity).body;
        Player player = players.get(entity);
        AnimatedImage animation = animated.get(entity).animation;
        Actor actor = actors.get(entity).actor;
        boolean wasStationary = targetVelocity.isZero();
        targetVelocity.x = 0;
        targetVelocity.y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            targetVelocity.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            targetVelocity.x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            targetVelocity.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            targetVelocity.y += 1;
        }
        boolean isStationary = targetVelocity.isZero();
        animation.setPlaying(!isStationary);
        if (targetVelocity.x != 0) {
            actor.setScaleX(Math.abs(actor.getScaleX()) * Math.signum(targetVelocity.x));
        }
        if (isStationary != wasStationary) {
            player.movementTime = 0f;
        }
        player.movementTime += delta();
        Vector2 accelerated = helper.accelerate(
                player.movementTime, player.settings.accelerationTime, player.settings.speed, targetVelocity);
        body.setLinearVelocity(accelerated);
    }
}
