package io.github.fourlastor.game.level.weapon.whip;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Whip;
import io.github.fourlastor.game.level.weapon.whip.state.Disabled;
import io.github.fourlastor.game.level.weapon.whip.state.Enabled;
import javax.inject.Inject;
import javax.inject.Provider;

public class WhipSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(BodyComponent.class, ActorComponent.class, Whip.class).get();
    private static final Family FAMILY_SETUP = Family.all(Whip.Request.class).get();
    private final ComponentMapper<Whip> whips;
    private final SetupListener setupListener;

    @Inject
    public WhipSystem(ComponentMapper<Whip> whips, SetupListener setupListener) {
        super(FAMILY);
        this.whips = whips;
        this.setupListener = setupListener;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        whips.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY_SETUP, setupListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(setupListener);
        super.removedFromEngine(engine);
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
            entity.add(new Whip(stateMachine, enabled, disabled, request.actor));
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
