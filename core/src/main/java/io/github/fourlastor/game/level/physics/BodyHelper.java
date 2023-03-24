package io.github.fourlastor.game.level.physics;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import javax.inject.Inject;

public class BodyHelper {

    @Inject
    public BodyHelper() {}

    public Vector2 velocityAsImpulse(Body body, Vector2 targetVelocity, Vector2 impulse) {
        return impulse.set(body.getLinearVelocity())
                .scl(-1f)
                .add(targetVelocity)
                .scl(body.getMass());
    }

    public void updateFilterData(Fixture fixture, BodyData.Mask target) {
        Filter oldFilter = fixture.getFilterData();
        if (oldFilter.maskBits == target.bits) {
            return;
        }
        Filter filter = new Filter();
        copyFilter(filter, oldFilter);
        filter.maskBits = target.bits;
        fixture.setFilterData(filter);
    }

    /**
     * Filter#set doesn't exist in GWT.
     */
    private void copyFilter(Filter newFilter, Filter filter) {
        newFilter.categoryBits = filter.categoryBits;
        newFilter.maskBits = filter.maskBits;
        newFilter.groupIndex = filter.groupIndex;
    }

    public Vector2 accelerate(float movementTime, float accelerationTime, float maxSpeed, Vector2 targetVelocity) {
        float progress = Math.min(1f, movementTime / accelerationTime);
        float interpolated =
                targetVelocity.isZero() ? Interpolation.pow2.apply(1 - progress) : Interpolation.pow2.apply(progress);
        return targetVelocity.nor().scl(maxSpeed).scl(interpolated);
    }
}
