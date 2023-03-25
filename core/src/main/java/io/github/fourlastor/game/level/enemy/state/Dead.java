package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.physics.BodyData;
import io.github.fourlastor.game.level.physics.BodyHelper;

public class Dead extends EnemyState {

    private float timer;
    private float blackTimer;
    private boolean black;
    private final ComponentMapper<ActorComponent> actors;
    private final BodyHelper helper;
    private final AssetManager assetManager;
    private final SoundController soundController;
    private Sound deathSound;

    @AssistedInject
    public Dead(
            @Assisted ImmutableArray<Entity> players,
            Dependencies mappers,
            ComponentMapper<ActorComponent> actors,
            BodyHelper helper, AssetManager assetManager, SoundController soundController) {
        super(mappers, players);
        this.actors = actors;
        this.helper = helper;
        this.assetManager = assetManager;
        this.soundController = soundController;
        deathSound = assetManager.get("audio/sounds/enemies/death/pigeon 0.wav");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        timer = 0f;
        blackTimer = 0f;
        soundController.play(deathSound);

        Body body = bodies.get(entity).body;
        body.setLinearVelocity(0, 0);
        for (Fixture fixture : body.getFixtureList()) {
            helper.updateFilterData(fixture, BodyData.Mask.DISABLED);
        }
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        timer += delta();
        blackTimer += delta();

        if (timer > 1) {
            entity.add(new Enemy.Delete());
            return;
        }

        Actor actor = actors.get(entity).actor;
        if (blackTimer >= 0.16f) {
            blackTimer = 0;
            if (black) {
                actor.setColor(Color.WHITE);
            } else {
                actor.setColor(Color.BLACK);
            }
            black = !black;
        }
    }

    @AssistedFactory
    public interface Factory {
        Dead create(ImmutableArray<Entity> players);
    }
}
