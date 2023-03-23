package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.physics.BodyHelper;

public class Alive extends EnemyState {

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();
    private final BodyHelper helper;

    @AssistedInject
    public Alive(@Assisted ImmutableArray<Entity> players, Dependencies mappers, BodyHelper helper) {
        super(mappers, players);
        this.helper = helper;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        Body body = bodies.get(entity).body;
        Vector2 position = body.getPosition();
        Vector2 playerPosition = findClosestPlayer();
        targetVelocity.set(playerPosition).sub(position).nor().scl(2f);
        body.applyLinearImpulse(helper.velocityAsImpulse(body, targetVelocity, impulse), body.getWorldCenter(), false);
    }

    private Vector2 findClosestPlayer() {
        return bodies.get(players.get(0)).body.getPosition();
    }

    @Override
    public boolean onMessage(Entity entity, Telegram msg) {
        if (entity != msg.extraInfo) {
            return false;
        }
        Enemy enemy = enemies.get(entity);
        enemy.health -= 10;
        if (enemy.health <= 0) {
            enemy.stateMachine.changeState(enemy.dead);
        }
        return true;
    }

    @AssistedFactory
    public interface Factory {
        Alive create(ImmutableArray<Entity> players);
    }
}
