package io.github.fourlastor.game.level.enemy;

public enum EnemyType {
    PIGEON_0(0.2f, "pigeon-0"),
    PIGEON_1(0.3f, "pigeon-1"),
    ;

    public final float size;
    public final String animationPath;

    EnemyType(float size, String animationPath) {
        this.size = size;
        this.animationPath = animationPath;
    }
}
