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
import squidpony.squidmath.SilkRNG;

import javax.inject.Inject;
import java.util.LinkedList;

import static java.util.Arrays.asList;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family ENEMY_FAMILY = Family.all(Enemy.class).get();
    private static final float SPAWN_INTERVAL = 2f;
    private static final float PASTA_INTERVAL = 15f;
    private static final int MAX_ENEMIES_COUNT = 300;
    private static final int SPAWN_ENEMIES_COUNT = 5;

    private final LinkedList<EnemyWave> waves = new LinkedList<>(asList(
            new EnemyWave(asList(EnemyType.PIGEON_0, EnemyType.PIGEON_1), 30f),
            new EnemyWave(asList(EnemyType.SATCHMO, EnemyType.SPARK), 60f),
            new EnemyWave(
                    asList(EnemyType.ANGRY_PINEAPPLE_0, EnemyType.ANGRY_PINEAPPLE_1, EnemyType.ANGRY_PINEAPPLE_2), 90f),
            new EnemyWave(asList(EnemyType.DRAGON_QUEEN, EnemyType.RAELEUS), 120f),
            new EnemyWave(asList(EnemyType.HYDROLIEN, EnemyType.LAVA_EATER), 150f),
            new EnemyWave(asList(EnemyType.LYZE, EnemyType.PANDA), 180f)));

    private EnemyWave wave = waves.poll();

    private final Camera camera;
    private final EntitiesFactory factory;
    private final SilkRNG random;

    private float totalTime = 0f;
    private float spawnTime = 0f;
    private float pastaTime = 0f;
    private ImmutableArray<Entity> entities;

    @Inject
    public EnemySpawnSystem(Camera camera, EntitiesFactory factory, SilkRNG random) {
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
            if (!waves.isEmpty()) {
                wave = waves.poll();
            }
        }
        // spawning enemies
        if (entities.size() < MAX_ENEMIES_COUNT && spawnTime > SPAWN_INTERVAL) {
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
        for (int i = 0; i < SPAWN_ENEMIES_COUNT; i++) {
            Entity enemy = factory.enemy(randomLocationOutsideViewport(), randomType());
            engine.addEntity(enemy);
        }
    }

    private EnemyType randomType() {
        return random.getRandomElement(wave.types);
    }
}
