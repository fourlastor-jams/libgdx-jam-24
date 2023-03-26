package io.github.fourlastor.game.level.enemy;

import java.util.List;

public class EnemyWave {

    public final List<EnemyType> types;
    public final List<EnemyType> bosses;
    public final float time;

    public EnemyWave(
            List<EnemyType> types,
            List<EnemyType> bosses,
            float time
    ) {
        this.types = types;
        this.bosses = bosses;
        this.time = time;
    }
}
