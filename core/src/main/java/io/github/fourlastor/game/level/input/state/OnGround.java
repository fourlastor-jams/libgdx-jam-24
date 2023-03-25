package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectSet;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.physics.BodyHelper;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class OnGround extends PlayerState {

    private final BodyHelper helper;
    private final ObjectSet<Entity> enemiesHitting = new ObjectSet<>();

    private float hitTimer = 0f;

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
        hitTimer += delta();
        Body body = bodies.get(entity).body;
        Player player = players.get(entity);
        AnimatedImage animation = animated.get(entity).animation;
        Actor actor = actors.get(entity).actor;
        if (hitTimer >= 1) {
            hitTimer = 0f;
            for (Entity enemy : enemiesHitting) {
                float damage = enemies.get(enemy).type.damage;
                player.hp -= damage;
            }
        }
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

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        if (telegram.message == Message.PLAYER_HIT.ordinal()) {
            Entity enemy = (Entity) telegram.extraInfo;
            enemiesHitting.add(enemy);
            return true;
        } else if (telegram.message == Message.PLAYER_HIT_END.ordinal()) {
            Entity enemy = (Entity) telegram.extraInfo;
            enemiesHitting.remove(enemy);
            return true;
        }
        return super.onMessage(entity, telegram);
    }
}
