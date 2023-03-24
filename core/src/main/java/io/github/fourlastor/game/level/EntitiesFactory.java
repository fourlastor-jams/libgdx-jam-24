package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.Animated;
import io.github.fourlastor.game.level.component.BodyBuilderComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.component.HpBar;
import io.github.fourlastor.game.level.component.PlayerRequest;
import io.github.fourlastor.game.level.component.Whip;
import io.github.fourlastor.game.level.enemy.EnemyType;
import io.github.fourlastor.game.level.physics.BodyData;
import io.github.fourlastor.game.ui.Bar;
import io.github.fourlastor.game.ui.ParallaxImage;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.GdxAnimation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import squidpony.squidmath.SilkRNG;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private static final float SCALE = 1f / 32f;
    private final TextureAtlas textureAtlas;
    private final Camera camera;
    private final Map<String, Array<TextureRegionDrawable>> enemyRegions = new HashMap<>();
    private final SilkRNG random;

    @Inject
    public EntitiesFactory(TextureAtlas textureAtlas, Camera camera, SilkRNG random) {
        this.textureAtlas = textureAtlas;
        this.camera = camera;
        this.random = random;
    }

    public Entity hpBar(Entity player) {
        Entity entity = new Entity();
        TextureRegionDrawable whitePixel = new TextureRegionDrawable(textureAtlas.findRegion("whitePixel"));

        Bar bar = new Bar(whitePixel.tint(Color.RED), whitePixel.tint(Color.BLACK));
        bar.setSize(20, 3);
        bar.setScale(SCALE);
        bar.setAmount(0.7f);
        entity.add(new ActorComponent(bar, ActorComponent.Layer.CHARACTER));
        entity.add(new HpBar(bar, player));
        return entity;
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

        Image whipActor = new Image(textureAtlas.findRegion("character/whip"));
        float whipW = whipActor.getWidth();
        float whipH = whipActor.getHeight();
        whipActor.setSize(whipW, whipH);
        int whipX = 24;
        int whipY = 4;
        whipActor.setPosition(whipX, whipY);

        Group group = new Group();
        group.setScale(SCALE);
        group.setOrigin(Align.left);
        float playerWidth = image.getWidth();
        float playerHeight = image.getHeight();
        group.setSize(playerWidth, playerHeight);
        group.addActor(image);
        group.addActor(whipActor);
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(new Vector2(4.5f, 1.5f));
            Body body = world.createBody(bodyDef);
            body.setUserData(entity);
            CircleShape shape = new CircleShape();
            shape.setRadius(0.25f);
            FixtureDef def = new FixtureDef();
            def.filter.categoryBits = BodyData.Category.PLAYER.bits;
            def.filter.maskBits = BodyData.Mask.PLAYER.bits;
            def.shape = shape;
            body.createFixture(def).setUserData(BodyData.Type.PLAYER);
            PolygonShape whipShape = new PolygonShape();
            FixtureDef whipDef = new FixtureDef();
            whipShape.setAsBox(43f * SCALE / 2, 2f * SCALE / 2, new Vector2(-19f * SCALE / 2, 18f * SCALE), 0f);
            float hx = whipW * SCALE / 2f;
            float hy = whipH * SCALE / 2f;
            whipShape.setAsBox(
                    hx,
                    hy,
                    new Vector2((whipX - playerWidth / 2) * SCALE + hx, (whipY - playerHeight / 2) * SCALE + hy),
                    0f);
            whipDef.shape = whipShape;
            whipDef.isSensor = true;
            whipDef.filter.categoryBits = BodyData.Category.WEAPON.bits;
            whipDef.filter.maskBits = BodyData.Mask.WEAPON.bits;
            body.createFixture(whipDef).setUserData(BodyData.Type.WEAPON_R);
            whipShape.setAsBox(
                    hx,
                    hy,
                    new Vector2((-whipX + playerWidth / 2) * SCALE - hx, (whipY - playerHeight / 2) * SCALE + hy),
                    0f);
            body.createFixture(whipDef).setUserData(BodyData.Type.WEAPON_L);
            whipShape.dispose();
            shape.dispose();
            return body;
        }));
        entity.add(new ActorComponent(group, ActorComponent.Layer.CHARACTER));
        entity.add(new PlayerRequest(camera));
        entity.add(new Animated(image));
        entity.add(new Whip.Request(whipActor));
        return entity;
    }

    public Entity bg() {
        Entity entity = new Entity();
        Actor actor = new ParallaxImage(Objects.requireNonNull(textureAtlas.findRegion("ground/grass")), 1f);
        actor.setPosition(-50, -50);
        actor.setScale(SCALE);
        entity.add(new ActorComponent(actor, ActorComponent.Layer.BG));
        return entity;
    }

    public Entity enemy(Vector2 position, EnemyType type) {
        Entity entity = new Entity();

        float period = type.frameDuration + random.nextFloat() * -type.frameDuration / 2f;
        Animation<TextureRegionDrawable> animation =
                new GdxAnimation<>(period, enemyWalk(type.animationPath), Animation.PlayMode.LOOP_PING_PONG);

        Image image = new AnimatedImage(animation);
        image.setScale(SCALE);
        entity.add(new ActorComponent(image, ActorComponent.Layer.ENEMIES));
        entity.add(new Enemy.Request());
        entity.add(new BodyBuilderComponent(world -> {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(position);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bodyDef);
            body.setUserData(entity);
            CircleShape shape = new CircleShape();
            shape.setRadius(0.2f);
            FixtureDef def = new FixtureDef();
            def.filter.categoryBits = BodyData.Category.ENEMY.bits;
            def.filter.maskBits = BodyData.Mask.ENEMY.bits;
            def.shape = shape;
            body.createFixture(def).setUserData(BodyData.Type.ENEMY);
            shape.dispose();
            return body;
        }));
        return entity;
    }

    private Array<TextureRegionDrawable> enemyWalk(String basePath) {
        String path = "enemy/" + basePath + "/walking";
        if (!enemyRegions.containsKey(path)) {
            Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(path);
            Array<TextureRegionDrawable> drawables = new Array<>(regions.size);
            for (TextureAtlas.AtlasRegion region : regions) {
                drawables.add(new TextureRegionDrawable(region));
            }
            enemyRegions.put(path, drawables);
        }

        return enemyRegions.get(path);
    }
}
