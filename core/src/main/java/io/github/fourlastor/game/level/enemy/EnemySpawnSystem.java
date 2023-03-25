package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.reward.RewardType;
import javax.inject.Inject;
import squidpony.squidmath.Noise;
import squidpony.squidmath.SilkRNG;

import java.util.LinkedList;

import static java.util.Arrays.asList;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family ENEMY_FAMILY = Family.all(Enemy.class).get();
    private static final float SPAWN_INTERVAL = 2f;
    private static final float PASTA_INTERVAL = 15f;

    private final LinkedList<EnemyWave> waves = new LinkedList<>(asList(
            new EnemyWave(
                    asList(EnemyType.PIGEON_0),
                    7f
            ),
            new EnemyWave(
                    asList(EnemyType.PIGEON_1),
                    15
            ),
            new EnemyWave(
                    asList(EnemyType.SATCHMO),
                    30
            )
    ));

    private EnemyWave wave = waves.poll();


    private final Camera camera;
    private final EntitiesFactory factory;
    private final SilkRNG random;
    private final Noise.Noise3D noise;

    private float totalTime = 0f;
    private float spawnTime = 0f;
    private float pastaTime = 0f;
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
        spawnTime += deltaTime;
        pastaTime += deltaTime;
        if (wave.time <= totalTime) {
            System.out.println(totalTime);
            if (!waves.isEmpty()) {
                wave = waves.poll();
            }
        }
        if (entities.size() < 300 && spawnTime > SPAWN_INTERVAL) {
            spawnTime = 0f;
            spawnEnemies();
        }
        if (pastaTime > PASTA_INTERVAL) {
            pastaTime = 0f;
            spawnPasta();
        }
    }

    private void spawnPasta() {
        getEngine().addEntity(factory.reward(RewardType.PASTA, randomLocationOutsideViewport()));
    }

    private Vector2 randomLocationOutsideViewport() {
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
        return random.getRandomElement(wave.types);
    }
}
