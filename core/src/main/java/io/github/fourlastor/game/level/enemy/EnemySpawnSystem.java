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
import squidpony.squidmath.SilkRNG;

import javax.inject.Inject;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family ENEMY_FAMILY = Family.all(EnemyAi.class).get();

    private final Camera camera;
    private final EntitiesFactory factory;
    private final SilkRNG random;

    private float runTime = 0f;
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
        runTime += deltaTime;
        if (entities.size() < 300 && runTime > 5f) {
            runTime = 0f;
            spawnEnemies();
        }
    }

    private void spawnEnemies() {
        Engine engine = getEngine();
        for (int i = 0; i < 30; i++) {
            Entity enemy = factory.enemy(randomLocation(), randomType());
            engine.addEntity(enemy);
        }
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
