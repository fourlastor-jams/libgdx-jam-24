package io.github.fourlastor.game.level.reward;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.component.Reward;
import io.github.fourlastor.game.level.reward.state.Away;
import io.github.fourlastor.game.level.reward.state.Chasing;
import io.github.fourlastor.game.level.reward.state.Inactive;
import javax.inject.Inject;

public class RewardMovementSystem extends IteratingSystem implements Telegraph {

    private static final Family FAMILY =
            Family.all(Reward.State.class, BodyComponent.class).get();
    private static final Family FAMILY_SETUP =
            Family.all(Reward.class).exclude(Reward.State.class).get();
    private static final Family FAMILY_PLAYER =
            Family.all(Player.class, BodyComponent.class).get();
    private final MessageDispatcher dispatcher;
    private final ComponentMapper<Reward.State> rewards;
    private final RewardStateMachine.Factory stateMachineFactory;
    private final Away.Factory awayFactory;
    private final Chasing.Factory chasingFactory;
    private final Inactive.Factory inactiveFactory;
    private ImmutableArray<Entity> playerEntities;
    private final SetupListener setupListener = new SetupListener();

    @Inject
    public RewardMovementSystem(
            MessageDispatcher dispatcher,
            ComponentMapper<Reward.State> rewards,
            RewardStateMachine.Factory stateMachineFactory,
            Away.Factory awayFactory,
            Chasing.Factory chasingFactory,
            Inactive.Factory inactiveFactory) {
        super(FAMILY);
        this.dispatcher = dispatcher;
        this.rewards = rewards;
        this.stateMachineFactory = stateMachineFactory;
        this.awayFactory = awayFactory;
        this.chasingFactory = chasingFactory;
        this.inactiveFactory = inactiveFactory;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        playerEntities = engine.getEntitiesFor(FAMILY_PLAYER);
        dispatcher.addListener(this, Message.GAME_OVER.ordinal());
        engine.addEntityListener(FAMILY_SETUP, setupListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(setupListener);
        playerEntities = null;
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        if (playerEntities.size() <= 0) {
            return;
        }
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        rewards.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.GAME_OVER.ordinal()) {
            setProcessing(false);
            return true;
        }
        return false;
    }

    private class SetupListener implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            Entity playerEntity = playerEntities.get(0);
            Inactive inactive = inactiveFactory.create(playerEntity);
            Away away = awayFactory.create(playerEntity);
            Chasing chasing = chasingFactory.create(playerEntity);
            entity.add(new Reward.State(stateMachineFactory.create(entity, inactive), away, chasing, inactive));
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
