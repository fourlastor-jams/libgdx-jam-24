package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.physics.BodyHelper;

public class Knocked extends EnemyState {

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();
    private final BodyHelper helper;

    private float timer;

    @AssistedInject
    public Knocked(@Assisted ImmutableArray<Entity> players, Dependencies mappers, BodyHelper helper) {
        super(mappers, players);
        this.helper = helper;
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        Actor actor = actors.get(entity).actor;
        actor.clearActions();
        actor.addAction(Actions.sequence(
                Actions.color(Color.RED, 0.2f),
                Actions.color(Color.WHITE, 0.2f)
        ));
        timer = 0;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
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
