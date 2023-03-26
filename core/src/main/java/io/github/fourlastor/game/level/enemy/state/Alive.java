package io.github.fourlastor.game.level.enemy.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TextraLabel;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.component.Enemy;
import io.github.fourlastor.game.level.physics.BodyHelper;
import squidpony.squidmath.SilkRNG;

import javax.inject.Named;

public class Alive extends EnemyState {

    private final Vector2 targetVelocity = new Vector2();
    private final Vector2 impulse = new Vector2();
    private final SilkRNG random;
    private final BodyHelper helper;
    private final Font font;

    @AssistedInject
    public Alive(@Assisted ImmutableArray<Entity> players, Dependencies mappers, SilkRNG random, BodyHelper helper, @Named("hp") Font font) {
        super(mappers, players);
        this.random = random;
        this.helper = helper;
        this.font = font;
    }

    @Override
    public void update(Entity entity) {
        super.update(entity);
        Body body = bodies.get(entity).body;
        Enemy enemy = enemies.get(entity);
        Vector2 position = body.getPosition();
        Vector2 playerPosition = findClosestPlayer();
        targetVelocity.set(playerPosition).sub(position).nor().scl(enemy.type.speed);
        Actor actor = actors.get(entity).actor;
        float targetScale = Math.abs(actor.getScaleX()) * Math.signum(targetVelocity.x);
        actor.setScaleX(targetScale);
        body.applyLinearImpulse(helper.velocityAsImpulse(body, targetVelocity, impulse), body.getWorldCenter(), false);
    }

    private Vector2 findClosestPlayer() {
        return bodies.get(playersEntities.get(0)).body.getPosition();
    }

    @Override
    public boolean onMessage(Entity entity, Telegram msg) {
        if (entity != msg.extraInfo) {
            return false;
        }
        Enemy enemy = enemies.get(entity);
        int baseDamage = players.get(playersEntities.get(0)).weaponDamage;
        float weaponDamage = baseDamage + baseDamage * (random.nextFloat(0.5f) - 0.25f);
        Actor actor = actors.get(entity).actor;
        Stage stage = actor.getStage();
        TextraLabel hpLabel = new TextraLabel(String.valueOf(Math.round(weaponDamage)), font);
        Group group = new Group();
        group.setScale(0f);
        group.addActor(hpLabel);
        group.addAction(Actions.sequence(
                Actions.scaleTo(1/64f, 1/64f, 0.5f),
                Actions.removeActor()
        ));
        group.setPosition(actor.getX(), actor.getY() + actor.getHeight() * actor.getScaleY());
        stage.addActor(group);
        enemy.health -= weaponDamage;
        if (enemy.health <= 0) {
            enemy.stateMachine.changeState(enemy.dead);
        } else {
            enemy.stateMachine.changeState(enemy.knocked);
        }
        return true;
    }

    @AssistedFactory
    public interface Factory {
        Alive create(ImmutableArray<Entity> players);
    }
}
