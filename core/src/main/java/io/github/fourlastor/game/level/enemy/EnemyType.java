package io.github.fourlastor.game.level.enemy;

import io.github.fourlastor.game.level.component.Player.Settings;
import io.github.fourlastor.game.level.reward.RewardType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum EnemyType {
    PIGEON_0(0.2f, 2f, Settings.PLAYER_SPEED * 0.4f, "pigeon-0", 10, RewardType.XP_S),
    PIGEON_1(0.3f, 1.5f, Settings.PLAYER_SPEED * 0.3f, "pigeon-1", 15, RewardType.XP_M, RewardType.XP_S),
    ;

    public final float size;
    public final float speed;
    public final float frameDuration;
    public final String animationPath;
    public final Set<RewardType> rewards;
    public final float damage;

    EnemyType(float size, float speed, float frameDuration, String animationPath, float damage, RewardType... rewards) {
        this.size = size;
        this.speed = speed;
        this.frameDuration = frameDuration;
        this.animationPath = animationPath;
        this.damage = damage;
        this.rewards = new HashSet<>(Arrays.asList(rewards));
    }
}
