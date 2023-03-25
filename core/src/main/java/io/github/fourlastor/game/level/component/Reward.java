package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.level.reward.RewardType;

public class Reward implements Component {
    public final RewardType type;

    public Reward(RewardType type) {
        this.type = type;
    }

    public static class PickUp implements Component {
        public final Entity player;

        public PickUp(Entity player) {
            this.player = player;
        }
    }
}
