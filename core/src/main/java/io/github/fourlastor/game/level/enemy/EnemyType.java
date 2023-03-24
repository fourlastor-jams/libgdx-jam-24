package io.github.fourlastor.game.level.enemy;

import io.github.fourlastor.game.level.reward.RewardType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum EnemyType {
    PIGEON_0(0.2f, 0.3f, "pigeon-0", RewardType.XP_S, RewardType.XP_M),
    PIGEON_1(0.3f, 0.4f, "pigeon-1", RewardType.XP_M, RewardType.XP_L),
    ;

    public final float size;
    public final float frameDuration;
    public final String animationPath;
    public final Set<RewardType> rewards;

    EnemyType(float size, float frameDuration, String animationPath, RewardType... rewards) {
        this.size = size;
        this.frameDuration = frameDuration;
        this.animationPath = animationPath;
        this.rewards = new HashSet<>(Arrays.asList(rewards));
    }
}
