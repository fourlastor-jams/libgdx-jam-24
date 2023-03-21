package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.Animated;
import io.github.fourlastor.game.level.component.BodyBuilderComponent;
import io.github.fourlastor.game.level.component.PlayerRequest;
import io.github.fourlastor.game.level.physics.Bits;
import io.github.fourlastor.game.ui.ParallaxImage;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.GdxAnimation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private final TextureAtlas textureAtlas;
    private final Camera camera;

    @Inject
    public EntitiesFactory(TextureAtlas textureAtlas, Camera camera) {
        this.textureAtlas = textureAtlas;
        this.camera = camera;
    }

    public Entity player() {
        Entity entity = new Entity();
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions("character/walking/walking");
        Array<Drawable> drawables = new Array<>(regions.size);
        for (TextureAtlas.AtlasRegion region : regions) {
            drawables.add(new TextureRegionDrawable(region));
        }
        GdxAnimation<Drawable> animation = new GdxAnimation<>(0.15f, drawables, Animation.PlayMode.LOOP);

        AnimatedImage image = new AnimatedImage(animation);
        image.setScale(1f / 32f);

        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(new Vector2(4.5f, 1.5f));
            Body body = world.createBody(bodyDef);
            CircleShape shape = new CircleShape();
            shape.setRadius(0.5f);
            FixtureDef def = new FixtureDef();
            def.filter.categoryBits = Bits.Category.PLAYER.bits;
            def.filter.maskBits = Bits.Mask.PLAYER.bits;
            def.shape = shape;
            Fixture fixture = body.createFixture(def);
            fixture.setUserData(entity);
            shape.dispose();
            return body;
        }));
        entity.add(new ActorComponent(image, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequest(camera));
        entity.add(new Animated(image));
        return entity;
    }

    public Entity bg() {
        Entity entity = new Entity();
        Actor actor = new ParallaxImage(textureAtlas.findRegion("ground/ground"), 1f);
        actor.setPosition(-50, -50);
        entity.add(new ActorComponent(actor, ActorComponent.Layer.BG_PARALLAX));
        return entity;
    }
}
