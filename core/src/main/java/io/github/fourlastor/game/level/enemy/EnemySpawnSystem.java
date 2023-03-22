package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.component.EnemyAi;
import javax.inject.Inject;
import squidpony.squidmath.Noise;
import squidpony.squidmath.SilkRNG;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family ENEMY_FAMILY = Family.all(EnemyAi.class).get();

    private final Camera camera;
    private final EntitiesFactory factory;
    private final SilkRNG random;
    private final Noise.Noise3D noise;

    private float totalTime = 0f;
    private float runTime = 0f;
    private ImmutableArray<Entity> entities;

    @Inject
    public EnemySpawnSystem(Camera camera, EntitiesFactory factory, SilkRNG random, Noise.Noise3D noise) {
        this.camera = camera;
        this.factory = factory;
        this.random = random;
        this.noise = noise;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entities = engine.getEntitiesFor(ENEMY_FAMILY);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        totalTime += deltaTime;
        runTime += deltaTime;
        if (entities.size() < 300 && runTime > 2f) {
            runTime = 0f;
            spawnEnemies();
        }
    }

    private void spawnEnemies() {
        Engine engine = getEngine();

        float width = camera.viewportWidth;
        float height = camera.viewportHeight;
        float left = camera.position.x - width / 2;
        float right = left + width;
        float bottom = camera.position.y - height / 2;
        float top = bottom + height;
        for (float x = left - width; x < right + width; x += 5) {
            if (shouldSpawn(x, bottom - height)) {
                Entity enemy = factory.enemy(new Vector2(x, bottom - height), randomType());
                engine.addEntity(enemy);
            }
            if (shouldSpawn(x, top + height)) {
                Entity enemy = factory.enemy(new Vector2(x, top + height), randomType());
                engine.addEntity(enemy);
            }
        }
        for (float y = bottom - height; y < top + height; y += 5) {
            if (shouldSpawn(left - width, y)) {
                Entity enemy = factory.enemy(new Vector2(left - width, y), randomType());
                engine.addEntity(enemy);
            }
            if (shouldSpawn(right + width, y)) {
                Entity enemy = factory.enemy(new Vector2(left - width, y), randomType());
                engine.addEntity(enemy);
            }
        }
    }

    private boolean shouldSpawn(float x, float y) {
        return Math.abs(noise.getNoise(x, y, totalTime)) > 0.2;
    }

    private EnemyType randomType() {
        if (random.nextBoolean()) {
            return EnemyType.PIGEON_0;
        } else {
            return EnemyType.PIGEON_1;
        }
    }

    private Vector2 randomLocation() {
        boolean horizontalSpawn = random.nextBoolean();
        boolean atStart = random.nextBoolean();
        float gradient = random.nextFloat();
        float left = camera.position.x - camera.viewportWidth / 2;
        float right = left + camera.viewportWidth;
        float bottom = camera.position.y - camera.viewportHeight / 2;
        float top = bottom + camera.viewportHeight;
        float x;
        float y;
        if (horizontalSpawn) {
            x = camera.viewportWidth * gradient + left;
            y = atStart ? bottom : top;
        } else {
            x = atStart ? left : right;
            y = camera.viewportHeight * gradient + bottom;
        }
        return new Vector2(x, y);
    }
}
