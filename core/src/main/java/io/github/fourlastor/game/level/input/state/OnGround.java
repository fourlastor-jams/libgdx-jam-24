package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class OnGround extends PlayerState {

    @Inject
    public OnGround(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
    }

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();

    @Override
    public void update(Entity entity) {
        super.update(entity);
        Body body = bodies.get(entity).body;
        Player player = players.get(entity);
        AnimatedImage animation = animated.get(entity).animation;
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
            animation.setScaleX(Math.abs(animation.getScaleX()) * Math.signum(targetVelocity.x));
        }
        if (isStationary != wasStationary) {
            player.movementTime = 0f;
        }
        player.movementTime += delta();
        float progress = Math.min(1f, player.movementTime / player.settings.accelerationTime);
        float interpolated =
                targetVelocity.isZero() ? Interpolation.pow2.apply(1 - progress) : Interpolation.pow2.apply(progress);
        targetVelocity.nor().scl(player.settings.speed).scl(interpolated);
        impulse.set(body.getLinearVelocity()).scl(-1f).add(targetVelocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }
}
