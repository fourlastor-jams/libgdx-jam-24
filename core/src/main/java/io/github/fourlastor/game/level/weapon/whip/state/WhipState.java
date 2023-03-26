package io.github.fourlastor.game.level.weapon.whip.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.component.Whip;
import io.github.fourlastor.game.level.physics.BodyData;
import io.github.fourlastor.game.level.physics.BodyHelper;
import javax.inject.Inject;

public abstract class WhipState implements State<Entity> {

    private final ComponentMapper<ActorComponent> actors;
    protected final ComponentMapper<BodyComponent> bodies;
    protected final ComponentMapper<Whip> whips;
    protected final ComponentMapper<Player> players;
    private final BodyHelper bodyHelper;

    public static class Dependencies {

        final ComponentMapper<ActorComponent> actors;
        final ComponentMapper<BodyComponent> bodies;
        final ComponentMapper<Whip> whips;
        final ComponentMapper<Player> players;

        final BodyHelper bodyHelper;

        @Inject
        public Dependencies(
                ComponentMapper<ActorComponent> actors,
                ComponentMapper<BodyComponent> bodies,
                ComponentMapper<Whip> whips,
                ComponentMapper<Player> players,
                BodyHelper bodyHelper) {
            this.actors = actors;
            this.bodies = bodies;
            this.whips = whips;
            this.players = players;
            this.bodyHelper = bodyHelper;
        }
    }

    private float delta;
    private float timer;

    public WhipState(Dependencies dependencies) {
        actors = dependencies.actors;
        whips = dependencies.whips;
        bodies = dependencies.bodies;
        bodyHelper = dependencies.bodyHelper;
        players = dependencies.players;
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

    private void updateHitBoxes(Body body, boolean flipped, Player player) {
        for (Fixture fixture : body.getFixtureList()) {
            Object userData = fixture.getUserData();
            if (!isWeapon(userData)) {
                continue;
            }
            BodyData.Type fixtureType = (BodyData.Type) userData;
            BodyData.Type currentFront = flipped ? BodyData.Type.WEAPON_FRONT : BodyData.Type.WEAPON_BACK;
            boolean currentCanCollide;
            switch (fixtureType) {
                case WEAPON_FRONT:
                case WEAPON_BACK:
                    currentCanCollide = (frontIsActive() && currentFront == fixtureType) || backIsActive(player);
                    break;
                case WEAPON_TOP:
                    currentCanCollide = topIsActive(player);
                    break;
                case WEAPON_BOTTOM:
                    currentCanCollide = bottomIsActive(player);
                    break;
                default:
                    currentCanCollide = false;
            }
            BodyData.Mask mask = currentCanCollide ? BodyData.Mask.WEAPON : BodyData.Mask.DISABLED;
            bodyHelper.updateFilterData(fixture, mask);
        }
    }

    private boolean isWeapon(Object type) {
        return type == BodyData.Type.WEAPON_FRONT
                || type == BodyData.Type.WEAPON_BACK
                || type == BodyData.Type.WEAPON_TOP
                || type == BodyData.Type.WEAPON_BOTTOM;
    }

    @Override
    public void update(Entity entity) {
        Whip whip = whips.get(entity);
        boolean playerFlipped = actors.get(entity).actor.getScaleX() < 0;
        Player player = players.get(entity);
        whip.front.setVisible(frontIsActive());
        whip.back.setVisible(backIsActive(player));
        whip.top.setVisible(topIsActive(player));
        whip.bottom.setVisible(bottomIsActive(player));
        updateHitBoxes(bodies.get(entity).body, playerFlipped, player);
        timer += delta();
        if (timer >= timer(entity)) {
            whip.stateMachine.changeState(nextState(whip));
        }
    }

    private boolean frontIsActive() {
        return canCollide();
    }

    private boolean backIsActive(Player player) {
        return player.hasPowerUp(Player.PowerUp.BACK_ATTACK) && canCollide();
    }

    private boolean topIsActive(Player player) {
        return player.hasPowerUp(Player.PowerUp.TOP_ATTACK) && canCollide();
    }

    private boolean bottomIsActive(Player player) {
        return player.hasPowerUp(Player.PowerUp.BOTTOM_ATTACK) && canCollide();
    }

    protected abstract float timer(Entity entity);

    protected abstract boolean canCollide();

    protected abstract WhipState nextState(Whip whip);

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
