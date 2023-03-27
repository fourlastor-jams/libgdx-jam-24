package io.github.fourlastor.game.level.enemy;

import io.github.fourlastor.game.level.component.Player.Settings;
import io.github.fourlastor.game.level.reward.RewardType;
import java.util.Arrays;
import java.util.List;

public enum EnemyType {
    PIGEON_0(0.2f, 1, 0.3f, 0.15f, "pigeon-0", 5, "pigeon 0.wav", RewardType.XP_S),
    PIGEON_1(0.3f, 15, 0.25f, 0.15f, "pigeon-1", 8, "pigeon 0.wav", RewardType.XP_S),
    SATCHMO(0.5f, 45, 0.2f, 0.15f, "satchmo", 16, "satchmo.wav", RewardType.XP_S),
    SPARK(0.5f, 35, 0.32f, 0.15f, "spark", 12, "325462__insanity54__laugh001.ogg", RewardType.XP_S),
    ANGRY_PINEAPPLE_0(
            0.5f, 55, 0.1f, 0.15f, "angry-pineapple-0", 20, "fruit squish.wav", RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_1(
            0.5f, 65, 0.2f, 0.15f, "angry-pineapple-1", 20, "fruit squish.wav", RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_2(
            0.5f, 75, 0.3f, 0.15f, "angry-pineapple-2", 20, "fruit squish.wav", RewardType.XP_M, RewardType.XP_S),
    DRAGON_QUEEN(0.5f, 1_300, 0.22f, 0.2f, "dragon-queen", 25, "dragonQueen0.wav", RewardType.XP_M, RewardType.XP_S),
    RAELEUS(0.5f, 1_620, 0.32f, 0.2f, "raeleus", 30, "horse.wav", RewardType.XP_M, RewardType.XP_S),
    HYDROLIEN(0.5f, 2_900, 0.3f, 0.2f, "hydrolien", 40, "fox bark.wav", RewardType.XP_M, RewardType.XP_S),
    LAVA_EATER(
            0.5f,
            2_350,
            0.3f,
            0.2f,
            "lava-eater",
            30,
            "174499__unfa__boiling-towel.wav",
            RewardType.XP_M,
            RewardType.XP_S),
    LYZE(
            0.5f,
            5_000,
            0.3f,
            0.2f,
            "lyze",
            30,
            "owl hoot.wav",
            RewardType.XP_M,
            RewardType.XP_M,
            RewardType.XP_L,
            RewardType.XP_M),
    PANDA(
            0.5f,
            5_800,
            0.2f,
            0.2f,
            "panda",
            30,
            "bear death.wav",
            RewardType.XP_M,
            RewardType.XP_M,
            RewardType.XP_L,
            RewardType.XP_M),
    ;

    public final float size;
    public final int maxHealth;
    public final float speed;
    public final float frameDuration;
    public final String animationPath;
    public final List<RewardType> rewards;
    public final float damage;
    public final String deathSound;

    EnemyType(
            float size,
            int maxHealth,
            float speed,
            float frameDuration,
            String animationPath,
            float damage,
            String deathSound,
            RewardType... rewards) {
        this.size = size;
        this.maxHealth = maxHealth;
        this.speed = speed * Settings.PLAYER_SPEED;
        this.frameDuration = frameDuration;
        this.animationPath = animationPath;
        this.damage = damage;
        this.deathSound = deathSound;
        this.rewards = Arrays.asList(rewards);
    }
}
