package io.github.fourlastor.game.level.enemy;

public enum EnemyType {
    PIGEON_0(0.2f, 0.3f, "pigeon-0"),
    PIGEON_1(0.3f, 0.4f, "pigeon-1"),
    ;

    public final float size;
    public final float frameDuration;
    public final String animationPath;

    EnemyType(float size, float frameDuration, String animationPath) {
        this.size = size;
        this.frameDuration = frameDuration;
        this.animationPath = animationPath;
    }
}
