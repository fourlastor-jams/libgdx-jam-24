package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.EnemyAi;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.physics.BodyHelper;
import javax.inject.Inject;

public class EnemyAiSystem extends IteratingSystem {

    private static final Family ENEMY_FAMILY =
            Family.all(BodyComponent.class, EnemyAi.class).get();
    private static final Family PLAYER_FAMILY =
            Family.all(BodyComponent.class, Player.class).get();
    private final ComponentMapper<BodyComponent> bodies;
    private final BodyHelper helper;

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();
    private ImmutableArray<Entity> players;

    @Inject
    public EnemyAiSystem(ComponentMapper<BodyComponent> bodies, BodyHelper helper) {
        super(ENEMY_FAMILY);
        this.bodies = bodies;
        this.helper = helper;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        players = engine.getEntitiesFor(PLAYER_FAMILY);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        players = null;
        super.removedFromEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (players.size() == 0) {
            return;
        }
        Body body = bodies.get(entity).body;
        Vector2 position = body.getPosition();
        Vector2 playerPosition = findClosestPlayer(position);
        targetVelocity.set(playerPosition).sub(position).nor().scl(2f);
        body.applyLinearImpulse(helper.velocityAsImpulse(body, targetVelocity, impulse), body.getWorldCenter(), false);
    }

    private Vector2 findClosestPlayer(Vector2 position) {
        int min = 0;
        float minDistance = Float.POSITIVE_INFINITY;
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            Vector2 playerPosition = bodies.get(player).body.getPosition();
            float distance = position.dst(playerPosition);
            if (distance < minDistance) {
                min = i;
                minDistance = distance;
            }
        }
        return bodies.get(players.get(min)).body.getPosition();
    }
}
