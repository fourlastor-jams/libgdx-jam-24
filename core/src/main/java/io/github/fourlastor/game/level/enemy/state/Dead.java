package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.Enemy;

public class Dead extends EnemyState {

    private float timer;
    private float blackTimer;
    private boolean black;
    private final ComponentMapper<ActorComponent> actors;

    @AssistedInject
    public Dead(
            @Assisted ImmutableArray<Entity> players, Dependencies mappers, ComponentMapper<ActorComponent> actors) {
        super(mappers, players);
        this.actors = actors;
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        timer = 0f;
        blackTimer = 0f;
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
