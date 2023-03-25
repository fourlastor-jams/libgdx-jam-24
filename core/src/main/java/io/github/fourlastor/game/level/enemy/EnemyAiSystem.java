package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.enemy.state.Alive;
import io.github.fourlastor.game.level.enemy.state.Dead;
import io.github.fourlastor.game.level.enemy.state.Knocked;
import io.github.fourlastor.game.level.reward.RewardType;
import java.util.Set;
import javax.inject.Inject;
import squidpony.squidmath.SilkRNG;

public class EnemyAiSystem extends IteratingSystem {

    private static final Family ENEMY_FAMILY =
            Family.all(BodyComponent.class, Enemy.class).get();
    private static final Family PLAYER_FAMILY =
            Family.all(BodyComponent.class, Player.class).get();
    private static final Family REQUEST_FAMILY = Family.all(Enemy.Request.class).get();
    private static final Family DELETE_FAMILY = Family.all(Enemy.Delete.class).get();
    private final ComponentMapper<Enemy> enemies;
    private final ComponentMapper<BodyComponent> bodies;
    private final ComponentMapper<Player> playerComponentMapper;
    private final MessageDispatcher dispatcher;

    private final Alive.Factory aliveFactory;
    private final Dead.Factory deadFactory;
    private final Knocked.Factory knockedFactory;
    private final EnemyStateMachine.Factory stateMachineFactory;
    private final SilkRNG random;
    private final EntitiesFactory entitiesFactory;

    private final SetupListener setupListener = new SetupListener();
    private final CleanupListener cleanupListener = new CleanupListener();

    private final DeleteListener deleteListener = new DeleteListener();
    private ImmutableArray<Entity> players;

    @Inject
    public EnemyAiSystem(
            ComponentMapper<Enemy> enemies,
            ComponentMapper<BodyComponent> bodies,
            ComponentMapper<Player> playerComponentMapper,
            MessageDispatcher dispatcher,
            Alive.Factory aliveFactory,
            Dead.Factory deadFactory,
            Knocked.Factory knockedFactory,
            EnemyStateMachine.Factory stateMachineFactory,
            SilkRNG random,
            EntitiesFactory entitiesFactory) {
        super(ENEMY_FAMILY);
        this.enemies = enemies;
        this.bodies = bodies;
        this.playerComponentMapper = playerComponentMapper;
        this.dispatcher = dispatcher;
        this.aliveFactory = aliveFactory;
        this.deadFactory = deadFactory;
        this.knockedFactory = knockedFactory;
        this.stateMachineFactory = stateMachineFactory;
        this.random = random;
        this.entitiesFactory = entitiesFactory;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        players = engine.getEntitiesFor(PLAYER_FAMILY);
        engine.addEntityListener(REQUEST_FAMILY, setupListener);
        engine.addEntityListener(ENEMY_FAMILY, cleanupListener);
        engine.addEntityListener(DELETE_FAMILY, deleteListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(deleteListener);
        engine.removeEntityListener(cleanupListener);
        engine.removeEntityListener(setupListener);
        players = null;
        super.removedFromEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (players.size() == 0) {
            return;
        }
        enemies.get(entity).stateMachine.update(deltaTime);
    }

    private class SetupListener implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            Enemy.Request request = entity.remove(Enemy.Request.class);
            Alive alive = aliveFactory.create(players);
            Dead dead = deadFactory.create(players);
            Knocked knocked = knockedFactory.create(players);
            EnemyStateMachine stateMachine = stateMachineFactory.create(entity, alive);
            dispatcher.addListener(stateMachine, Message.ENEMY_HIT.ordinal());
            entity.add(new Enemy(stateMachine, alive, dead, knocked, request.type));
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }

    private class CleanupListener implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {}

        @Override
        public void entityRemoved(Entity entity) {
            EnemyStateMachine stateMachine = enemies.get(entity).stateMachine;
            dispatcher.removeListener(stateMachine);
        }
    }

    private class DeleteListener implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            Set<RewardType> rewards = enemies.get(entity).type.rewards;
            RewardType rewardType = random.getRandomElement(rewards);
            Vector2 position = bodies.get(entity).body.getPosition();
            getEngine().removeEntity(entity);
            getEngine().addEntity(entitiesFactory.reward(rewardType, position));
            Player player = playerComponentMapper.get(players.get(0));
            player.killCounter++;
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
