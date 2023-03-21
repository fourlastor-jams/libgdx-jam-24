package io.github.fourlastor.game.level.di;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.input.PlayerInputSystem;
import io.github.fourlastor.game.level.physics.PhysicsDebugSystem;
import io.github.fourlastor.game.level.physics.PhysicsSystem;
import io.github.fourlastor.game.level.system.ActorFollowBodySystem;
import io.github.fourlastor.game.level.system.CameraMovementSystem;
import io.github.fourlastor.game.level.system.ClearScreenSystem;
import io.github.fourlastor.game.level.enemy.EnemyAiSystem;
import io.github.fourlastor.game.level.enemy.EnemySpawnSystem;
import io.github.fourlastor.game.level.system.StageSystem;

@Module
public class LevelModule {

    @Provides
    @ScreenScoped
    public Engine engine(
            PlayerInputSystem playerInputSystem,
            EnemyAiSystem enemyAiSystem,
            CameraMovementSystem cameraMovementSystem,
            PhysicsSystem physicsSystem,
            StageSystem stageSystem,
            ClearScreenSystem clearScreenSystem,
            ActorFollowBodySystem actorFollowBodySystem,
            EnemySpawnSystem enemySpawnSystem,
            @SuppressWarnings("unused") // debug only
                    PhysicsDebugSystem physicsDebugSystem) {
        Engine engine = new Engine();
        engine.addSystem(playerInputSystem);
        engine.addSystem(enemyAiSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(actorFollowBodySystem);
        engine.addSystem(cameraMovementSystem);
        engine.addSystem(clearScreenSystem);
        engine.addSystem(stageSystem);
        engine.addSystem(enemySpawnSystem);
        engine.addSystem(physicsDebugSystem);
        return engine;
    }

    @Provides
    @Layers
    public Class<? extends Enum<?>> layersEnum() {
        return ActorComponent.Layer.class;
    }

    @Provides
    @ScreenScoped
    public Viewport viewport() {
        return new FitViewport(16f, 9f);
    }

    @Provides
    @ScreenScoped
    public Stage stage(Viewport viewport) {
        return new Stage(viewport);
    }

    @Provides
    @ScreenScoped
    public Camera camera(Viewport viewport) {
        return viewport.getCamera();
    }

    @Provides
    @ScreenScoped
    @Gravity
    public Vector2 gravity() {
        return new Vector2(0f, 0f);
    }

    @Provides
    @ScreenScoped
    public World world(@Gravity Vector2 gravity) {
        return new World(gravity, true);
    }

    @Provides
    @ScreenScoped
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }
}
