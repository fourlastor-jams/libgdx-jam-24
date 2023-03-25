package io.github.fourlastor.game.level.reward.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.fourlastor.game.level.component.Reward;

abstract class Movement extends RewardState {

    private float timer = 0f;

    public Movement(Entity player, Dependencies mappers) {
        super(mappers, player);
    }

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        timer = 0f;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        timer += delta();
        if (timer >= 0.3) {
            Reward.State state = rewards.get(entity);
            state.stateMachine.changeState(state.chasing);
            return;
        }
        Body body = bodies.get(entity).body;
        Vector2 position = body.getPosition();
        Vector2 playerPosition = bodies.get(player).body.getPosition();
        targetVelocity.set(playerPosition).sub(position).nor().scl(20f).scl(chasing() ? 1 : -1);
        body.applyForceToCenter(helper.velocityAsImpulse(body, targetVelocity, impulse), false);
    }

    protected abstract boolean chasing();
}
