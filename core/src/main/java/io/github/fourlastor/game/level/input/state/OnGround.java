package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ObjectSet;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.physics.BodyHelper;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

import javax.inject.Inject;

public class OnGround extends PlayerState {

    private final BodyHelper helper;
    private final ObjectSet<Entity> enemiesHitting = new ObjectSet<>();
    private final SoundController soundController;
    private final AssetManager assetManager;

    private float hitTimer = 0f;

    @Inject
    public OnGround(Mappers mappers, BodyHelper helper, SoundController soundController, AssetManager assetManager) {
        super(mappers);
        this.helper = helper;
        this.soundController = soundController;
        this.assetManager = assetManager;
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
        player.actor.setBleeding(enemiesHitting.notEmpty());
        if (hitTimer >= 1) {
            hitTimer = 0f;
            for (Entity enemy : enemiesHitting) {
                float damage = enemies.get(enemy).type.damage;
                player.hp -= damage;
                player.hp = Math.max(0f, player.hp);
                soundController.play(assetManager.get("audio/sounds/player/hurt.wav", Sound.class), .1f);
            }
            if (enemiesHitting.notEmpty()) {
                player.actor.clearActions();
                player.actor.addAction(Actions.sequence(
                        Actions.color(Color.RED, .1f),
                        Actions.color(Color.WHITE, .1f)
                ));
            }
        }
        boolean wasStationary = targetVelocity.isZero();
        targetVelocity.x = 0;
        targetVelocity.y = 0;
        if (isLeftPressed()) {
            targetVelocity.x -= 1;
        }
        if (isRightPressed()) {
            targetVelocity.x += 1;
        }
        if (isDownPressed()) {
            targetVelocity.y -= 1;
        }
        if (isUpPressed()) {
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
        if (player.hp <= 0) {
            player.stateMachine.changeState(player.dead);
        }
    }

    private boolean isUpPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
    }

    private boolean isDownPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);
    }

    private boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
    }

    private boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        if (telegram.message == Message.PLAYER_HIT.ordinal()) {
            Entity enemy = (Entity) telegram.extraInfo;
            enemiesHitting.add(enemy);
            hitTimer = 1f;
            return true;
        } else if (telegram.message == Message.PLAYER_HIT_END.ordinal()) {
            Entity enemy = (Entity) telegram.extraInfo;
            enemiesHitting.remove(enemy);
            return true;
        }
        return super.onMessage(entity, telegram);
    }
}
