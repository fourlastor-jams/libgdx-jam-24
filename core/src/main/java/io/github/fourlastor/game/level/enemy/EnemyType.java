package io.github.fourlastor.game.level.enemy;

import com.badlogic.gdx.assets.AssetManager;
import io.github.fourlastor.game.level.component.Player.Settings;
import io.github.fourlastor.game.level.reward.RewardType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum EnemyType {
    PIGEON_0(0.2f, 1, 0.3f, 0.4f, "pigeon-0", 5, "pigeon 0.wav", RewardType.XP_S),
    PIGEON_1(0.3f, 15, 0.25f, 0.3f, "pigeon-1", 7, "pigeon 0.wav", RewardType.XP_S),
    SATCHMO(0.5f, 30, 0.2f, 0.1f, "satchmo", 15, "satchmo.wav", RewardType.XP_S),
    SPARK(0.5f, 20, 0.32f, 0.1f, "spark", 10, "325462__insanity54__laugh001.wav", RewardType.XP_S),
    ANGRY_PINEAPPLE_0(
            0.5f, 30, 0.3f, 0.1f, "angry-pineapple-0", 10, "fruit squish.wav", RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_1(
            0.5f, 30, 0.3f, 0.1f, "angry-pineapple-1", 10, "fruit squish.wav", RewardType.XP_M, RewardType.XP_S),
    ANGRY_PINEAPPLE_2(
            0.5f, 30, 0.3f, 0.1f, "angry-pineapple-2", 10, "fruit squish.wav", RewardType.XP_M, RewardType.XP_S),
    DRAGON_QUEEN(0.5f, 30, 0.3f, 0.1f, "dragon-queen", 10, "dragonQueen0.wav", RewardType.XP_M, RewardType.XP_S),
    HYDROLIEN(0.5f, 30, 0.3f, 0.1f, "hydrolien", 10, "fox bark.wav", RewardType.XP_M, RewardType.XP_S),
    LAVA_EATER(0.5f, 30, 0.3f, 0.1f, "lava-eater", 10, "174499__unfa__boiling-towel.wav", RewardType.XP_M, RewardType.XP_S),
    LYZE(0.5f, 30, 0.3f, 0.1f, "lyze", 10, "owl hoot.wav", RewardType.XP_M, RewardType.XP_S),
    PANDA(0.5f, 30, 0.3f, 0.1f, "panda", 10, "bear death.wav", RewardType.XP_M, RewardType.XP_S),
    RAELEUS(0.5f, 30, 0.3f, 0.1f, "raeleus", 10, "horse.wav", RewardType.XP_M, RewardType.XP_S),
    ;

    public final float size;
    public final int maxHealth;
    public final float speed;
    public final float frameDuration;
    public final String animationPath;
    public final Set<RewardType> rewards;
    public final float damage;
    public final String deathSound;

    EnemyType(
            float size,
            int maxHealth,
            float speed,
            float frameDuration,
            String animationPath,
            float damage,
            String deathSound, RewardType... rewards) {
        this.size = size;
        this.maxHealth = maxHealth;
        this.speed = speed * Settings.PLAYER_SPEED;
        this.frameDuration = frameDuration;
        this.animationPath = animationPath;
        this.damage = damage;
        this.deathSound = deathSound;
        this.rewards = new HashSet<>(Arrays.asList(rewards));
    }
}
