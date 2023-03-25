package io.github.fourlastor.game.level.enemy;

import io.github.fourlastor.game.level.component.Player.Settings;
import io.github.fourlastor.game.level.reward.RewardType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum EnemyType {
    PIGEON_0(0.2f, 25, 2f, Settings.PLAYER_SPEED * 0.4f, "pigeon-0", 10, RewardType.XP_S),
    PIGEON_1(0.3f, 15, 1.5f, Settings.PLAYER_SPEED * 0.3f, "pigeon-1", 15, RewardType.XP_M, RewardType.XP_S),
    SATCHMO(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "satchmo", 10, RewardType.XP_M, RewardType.XP_S),
    SPARK(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "spark", 10, RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_0(
            0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "angry-pineapple-0", 10, RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_1(
            0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "angry-pineapple-1", 10, RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_2(
            0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "angry-pineapple-2", 10, RewardType.XP_M, RewardType.XP_S),
    DRAGON_QUEEN(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "dragon-queen", 10, RewardType.XP_M, RewardType.XP_S),
    HYDROLIEN(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "hydrolien", 10, RewardType.XP_M, RewardType.XP_S),
    LAVA_EATER(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "lava-eater", 10, RewardType.XP_M, RewardType.XP_S),
    LYZE(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "lyze", 10, RewardType.XP_M, RewardType.XP_S),
    PANDA(0.5f, 30, 0.7f, Settings.PLAYER_SPEED * 0.1f, "panda", 10, RewardType.XP_M, RewardType.XP_S),
    RAELEUS(0.5f, 30, 2f, Settings.PLAYER_SPEED * 0.1f, "raeleus", 10, RewardType.XP_M, RewardType.XP_S),
    ;

    public final float size;
    public final int maxHealth;
    public final float speed;
    public final float frameDuration;
    public final String animationPath;
    public final Set<RewardType> rewards;
    public final float damage;

    EnemyType(
            float size,
            int maxHealth,
            float speed,
            float frameDuration,
            String animationPath,
            float damage,
            RewardType... rewards) {
        this.size = size;
        this.maxHealth = maxHealth;
        this.speed = speed;
        this.frameDuration = frameDuration;
        this.animationPath = animationPath;
        this.damage = damage;
        this.rewards = new HashSet<>(Arrays.asList(rewards));
    }
}
