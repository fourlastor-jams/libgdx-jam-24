package io.github.fourlastor.game.level.weapon.whip.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Whip;
import io.github.fourlastor.game.level.physics.BodyData;
import io.github.fourlastor.game.level.physics.BodyHelper;
import javax.inject.Inject;

public abstract class WhipState implements State<Entity> {

    private final ComponentMapper<ActorComponent> actors;
    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<Whip> whips;
    private final BodyHelper bodyHelper;

    public static class Dependencies {

        final ComponentMapper<ActorComponent> actors;
        final ComponentMapper<BodyComponent> bodies;
        final ComponentMapper<Whip> whips;

        final BodyHelper bodyHelper;

        @Inject
        public Dependencies(
                ComponentMapper<ActorComponent> actors,
                ComponentMapper<BodyComponent> bodies,
                ComponentMapper<Whip> whips,
                BodyHelper bodyHelper) {
            this.actors = actors;
            this.bodies = bodies;
            this.whips = whips;
            this.bodyHelper = bodyHelper;
        }
    }

    private float delta;
    private float timer;

    public WhipState(Dependencies dependencies) {
        this.actors = dependencies.actors;
        this.whips = dependencies.whips;
        this.bodies = dependencies.bodies;
        this.bodyHelper = dependencies.bodyHelper;
    }

    public final void setDelta(float delta) {
        this.delta = delta;
    }

    protected final float delta() {
        return delta;
    }

    @Override
    public void enter(Entity entity) {
        timer = 0f;
    }

    private void updateHitBoxes(Body body, boolean flipped) {
        for (Fixture fixture : body.getFixtureList()) {
            Object type = fixture.getUserData();
            if (type != BodyData.Type.WEAPON_L && type != BodyData.Type.WEAPON_R) {
                continue;
            }
            BodyData.Type target = flipped ? BodyData.Type.WEAPON_L : BodyData.Type.WEAPON_R;
            BodyData.Mask mask = canCollide() && target == type ? BodyData.Mask.WEAPON : BodyData.Mask.DISABLED;
            bodyHelper.updateFilterData(fixture, mask);
        }
    }

    @Override
    public void update(Entity entity) {
        Whip whip = whips.get(entity);
        boolean flipped = actors.get(entity).actor.getScaleX() < 0;
        whip.actor.setVisible(canCollide());
        updateHitBoxes(bodies.get(entity).body, flipped);
        timer += delta();
        if (timer >= timer()) {
            whip.stateMachine.changeState(nextState(whip));
        }
    }

    protected abstract float timer();

    protected abstract boolean canCollide();

    protected abstract WhipState nextState(Whip whip);

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
