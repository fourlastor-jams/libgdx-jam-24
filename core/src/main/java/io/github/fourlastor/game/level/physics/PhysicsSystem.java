package io.github.fourlastor.game.level.physics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.BodyBuilderComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Reward;
import javax.inject.Inject;

public class PhysicsSystem extends IntervalSystem {

    private static final Family FAMILY_BUILDER =
            Family.all(BodyBuilderComponent.class).get();
    private static final Family FAMILY_BODY = Family.all(BodyComponent.class).get();
    private static final float STEP = 1f / 60f;

    private final World world;
    private final ComponentMapper<BodyBuilderComponent> bodyBuilders;
    private final ComponentMapper<BodyComponent> bodies;
    private final MessageDispatcher messageDispatcher;
    private final Factory factory;
    private final Cleaner cleaner;

    @Inject
    public PhysicsSystem(
            World world,
            ComponentMapper<BodyBuilderComponent> bodyBuilders,
            ComponentMapper<BodyComponent> bodies,
            MessageDispatcher messageDispatcher) {
        super(STEP);
        this.world = world;
        this.bodyBuilders = bodyBuilders;
        this.bodies = bodies;
        this.messageDispatcher = messageDispatcher;
        factory = new Factory();
        cleaner = new Cleaner();
    }

    @Override
    protected void updateInterval() {
        world.step(STEP, 6, 2);
    }

    @Override
    public void addedToEngine(Engine engine) {
        world.setContactListener(contactListener);
        engine.addEntityListener(FAMILY_BUILDER, factory);
        engine.addEntityListener(FAMILY_BODY, cleaner);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(factory);
        engine.removeEntityListener(cleaner);
        world.setContactListener(null);
    }

    /**
     * Creates a body in the world every time a body builder is added.
     */
    public class Factory implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            BodyBuilderComponent component = bodyBuilders.get(entity);
            Body body = component.factory.build(world);
            entity.add(new BodyComponent(body));
            entity.remove(BodyBuilderComponent.class);
        }

        @Override
        public void entityRemoved(Entity entity) {
            // nothing
        }
    }

    /**
     * Cleans up a body which has been removed from the engine.
     */
    public class Cleaner implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            // nothing
        }

        @Override
        public void entityRemoved(Entity entity) {
            BodyComponent component = bodies.get(entity);
            if (component.body != null) {
                world.destroyBody(component.body);
                component.body = null;
            }
        }
    }

    private final ContactListener contactListener = new ContactListener() {

        @Override
        public void beginContact(Contact contact) {
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();
            if (isWeapon(fixtureA) && isEnemy(fixtureB)) {
                onEnemyHit(fixtureB);
            } else if (isWeapon(fixtureB) && isEnemy(fixtureA)) {
                onEnemyHit(fixtureA);
            } else if (isPlayer(fixtureA) && isReward(fixtureB)) {
                onPickUp(fixtureB, fixtureA);
            } else if (isPlayer(fixtureB) && isReward(fixtureA)) {
                onPickUp(fixtureA, fixtureB);
            }
        }

        private void onPickUp(Fixture reward, Fixture player) {
            Entity rewardEntity = (Entity) reward.getBody().getUserData();
            Entity playerEntity = (Entity) player.getBody().getUserData();
            rewardEntity.add(new Reward.PickUp(playerEntity));
        }

        private boolean isReward(Fixture fixture) {
            Object userData = fixture.getUserData();
            return userData == BodyData.Type.REWARD;
        }

        private boolean isPlayer(Fixture fixture) {
            Object userData = fixture.getUserData();
            return userData == BodyData.Type.PLAYER;
        }

        private boolean isWeapon(Fixture fixture) {
            Object userData = fixture.getUserData();
            return userData == BodyData.Type.WEAPON_L || userData == BodyData.Type.WEAPON_R;
        }

        private boolean isEnemy(Fixture fixture) {
            return fixture.getUserData() == BodyData.Type.ENEMY;
        }

        private void onEnemyHit(Fixture fixture) {
            messageDispatcher.dispatchMessage(
                    Message.ENEMY_HIT.ordinal(), fixture.getBody().getUserData());
        }

        @Override
        public void endContact(Contact contact) {}

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {}

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {}
    };
}
