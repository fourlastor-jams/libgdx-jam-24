package io.github.fourlastor.game.level.weapon.whip;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Whip;
import io.github.fourlastor.game.level.weapon.whip.state.Disabled;
import io.github.fourlastor.game.level.weapon.whip.state.Enabled;

import javax.inject.Inject;
import javax.inject.Provider;

public class WhipSystem extends IteratingSystem implements Telegraph {

    private static final Family FAMILY =
            Family.all(BodyComponent.class, ActorComponent.class, Whip.class).get();
    private static final Family FAMILY_SETUP = Family.all(Whip.Request.class).get();
    private final ComponentMapper<Whip> whips;
    private final SetupListener setupListener;
    private final MessageDispatcher dispatcher;

    @Inject
    public WhipSystem(ComponentMapper<Whip> whips, SetupListener setupListener, MessageDispatcher dispatcher) {
        super(FAMILY);
        this.whips = whips;
        this.setupListener = setupListener;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        whips.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY_SETUP, setupListener);
        dispatcher.addListener(this, Message.GAME_OVER.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(setupListener);
        super.removedFromEngine(engine);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.GAME_OVER.ordinal()) {
            setProcessing(false);
            return true;
        }
        return false;
    }

    public static class SetupListener implements EntityListener {

        private final Provider<Enabled> enabledFactory;
        private final Provider<Disabled> disabledFactory;
        private final WhipStateMachine.Factory stateMachineFactory;

        @Inject
        public SetupListener(
                Provider<Enabled> enabledFactory,
                Provider<Disabled> disabledFactory,
                WhipStateMachine.Factory stateMachineFactory) {
            this.enabledFactory = enabledFactory;
            this.disabledFactory = disabledFactory;
            this.stateMachineFactory = stateMachineFactory;
        }

        @Override
        public void entityAdded(Entity entity) {
            Whip.Request request = entity.remove(Whip.Request.class);
            Enabled enabled = enabledFactory.get();
            Disabled disabled = disabledFactory.get();
            WhipStateMachine stateMachine = stateMachineFactory.create(entity, disabled);
            entity.add(new Whip(stateMachine, enabled, disabled, request.front, request.back, request.top, request.bottom));
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
