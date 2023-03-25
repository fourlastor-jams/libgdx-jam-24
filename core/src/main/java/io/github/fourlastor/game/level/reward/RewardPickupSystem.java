package io.github.fourlastor.game.level.reward;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.component.Reward;
import javax.inject.Inject;

public class RewardPickupSystem extends EntitySystem {

    private static final Family FAMILY =
            Family.all(Reward.class, Reward.PickUp.class).get();

    private final ComponentMapper<Player> players;
    private final ComponentMapper<Reward> rewards;
    private final ComponentMapper<Reward.PickUp> pickups;
    private final RewardsListener rewardsListener = new RewardsListener();
    private final AssetManager assetManager;
    private final SoundController soundController;
    private Sound xp0Sound;
    private Sound xp1Sound;
    private Sound xp2Sound;
    private Sound pastaSound;

    @Inject
    public RewardPickupSystem(
            ComponentMapper<Player> players, ComponentMapper<Reward> rewards, ComponentMapper<Reward.PickUp> pickups, AssetManager assetManager, SoundController soundController) {
        this.players = players;
        this.rewards = rewards;
        this.pickups = pickups;
        this.assetManager = assetManager;
        xp0Sound = assetManager.get("audio/sounds/pickups/xp 0.wav", Sound.class);
        xp1Sound = assetManager.get("audio/sounds/pickups/xp 1.wav", Sound.class);
        xp2Sound = assetManager.get("audio/sounds/pickups/xp 2.wav", Sound.class);
        pastaSound = assetManager.get("audio/sounds/pickups/pasta.wav", Sound.class);
        this.soundController = soundController;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(FAMILY, rewardsListener);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(rewardsListener);
        super.removedFromEngine(engine);
    }

    private class RewardsListener implements EntityListener {
        @Override
        public void entityAdded(Entity entity) {
            Entity playerEntity = pickups.get(entity).player;
            Player player = players.get(playerEntity);
            RewardType type = rewards.get(entity).type;
            switch (type) {
                case XP_S:
                    player.xp += 10;
                    soundController.play(xp0Sound);
                    break;
                case XP_M:
                    player.xp += 20;
                    soundController.play(xp1Sound);
                    break;
                case XP_L:
                    player.xp += 30;
                    soundController.play(xp2Sound);
                    break;
                case PASTA:
                    player.hp += player.maxHp * 0.25f;
                    player.hp = Math.min(player.maxHp, player.hp);
                    soundController.play(pastaSound);
                    break;
            }
            getEngine().removeEntity(entity);
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
