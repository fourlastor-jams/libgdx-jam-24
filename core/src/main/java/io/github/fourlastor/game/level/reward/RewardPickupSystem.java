package io.github.fourlastor.game.level.reward;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
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

    @Inject
    public RewardPickupSystem(
            ComponentMapper<Player> players, ComponentMapper<Reward> rewards, ComponentMapper<Reward.PickUp> pickups) {
        this.players = players;
        this.rewards = rewards;
        this.pickups = pickups;
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
                    break;
                case XP_M:
                    player.xp += 20;
                    break;
                case XP_L:
                    player.xp += 30;
                    break;
                case PASTA:
                    player.hp += player.maxHp * 0.25f;
                    player.hp = Math.min(player.maxHp, player.hp);
                    break;
            }
            getEngine().removeEntity(entity);
        }

        @Override
        public void entityRemoved(Entity entity) {}
    }
}
