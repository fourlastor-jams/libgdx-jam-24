package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.enemy.EnemyStateMachine;
import io.github.fourlastor.game.level.enemy.EnemyType;
import io.github.fourlastor.game.level.enemy.state.Alive;
import io.github.fourlastor.game.level.enemy.state.Dead;

public class Enemy implements Component {

    public final EnemyStateMachine stateMachine;
    public final Alive alive;
    public final Dead dead;
    public final EnemyType type;
    public final int maxHealth = 5;
    public int health = maxHealth;

    public Enemy(EnemyStateMachine stateMachine, Alive alive, Dead dead, EnemyType type) {
        this.stateMachine = stateMachine;
        this.alive = alive;
        this.dead = dead;
        this.type = type;
    }

    public static class Request implements Component {
        public final EnemyType type;

        public Request(EnemyType type) {

            this.type = type;
        }
    }

    public static class Delete implements Component {}
}
