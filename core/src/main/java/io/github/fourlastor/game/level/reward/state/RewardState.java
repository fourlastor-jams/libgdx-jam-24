package io.github.fourlastor.game.level.reward.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Reward;
import io.github.fourlastor.game.level.physics.BodyHelper;
import javax.inject.Inject;

public abstract class RewardState implements State<Entity> {

    protected final ComponentMapper<BodyComponent> bodies;
    protected final Entity player;
    protected final ComponentMapper<Reward.State> rewards;
    protected final BodyHelper helper;

    public static class Dependencies {

        final ComponentMapper<BodyComponent> bodies;
        final ComponentMapper<Reward.State> rewards;
        final BodyHelper bodyHelper;

        @Inject
        public Dependencies(
                ComponentMapper<BodyComponent> bodies, ComponentMapper<Reward.State> rewards, BodyHelper bodyHelper) {
            this.bodies = bodies;
            this.rewards = rewards;
            this.bodyHelper = bodyHelper;
        }
    }

    private float delta;

    public RewardState(Dependencies dependencies, Entity player) {
        this.rewards = dependencies.rewards;
        this.bodies = dependencies.bodies;
        this.helper = dependencies.bodyHelper;
        this.player = player;
    }

    public final void setDelta(float delta) {
        this.delta = delta;
    }

    protected final float delta() {
        return delta;
    }

    @Override
    public void enter(Entity entity) {}

    @Override
    public void update(Entity entity) {}

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram msg) {
        return false;
    }
}
