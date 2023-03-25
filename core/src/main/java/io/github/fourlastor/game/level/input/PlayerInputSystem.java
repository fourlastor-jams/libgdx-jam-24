package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.Animated;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.component.PlayerRequest;
import io.github.fourlastor.game.level.input.state.OnGround;
import javax.inject.Inject;
import javax.inject.Provider;

public class PlayerInputSystem extends IteratingSystem {

    private static final Family FAMILY_REQUEST =
            Family.all(PlayerRequest.class, BodyComponent.class, Animated.class).get();
    private static final Family FAMILY =
            Family.all(Player.class, BodyComponent.class).get();

    private final PlayerSetup playerSetup;
    private final ComponentMapper<Player> players;

    @Inject
    public PlayerInputSystem(PlayerSetup playerSetup, ComponentMapper<Player> players) {
        super(FAMILY);
        this.playerSetup = playerSetup;
        this.players = players;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        players.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY_REQUEST, playerSetup);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(playerSetup);
        super.removedFromEngine(engine);
    }

    /**
     * Creates a player component whenever a request to set up a player is made.
     * Takes care of instantiating the state machine and the possible player states.
     */
    public static class PlayerSetup implements EntityListener {

        private final Provider<OnGround> onGroundProvider;
        private final InputStateMachine.Factory stateMachineFactory;
        private final MessageDispatcher messageDispatcher;

        @Inject
        public PlayerSetup(
                Provider<OnGround> onGroundProvider,
                InputStateMachine.Factory stateMachineFactory,
                MessageDispatcher messageDispatcher) {
            this.onGroundProvider = onGroundProvider;
            this.stateMachineFactory = stateMachineFactory;
            this.messageDispatcher = messageDispatcher;
        }

        @Override
        public void entityAdded(Entity entity) {
            Player.Settings settings = new Player.Settings(0.3f);
            PlayerRequest request = entity.remove(PlayerRequest.class);
            OnGround onGround = onGroundProvider.get();
            InputStateMachine stateMachine = stateMachineFactory.create(entity, onGround);

            entity.add(new Player(request.camera, stateMachine, onGround, settings, request.actor));
            stateMachine.getCurrentState().enter(entity);
            messageDispatcher.addListener(stateMachine, Message.PLAYER_HIT.ordinal());
            messageDispatcher.addListener(stateMachine, Message.PLAYER_HIT_END.ordinal());
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
