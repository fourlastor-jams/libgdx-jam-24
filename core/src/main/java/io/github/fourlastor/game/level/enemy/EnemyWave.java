package io.github.fourlastor.game.level.enemy;

import java.util.List;

public class EnemyWave {

    public final List<EnemyType> types;
    public final float time;

    public EnemyWave(List<EnemyType> types, float time) {
        this.types = types;
        this.time = time;
    }
}
