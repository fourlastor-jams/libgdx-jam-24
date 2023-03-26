package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.component.BodyComponent;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.reward.RewardType;
import squidpony.squidmath.SilkRNG;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family ENEMY_FAMILY = Family.all(Enemy.class).get();
    private static final float SPAWN_INTERVAL = 3f;
    private static final float PASTA_INTERVAL = 15f;
    private static final int MAX_ENEMIES_COUNT = 1000;
    private static final int SPAWN_ENEMIES_COUNT = 5;
    private static final float MAX_VIEWPORT_GARBAGE = 2f;
    private static final float END_SPAWN_LIMIT = MAX_VIEWPORT_GARBAGE - 0.1f;
    private static final float START_SPAWN_LIMIT = 1.3f;

    private final LinkedList<EnemyWave> waves = new LinkedList<>(asList(
            new EnemyWave(asList(EnemyType.PIGEON_0, EnemyType.PIGEON_1), Collections.emptyList(), 10),
            new EnemyWave(asList(EnemyType.PIGEON_0, EnemyType.PIGEON_1), asList(EnemyType.PIGEON_1), 10),
            new EnemyWave(asList(EnemyType.SATCHMO, EnemyType.SPARK), asList(EnemyType.SATCHMO, EnemyType.SPARK), 10),
            new EnemyWave(
                    asList(EnemyType.ANGRY_PINEAPPLE_0, EnemyType.ANGRY_PINEAPPLE_1, EnemyType.ANGRY_PINEAPPLE_2), asList(EnemyType.ANGRY_PINEAPPLE_0, EnemyType.ANGRY_PINEAPPLE_1, EnemyType.ANGRY_PINEAPPLE_2), 10),
            new EnemyWave(asList(EnemyType.DRAGON_QUEEN, EnemyType.RAELEUS), asList(EnemyType.DRAGON_QUEEN, EnemyType.RAELEUS), 10),
            new EnemyWave(asList(EnemyType.HYDROLIEN, EnemyType.LAVA_EATER), asList(EnemyType.HYDROLIEN, EnemyType.LAVA_EATER), 10),
            new EnemyWave(asList(EnemyType.LYZE, EnemyType.PANDA), asList(EnemyType.LYZE, EnemyType.PANDA), 10)));

    private final ComponentMapper<BodyComponent> bodies;

    private EnemyWave wave = waves.poll();
    private int currentWave = 1;

    private final Camera camera;
    private final EntitiesFactory factory;
    private final SilkRNG random;

    private float totalTime = 0f;
    private float spawnTime = 0f;
    private float pastaTime = 0f;
    private ImmutableArray<Entity> entities;
    private boolean newWave = false;

    @Inject
    public EnemySpawnSystem(ComponentMapper<BodyComponent> bodies, Camera camera, EntitiesFactory factory, SilkRNG random) {
        this.bodies = bodies;
        this.camera = camera;
        this.factory = factory;
        this.random = random;
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
        // changing waves
        if (wave.time <= totalTime) {
            totalTime = 0;
            if (!waves.isEmpty()) {
                wave = waves.poll();
                currentWave += 1;
                newWave = true;
            }
        }
        // spawning enemies
        if (entities.size() < MAX_ENEMIES_COUNT && spawnTime > SPAWN_INTERVAL) {
            spawnTime = 0f;
            spawnEnemies();
            if (newWave) {
                newWave = false;
                spawnBosses();
            }
        }
        // spawn pasta
        if (pastaTime > PASTA_INTERVAL) {
            pastaTime = 0f;
            spawnPasta();
        }
        // cleanup enemies
        for (Entity enemy : entities) {
            Vector2 position = bodies.get(enemy).body.getPosition();
            if (
                    camera.position.dst(position.x, position.y, camera.position.z)
                            > viewportRadius(camera.viewportWidth, camera.viewportHeight) * MAX_VIEWPORT_GARBAGE
            ) {
                getEngine().removeEntity(enemy);
            }
        }
    }

    private void spawnBosses() {
        if (wave.bosses.isEmpty()) {
            return;
        }
        Engine engine = getEngine();
        Entity enemy = factory.enemy(randomLocationOutsideViewport(
                START_SPAWN_LIMIT + random.nextFloat() * (END_SPAWN_LIMIT - START_SPAWN_LIMIT)
        ), randomType(wave.bosses), true);
        engine.addEntity(enemy);
    }

    private void spawnPasta() {
        getEngine().addEntity(factory.reward(RewardType.PASTA, randomLocationOutsideViewport()));
    }

    private Vector2 randomLocationOutsideViewport() {
        return randomLocationOutsideViewport(1);
    }

    private final Vector2 ray = new Vector2();

    private Vector2 randomLocationOutsideViewport(float paddingGradient) {
        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;
        // pick a random angle
        double alpha = random.nextDouble(Math.PI * 2);
        float viewportRadius = viewportRadius(viewportWidth, viewportHeight);
        float radius = viewportRadius * paddingGradient;
        float x = camera.position.x + (float) (Math.cos(alpha) * radius);
        float y = camera.position.y + (float) (Math.sin(alpha) * radius);

        return new Vector2(x, y);
    }

    private float viewportRadius(float viewportWidth, float viewportHeight) {
        return ray.set(camera.position.x + viewportWidth / 2, camera.position.y + viewportHeight / 2).dst(camera.position.x, camera.position.y);
    }

    private void spawnEnemies() {
        Engine engine = getEngine();
        for (int i = 0; i < SPAWN_ENEMIES_COUNT * currentWave; i++) {
            Entity enemy = factory.enemy(randomLocationOutsideViewport(
                    START_SPAWN_LIMIT + random.nextFloat() * (END_SPAWN_LIMIT - START_SPAWN_LIMIT)
            ), randomType(wave.types), false);
            engine.addEntity(enemy);
        }
    }

    private EnemyType randomType(List<EnemyType> types) {
        return random.getRandomElement(types);
    }
}
