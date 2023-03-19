package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.BodyBuilderComponent;
import io.github.fourlastor.game.level.component.PlayerRequest;
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

        Image image = new Image(textureAtlas.findRegion("whitePixel"));

        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(4.5f, 1.5f));
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 0.5f);
            Fixture fixture = body.createFixture(shape, 0.0f);
            fixture.setFriction(100f);
            fixture.setRestitution(0.15f);
            fixture.setUserData(UserData.PLAYER);
            shape.dispose();
            return body;
        }));
        entity.add(new ActorComponent(image, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequest(camera));
        return entity;
    }
}
