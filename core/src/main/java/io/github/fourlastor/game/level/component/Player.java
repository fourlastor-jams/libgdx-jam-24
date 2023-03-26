package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.ObjectSet;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Dead;
import io.github.fourlastor.game.level.input.state.OnGround;
import io.github.fourlastor.game.ui.PlayerActor;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class Player implements Component {

    private static final float NEXT_LEVEL_RATIO = 1.14f;
    public final Camera camera;
    public final InputStateMachine stateMachine;
    public final OnGround onGround;
    public final Dead dead;

    public final Settings settings;
    public final PlayerActor actor;
    public float movementTime = 0f;

    public ObjectSet<PowerUp> powerUps = new ObjectSet<>(PowerUp.values().length);
    public float xp = 0f;

    public int level = 1;
    public int weaponDamage = 10;
    public float whipWaitTime = 2;
    private float levelXpAddition = 100;
    public float nextLevelXp = levelXpAddition; // 100, 200, 300, 500, 1000
    public float maxHp = 1000f;
    public float hp = maxHp;
    public int killCounter = 0;

    public Player(
            Camera camera,
            InputStateMachine stateMachine,
            OnGround onGround,
            Dead dead,
            Settings settings,
            PlayerActor actor) {
        this.camera = camera;
        this.stateMachine = stateMachine;
        this.onGround = onGround;
        this.dead = dead;
        this.settings = settings;
        this.actor = actor;
    }

    public void addXp(float newXp) {
        xp += newXp;
        if (xp >= nextLevelXp) {
            levelUp();
        }
    }

    private void levelUp() {
        float remainder = xp - nextLevelXp;
        level += 1;
        levelXpAddition *= NEXT_LEVEL_RATIO;
        nextLevelXp += levelXpAddition;
        xp = remainder;
        if (level % 2 == 0) {
            weaponDamage += 10;
        } else {
            whipWaitTime -= Math.max(0.1f, 0.3f);
        }
    }

    public boolean hasPowerUp(PowerUp powerUp) {
        return powerUps.contains(powerUp);
    }

    public void upgradePowerUp() {
        if (!hasPowerUp(PowerUp.BACK_ATTACK)) {
            powerUps.add(PowerUp.BACK_ATTACK);
        } else if (!hasPowerUp(PowerUp.TOP_ATTACK)) {
            powerUps.add(PowerUp.TOP_ATTACK);
        } else if (!hasPowerUp(PowerUp.BOTTOM_ATTACK)) {
            powerUps.add(PowerUp.BOTTOM_ATTACK);
        }
    }

    public static class Settings {

        public static final float PLAYER_SPEED = 5f;
        public final float speed = PLAYER_SPEED;
        public final float accelerationTime;

        public Settings(float accelerationTime) {
            this.accelerationTime = accelerationTime;
        }
    }

    public enum PowerUp {
        BACK_ATTACK,
        TOP_ATTACK,
        BOTTOM_ATTACK,
    }
}
