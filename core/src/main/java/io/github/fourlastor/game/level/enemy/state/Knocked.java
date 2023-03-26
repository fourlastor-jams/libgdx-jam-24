package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.physics.BodyHelper;

public class Knocked extends EnemyState {

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();
    private final BodyHelper helper;
    private final SoundController soundController;
    private final AssetManager assetManager;

    private float timer;

    @AssistedInject
    public Knocked(@Assisted ImmutableArray<Entity> players, Dependencies mappers, BodyHelper helper, SoundController soundController, AssetManager assetManager) {
        super(mappers, players);
        this.helper = helper;
        this.soundController = soundController;
        this.assetManager = assetManager;
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        timer = 0;
        soundController.play(assetManager.get("audio/sounds/enemies/hurt.wav", Sound.class), .3f);
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        Actor actor = actors.get(entity).actor;
        float percent;
        Color start;
        Color end;
        if (timer <= 0.2) {
            percent = Math.min(1, timer / 0.2f);
            start = Color.WHITE;
            end = Color.RED;
        } else {
            start = Color.RED;
            end = Color.WHITE;
            percent = Math.min(1, (timer - 0.2f) / 0.2f);
        }
        Color color = actor.getColor();
        if (percent == 0) color.set(start);
        else if (percent == 1) color.set(end);
        else {
            float r = start.r + (end.r - start.r) * percent;
            float g = start.g + (end.g - start.g) * percent;
            float b = start.b + (end.b - start.b) * percent;
            float a = start.a + (end.a - start.a) * percent;
            color.set(r, g, b, a);
        }
        timer += delta();
        if (timer > 0.6f) {
            Enemy enemy = enemies.get(entity);
            enemy.stateMachine.changeState(enemy.alive);
            return;
        }
        Body body = bodies.get(entity).body;
        if (timer > 0.3f) {
            if (!body.getLinearVelocity().equals(Vector2.Zero)) {
                body.setLinearVelocity(0, 0);
            }
            return;
        }
        Vector2 position = body.getPosition();
        Vector2 playerPosition = findClosestPlayer();
        targetVelocity.set(position).sub(playerPosition).nor().scl(2f);
        body.applyLinearImpulse(helper.velocityAsImpulse(body, targetVelocity, impulse), body.getWorldCenter(), false);
    }

    private Vector2 findClosestPlayer() {
        return bodies.get(players.get(0)).body.getPosition();
    }

    @AssistedFactory
    public interface Factory {
        Knocked create(ImmutableArray<Entity> players);
    }
}
