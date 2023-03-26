package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.enemy.EnemyStateMachine;
import io.github.fourlastor.game.level.enemy.EnemyType;
import io.github.fourlastor.game.level.enemy.state.Alive;
import io.github.fourlastor.game.level.enemy.state.Dead;
import io.github.fourlastor.game.level.enemy.state.Knocked;

public class Enemy implements Component {

    public final EnemyStateMachine stateMachine;
    public final Alive alive;
    public final Dead dead;
    public final EnemyType type;
    public final Knocked knocked;
    public final boolean boss;
    public final int maxHealth;
    public int health;

    public Enemy(EnemyStateMachine stateMachine, Alive alive, Dead dead, Knocked knocked, boolean boss, EnemyType type) {
        this.stateMachine = stateMachine;
        this.alive = alive;
        this.dead = dead;
        this.knocked = knocked;
        this.boss = boss;
        this.type = type;
        maxHealth = boss ? type.maxHealth * 10 : type.maxHealth;
        health = maxHealth;
    }

    public static class Request implements Component {
        public final EnemyType type;
        public final boolean boss;

        public Request(EnemyType type, boolean boss) {

            this.type = type;
            this.boss = boss;
        }
    }

    public static class Delete implements Component {}
}
